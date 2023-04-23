package fr.croixrouge.storage.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDRepository;
import fr.croixrouge.storage.model.StorageUserProduct;

import java.util.List;

public interface StorageUserProductRepository extends CRUDRepository<ID, StorageUserProduct> {

    List<StorageUserProduct> findAll(String userId, ID productId);
}
