package fr.croixrouge.repository.db.product_limit;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductLimitDBRepository extends CrudRepository<ProductLimitDB, Long> {

    @Query("select p from ProductLimitDB p where p.id = ?1 and p.localUnitDB.localUnitID = ?2")
    Optional<ProductLimitDB> findByIdAndLocalUnitDB_LocalUnitID(Long id, Long localUnitID);

    @Query("select p from ProductLimitDB p where p.localUnitDB.localUnitID = ?1")
    List<ProductLimitDB> findByLocalUnitDB_LocalUnitID(Long localUnitID);

}
