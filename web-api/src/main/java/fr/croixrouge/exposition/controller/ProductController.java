package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.product.ConservationResponse;
import fr.croixrouge.exposition.dto.product.CreateProductDTO;
import fr.croixrouge.exposition.dto.product.ProductResponse;
import fr.croixrouge.exposition.dto.product.UnitResponse;
import fr.croixrouge.service.ProductLimitService;
import fr.croixrouge.service.ProductService;
import fr.croixrouge.service.StorageProductService;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.ClothSize;
import fr.croixrouge.storage.model.product.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController extends CRUDController<ID, Product, ProductService, ProductResponse, CreateProductDTO> {

    private final  ProductLimitService productLimitService;

    private final StorageProductService storageProductService;

    public ProductController(ProductService service, ProductLimitService productLimitService, StorageProductService storageProductService) {
        super(service);
        this.productLimitService = productLimitService;
        this.storageProductService = storageProductService;
    }

    @Override
    public ProductResponse toDTO(Product model) {
        return new ProductResponse(model);
    }

    @Override
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable ID id) {
        Product product = service.findById(id);
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

        service.delete(product);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/units")
    public ResponseEntity<UnitResponse> getUnits() {
        return ResponseEntity.ok(UnitResponse.fromMeasurementUnits());
    }

    @GetMapping("/conservations")
    public ResponseEntity<ConservationResponse> getConservations() {
        return ResponseEntity.ok(ConservationResponse.fromFoodConservations());
    }

    @GetMapping("/sizes")
    public ResponseEntity<List<String>> getSizes() {
        return ResponseEntity.ok(ClothSize.getAllSizes());
    }
}
