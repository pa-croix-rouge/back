package fr.croixrouge.storage.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDRepository;
import fr.croixrouge.storage.model.product.ClothProduct;

import java.util.Optional;

public interface ClothProductRepository extends CRUDRepository<ID, ClothProduct> {

    Optional<ClothProduct> findByProductId(ID productId);

}
