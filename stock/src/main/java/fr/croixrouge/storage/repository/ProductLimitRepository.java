package fr.croixrouge.storage.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDRepository;
import fr.croixrouge.storage.model.product.ProductLimit;

import java.util.List;
import java.util.Optional;

public interface ProductLimitRepository extends CRUDRepository<ID, ProductLimit> {

    Optional<ProductLimit> findByIdAndLocalUnitId(ID id, ID localUnitId);

    List<ProductLimit> findAllByLocalUnitId(ID localUnitId);

}
