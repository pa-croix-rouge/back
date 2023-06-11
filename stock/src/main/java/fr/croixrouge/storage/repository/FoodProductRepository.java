package fr.croixrouge.storage.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDRepository;
import fr.croixrouge.storage.model.product.FoodProduct;

import java.util.Optional;

public interface FoodProductRepository extends CRUDRepository<ID, FoodProduct> {

    Optional<FoodProduct> findByProductId(ID productId);
}
