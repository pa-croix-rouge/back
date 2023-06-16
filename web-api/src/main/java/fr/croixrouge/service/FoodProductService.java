package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.product.FoodProduct;
import fr.croixrouge.storage.repository.FoodProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodProductService extends CRUDService<ID, FoodProduct, FoodProductRepository> {
    public FoodProductService(FoodProductRepository repository) {
        super(repository);
    }

    public FoodProduct findByLocalUnitIdAndId(ID localUnitId, ID id) {
        return this.repository.findByLocalUnitIdAndId(localUnitId, id).orElseThrow();
    }

    public List<FoodProduct> findAllByLocalUnitId(ID localUnitId) {
        return this.repository.findAllByLocalUnitId(localUnitId);
    }
}
