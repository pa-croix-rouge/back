package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.product.ProductLimit;
import fr.croixrouge.storage.repository.ProductLimitRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductLimitService extends CRUDService<ID, ProductLimit, ProductLimitRepository>{

    public ProductLimitService(ProductLimitRepository productLimitRepository) {
        super(productLimitRepository);
    }

    public void deleteProductLimit(ProductLimit productLimit) {
        repository.delete(productLimit);
    }
}
