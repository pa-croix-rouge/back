package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.CreateProductDTO;
import fr.croixrouge.exposition.dto.ProductResponse;
import fr.croixrouge.service.ProductService;
import fr.croixrouge.storage.model.product.Product;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController extends CRUDController<ID, Product, ProductService, ProductResponse, CreateProductDTO> {

    public ProductController(ProductService service) {
        super(service);
    }

    @Override
    public ProductResponse toDTO(Product model) {
        return new ProductResponse(model);
    }

}
