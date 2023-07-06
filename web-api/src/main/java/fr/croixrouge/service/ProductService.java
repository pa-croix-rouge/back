package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ProductService extends CRUDService<ID, Product, ProductRepository> {

    public ProductService(ProductRepository repository) {
        super(repository);
    }

    public void removeAllProductLimit(ID productLimitId) {
        repository.findAllWithProductLimit(productLimitId)
                .forEach(product -> {
                    repository.save(new Product(
                            product.getId(),
                            product.getName(),
                            product.getQuantity(),
                            null
                    ));
                });
    }

    @Override
    public Product findById(ID id) {
        var product = super.findById(id);
        if (repository.isDeleted(product.getId())) {
            throw new NoSuchElementException();
        }
        return product;
    }
}
