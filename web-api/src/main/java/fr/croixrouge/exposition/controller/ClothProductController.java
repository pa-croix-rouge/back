package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.product.ClothProductResponse;
import fr.croixrouge.exposition.dto.product.CreateClothProductDTO;
import fr.croixrouge.exposition.error.ErrorHandler;
import fr.croixrouge.service.*;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product/cloth")
public class ClothProductController extends ErrorHandler {

    private final ClothProductService service;

    private final AuthenticationService authenticationService;

    private final StorageService storageService;

    private final ProductService productService;

    private final StorageProductService storageProductService;

    private final ProductLimitService productLimitService;

    public ClothProductController(ClothProductService service, AuthenticationService authenticationService, StorageService storageService, ProductService productService, StorageProductService storageProductService, ProductLimitService productLimitService) {
        this.service = service;
        this.authenticationService = authenticationService;
        this.storageService = storageService;
        this.productService = productService;
        this.storageProductService = storageProductService;
        this.productLimitService = productLimitService;
    }

    public ClothProductResponse toDTO(ClothProduct model) {
        return ClothProductResponse.fromClothProduct(model);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClothProductResponse> getByID(@PathVariable ID id, HttpServletRequest request) {
        ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        ClothProduct clothProduct = service.findByLocalUnitIdAndProductId(localUnitId, id);
        return ResponseEntity.ok(toDTO(clothProduct));
    }

    @GetMapping()
    public ResponseEntity<List<ClothProductResponse>> getAll(HttpServletRequest request) {
        ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        return ResponseEntity.ok(service.findAllByLocalUnitId(localUnitId).stream().map(this::toDTO).toList());
    }

    @PostMapping()
    public ResponseEntity<ID> create(@RequestBody CreateClothProductDTO createClothProductDTO, HttpServletRequest request) {
        ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        Storage storage = storageService.findByLocalUnitIdAndId(localUnitId, new ID(createClothProductDTO.getStorageId()));
        if (storage == null) {
            return ResponseEntity.notFound().build();
        }

        ProductLimit productLimit = null;
        if (createClothProductDTO.getLimitID() != null) {
            productLimit = productLimitService.findById(new ID(createClothProductDTO.getLimitID()));
        }

        Product product = createClothProductDTO.toModel(productLimit).getProduct();
        ID productId = productService.save(product);
        if (productId == null) {
            return ResponseEntity.badRequest().build();
        }

        Product productPersisted = productService.findById(productId);
        ClothProduct clothProduct = new ClothProduct(null, productPersisted, ClothSize.fromLabel(createClothProductDTO.getSize()), ClothGender.fromLabel(createClothProductDTO.getGender()));
        ID clothProductId = service.save(clothProduct);
        if (clothProductId == null) {
            return ResponseEntity.badRequest().build();
        }
        StorageProduct storageProduct = new StorageProduct(storage, productPersisted, createClothProductDTO.getAmount());
        ID storageProductId = storageProductService.save(storageProduct);
        if (storageProductId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(clothProductId);
    }

    @PostMapping("/{clothProductId}")
    public ResponseEntity<ID> update(@PathVariable ID clothProductId, @RequestBody CreateClothProductDTO createClothProductDTO, HttpServletRequest request) {
        ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        ClothProduct clothProduct = service.findByLocalUnitIdAndProductId(localUnitId, clothProductId);
        if (clothProduct == null) {
            return ResponseEntity.notFound().build();
        }

        ProductLimit productLimit = null;
        if (createClothProductDTO.getLimitID() != null) {
            productLimit = productLimitService.findById(new ID(createClothProductDTO.getLimitID()));
        }

        Product product = new Product(clothProduct.getProduct().getId(), createClothProductDTO.toModel().getProduct().getName(), createClothProductDTO.toModel().getProduct().getQuantity(), productLimit);
        ID productId = productService.save(product);
        if (productId == null) {
            return ResponseEntity.badRequest().build();
        }

        Product productPersisted = productService.findById(productId);
        ClothProduct clothProductUpdated = new ClothProduct(clothProductId, productPersisted, ClothSize.fromLabel(createClothProductDTO.getSize()), ClothGender.fromLabel(createClothProductDTO.getGender()));
        ID clothProductUpdatedId = service.save(clothProductUpdated);
        if (clothProductUpdatedId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(clothProductUpdatedId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id, HttpServletRequest request) {
        ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        ClothProduct clothProduct = service.findByLocalUnitIdAndProductId(localUnitId, id);
        if (clothProduct == null) {
            return ResponseEntity.notFound().build();
        }
        Product product = productService.findById(clothProduct.getProduct().getId());
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

        service.delete(clothProduct);
        productService.delete(product);
        return ResponseEntity.ok().build();
    }
}
