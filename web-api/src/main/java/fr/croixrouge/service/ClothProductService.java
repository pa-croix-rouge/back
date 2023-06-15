package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.product.ClothProduct;
import fr.croixrouge.storage.repository.ClothProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClothProductService extends CRUDService<ID, ClothProduct, ClothProductRepository> {
    public ClothProductService(ClothProductRepository repository) {
        super(repository);
    }

    public ClothProduct findByLocalUnitIdAndId(ID localUnitId, ID id) {
        return this.repository.findByLocalUnitIdAndId(localUnitId, id).orElseThrow();
    }

    public List<ClothProduct> findAllByLocalUnitId(ID localUnitId) {
        return this.repository.findAllByLocalUnitId(localUnitId);
    }
}
