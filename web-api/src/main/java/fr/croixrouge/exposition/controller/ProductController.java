package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.product.CreateProductDTO;
import fr.croixrouge.exposition.dto.product.ProductResponse;
import fr.croixrouge.service.ProductLimitService;
import fr.croixrouge.service.ProductService;
import fr.croixrouge.service.StorageProductService;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
