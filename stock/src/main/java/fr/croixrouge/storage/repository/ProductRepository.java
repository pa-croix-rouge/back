package fr.croixrouge.storage.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDRepository;
import fr.croixrouge.storage.model.product.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CRUDRepository<ID, Product> {

    List<Product> findAllWithProductLimit(ID productLimitId);

    Optional<Product> findByIdAndLocalUnitId(ID id, ID localUnitId);

    List<Product> findAllByLocalUnitId(ID localUnitId);

    boolean isDeleted(ID id);
}
