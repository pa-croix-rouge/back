package fr.croixrouge.repository.db.product;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductDBRepository extends CrudRepository<ProductDB, Long> {
    @Query("select p from ProductDB p where p.productLimitDB.id = ?1")
    List<ProductDB> findByProductLimitDB_Id(Long id);

    @Query("select (count(p) > 0) from ProductDB p where p.id = ?1 and p.deletionDate is not null")
    boolean existsByIdAndDeletionDateNotNull(Long id);

    @Query("select p from ProductDB p where p.id = ?1 and p.localUnitDB.localUnitID = ?2")
    Optional<ProductDB> findByIdAndLocalUnitDB_LocalUnitID(Long id, Long localUnitID);

    @Query("select p from ProductDB p where p.localUnitDB.localUnitID = ?1")
    List<ProductDB> findByLocalUnitDB_LocalUnitID(Long localUnitID);

}
