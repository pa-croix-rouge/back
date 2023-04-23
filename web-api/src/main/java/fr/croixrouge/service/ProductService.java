package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends CRUDService<ID, Product, ProductRepository> {

    public ProductService(ProductRepository repository) {
        super(repository);
    }

}
