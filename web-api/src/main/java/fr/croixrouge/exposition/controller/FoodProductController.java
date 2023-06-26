package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.product.CreateFoodProductDTO;
import fr.croixrouge.exposition.dto.product.FoodProductResponse;
import fr.croixrouge.service.FoodProductService;
import fr.croixrouge.service.ProductLimitService;
import fr.croixrouge.service.ProductService;
import fr.croixrouge.service.StorageProductService;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.FoodProduct;
import fr.croixrouge.storage.model.product.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product/food")
public class FoodProductController extends CRUDController<ID, FoodProduct, FoodProductService, FoodProductResponse, CreateFoodProductDTO> {

    private final ProductService productService;

    private final ProductLimitService productLimitService;

    private final StorageProductService storageProductService;

    public FoodProductController(FoodProductService service, ProductService productService, ProductLimitService productLimitService, StorageProductService storageProductService) {
        super(service);
        this.productService = productService;
        this.productLimitService = productLimitService;
        this.storageProductService = storageProductService;
    }

    @Override
    public FoodProductResponse toDTO(FoodProduct model) {
        return new FoodProductResponse(model);
    }

    @Override
    @GetMapping(value = "/{id}")
    public ResponseEntity<FoodProductResponse> getByID(@PathVariable ID id) {
        FoodProduct foodProduct = service.findById(id);
        if (foodProduct == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(foodProduct));
    }

    @Override
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<ID> post(@RequestBody CreateFoodProductDTO model) {
        Product product = model.toModel().getProduct();
        ID productId = productService.save(product);
        if (productId == null) {
            return ResponseEntity.badRequest().build();
        }

        Product productPersisted = productService.findById(productId);
        FoodProduct foodProduct = new FoodProduct(null, productPersisted, model.getFoodConservation(), model.getExpirationDate(), model.getOptimalConsumptionDate(), model.getPrice());
        ID foodProductId = service.save(foodProduct);
        if (foodProductId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(foodProductId);
    }

    @Override
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
}
