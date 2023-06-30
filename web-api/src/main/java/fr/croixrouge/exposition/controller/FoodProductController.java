package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.product.CreateFoodProductDTO;
import fr.croixrouge.exposition.dto.product.FoodProductResponse;
import fr.croixrouge.exposition.dto.product.FoodStorageProductResponse;
import fr.croixrouge.exposition.error.ErrorHandler;
import fr.croixrouge.service.*;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.FoodConservation;
import fr.croixrouge.storage.model.product.FoodProduct;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.model.product.ProductLimit;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/product/food")
public class FoodProductController extends ErrorHandler {

    private final FoodProductService service;
    private final ProductService productService;

    private final ProductLimitService productLimitService;

    private final StorageProductService storageProductService;

    private final StorageService storageService;

    private final AuthenticationService authenticationService;

    public FoodProductController(FoodProductService service, ProductService productService, ProductLimitService productLimitService, StorageProductService storageProductService, StorageService storageService, AuthenticationService authenticationService) {
        this.service = service;
        this.productService = productService;
        this.productLimitService = productLimitService;
        this.storageProductService = storageProductService;
        this.storageService = storageService;
        this.authenticationService = authenticationService;
    }

    public FoodProductResponse toDTO(FoodProduct model) {
        return new FoodProductResponse(model);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<FoodProductResponse> getByID(@PathVariable ID id, HttpServletRequest request) {
        ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        FoodProduct foodProduct = service.findByLocalUnitIdAndId(localUnitId, id);
        return ResponseEntity.ok(toDTO(foodProduct));
    }

    @GetMapping()
    public ResponseEntity<List<FoodProductResponse>> getAll(HttpServletRequest request) {
        ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        return ResponseEntity.ok(service.findAllByLocalUnitId(localUnitId).stream().map(this::toDTO).toList());
    }

    @PostMapping()
    public ResponseEntity<ID> post(@RequestBody CreateFoodProductDTO model, HttpServletRequest request) {
        ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        Storage storage = storageService.findByLocalUnitIdAndId(localUnitId, new ID(model.getStorageId()));
        if (storage == null) {
            return ResponseEntity.notFound().build();
        }

        ProductLimit productLimit = null;
        if (model.getLimitID() != null) {
            productLimit = productLimitService.findById(new ID(model.getLimitID()));
        }

        Product product = model.toModel(productLimit).getProduct();
        ID productId = productService.save(product);
        if (productId == null) {
            return ResponseEntity.badRequest().build();
        }

        Product productPersisted = productService.findById(productId);
        FoodProduct foodProduct = new FoodProduct(null, productPersisted, FoodConservation.fromLabel(model.getFoodConservation()), CreateFoodProductDTO.toLocalDateTime(model.getExpirationDate()), CreateFoodProductDTO.toLocalDateTime(model.getOptimalConsumptionDate()), model.getPrice());
        ID foodProductId = service.save(foodProduct);
        if (foodProductId == null) {
            return ResponseEntity.badRequest().build();
        }
        StorageProduct storageProduct = new StorageProduct(storage, productPersisted, model.getAmount());
        ID storageProductId = storageProductService.save(storageProduct);
        if (storageProductId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(foodProductId);
    }

    @PostMapping("/{id}")
    public ResponseEntity<ID> update(@PathVariable ID id, @RequestBody CreateFoodProductDTO model, HttpServletRequest request) {
        ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        FoodProduct foodProduct = service.findByLocalUnitIdAndId(localUnitId, id);
        if (foodProduct == null) {
            return ResponseEntity.notFound().build();
        }
        ProductLimit productLimit = null;
        if (model.getLimitID() != null) {
            productLimit = productLimitService.findById(new ID(model.getLimitID()));
        }

        Product product = new Product(foodProduct.getProduct().getId(), model.toModel().getProduct().getName(), model.toModel().getProduct().getQuantity(), productLimit);
        ID productId = productService.save(product);
        if (productId == null) {
            return ResponseEntity.badRequest().build();
        }

        Product productPersisted = productService.findById(productId);
        FoodProduct foodProductUpdated = new FoodProduct(id, productPersisted, model.toModel().getFoodConservation(), model.toModel().getExpirationDate(), model.toModel().getOptimalConsumptionDate(), model.getPrice());
        ID foodProductUpdatedId = service.save(foodProductUpdated);
        if (foodProductUpdatedId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(foodProductUpdatedId);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable ID id) {
        FoodProduct foodProduct = service.findById(id);
        if (foodProduct == null) {
            return ResponseEntity.notFound().build();
        }

        Product product = productService.findById(foodProduct.getProduct().getId());
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        if(product.getLimit() != null) {
            productLimitService.delete(product.getLimit());
        }

        StorageProduct storageProduct = storageProductService.findByProduct(product);
        if (storageProduct != null) {
            storageProductService.delete(storageProduct);
        }

        service.delete(foodProduct);
        productService.delete(product);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/expired")
    public ResponseEntity<List<FoodStorageProductResponse>> getExpired(HttpServletRequest request) {
        ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        var foodStorageProducts = storageProductService.getProductsByLocalUnit(localUnitId).getFoodProducts();
        var foods = foodStorageProducts.entrySet().stream().filter(entry -> entry.getValue().getExpirationDate().minusDays(7).isBefore(ZonedDateTime.now())).map(entry -> FoodStorageProductResponse.fromFoodProduct(entry.getValue(), entry.getKey())).sorted(Comparator.comparing(FoodStorageProductResponse::getId)).toList();
        return ResponseEntity.ok(foods);
    }
}
