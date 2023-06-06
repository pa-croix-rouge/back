package fr.croixrouge.service;


import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.product.FoodProduct;
import fr.croixrouge.storage.repository.FoodProductRepository;
import org.springframework.stereotype.Service;

@Service
public class FoodProductService extends CRUDService<ID, FoodProduct, FoodProductRepository> {
    public FoodProductService(FoodProductRepository repository) {
        super(repository);
    }
}
