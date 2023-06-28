package fr.croixrouge.storage.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDRepository;
import fr.croixrouge.storage.model.product.FoodProduct;

import java.util.List;
import java.util.Optional;

public interface FoodProductRepository extends CRUDRepository<ID, FoodProduct> {

    Optional<FoodProduct> findByProductId(ID productId);

    Optional<FoodProduct> findByLocalUnitIdAndId(ID localUnitId, ID id);

    List<FoodProduct> findAllByLocalUnitId(ID localUnitId);

    List<FoodProduct> findAllSoonExpiredByLocalUnitId(ID localUnitId);
}
