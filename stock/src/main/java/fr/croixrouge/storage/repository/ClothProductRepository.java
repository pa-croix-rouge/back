package fr.croixrouge.storage.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDRepository;
import fr.croixrouge.storage.model.product.ClothProduct;

import java.util.List;
import java.util.Optional;

public interface ClothProductRepository extends CRUDRepository<ID, ClothProduct> {

    Optional<ClothProduct> findByProductId(ID productId);

    Optional<ClothProduct> findByLocalUnitIdAndId(ID localUnitId, ID id);

    List<ClothProduct> findAllByLocalUnitId(ID localUnitId);
}
