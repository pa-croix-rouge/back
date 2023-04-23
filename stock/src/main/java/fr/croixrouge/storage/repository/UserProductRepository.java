package fr.croixrouge.storage.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDRepository;
import fr.croixrouge.storage.model.UserProduct;

import java.util.List;

public interface UserProductRepository extends CRUDRepository<ID, UserProduct> {

    List<UserProduct> findAll(String userId, ID productId);
}
