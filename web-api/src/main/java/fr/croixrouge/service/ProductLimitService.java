package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.product.CreateProductLimitDTO;
import fr.croixrouge.storage.model.product.ProductLimit;
import fr.croixrouge.storage.repository.ProductLimitRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class ProductLimitService extends CRUDService<ID, ProductLimit, ProductLimitRepository> {
    public ProductLimitService(ProductLimitRepository productLimitRepository) {
        super(productLimitRepository);
    }

    public void update(ID id, CreateProductLimitDTO createProductLimitDTO) {

        var productLimit = findById(id);

        var newProductLimit = new ProductLimit(id,
                createProductLimitDTO.getName() == null ? productLimit.getName() : createProductLimitDTO.getName(),
                createProductLimitDTO.getDuration() == null ? productLimit.getDuration() : Duration.ofDays(createProductLimitDTO.getDuration()),
                createProductLimitDTO.getQuantity() == null ? productLimit.getQuantity() : createProductLimitDTO.getQuantity().toQuantifier()
        );

        save(newProductLimit);

    }
}
