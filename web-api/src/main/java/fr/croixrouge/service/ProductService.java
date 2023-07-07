package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService extends CRUDService<ID, Product, ProductRepository> {

    public ProductService(ProductRepository repository) {
        super(repository);
    }

    public Product findById(ID localUnitId, ID id) {
        var product = repository.findByIdAndLocalUnitId(id, localUnitId)
                .orElseThrow(NoSuchElementException::new);
        if (repository.isDeleted(product.getId())) {
            throw new NoSuchElementException();
        }
        return product;
    }

    public List<Product> findAll(ID localUnitId) {
        return repository.findAllByLocalUnitId(localUnitId);
    }

    public void removeAllProductLimit(ID productLimitId) {
        repository.findAllWithProductLimit(productLimitId)
                .forEach(product -> {
                    repository.save(new Product(
                            product.getId(),
                            product.getName(),
                            product.getQuantity(),
                            null,
                            product.getLocalUnitId()));
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
