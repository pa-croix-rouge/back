package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.product.ClothProduct;
import fr.croixrouge.storage.repository.ClothProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ClothProductService extends CRUDService<ID, ClothProduct, ClothProductRepository> {
    public ClothProductService(ClothProductRepository repository) {
        super(repository);
    }
}
