package fr.croixrouge.repository.db.storage_product;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StorageProductDBRepository extends CrudRepository<StorageProductDB, Long> {
    @Query("select s from StorageProductDB s where s.productDB.id = ?1 and s.storageDB.id = ?2")
    Optional<StorageProductDB> findByProductDB_IdAndStorageDB_Id(Long id, Long id1);

    @Query("select s from StorageProductDB s where s.productDB.id = ?1")
    Optional<StorageProductDB> findByProductDB_Id(Long id);

    @Query("select s from StorageProductDB s where s.storageDB.id = ?1")
    List<StorageProductDB> findAllByStorageDB_Id(Long id);

    @Query("select s from StorageProductDB s where s.storageDB.localUnitDB.localUnitID = ?1")
    List<StorageProductDB> findAllByStorageDB_LocalUnitDB_Id(Long id);

    @Query("select s from StorageProductDB s where s.storageDB.localUnitDB.localUnitID = ?1 and s.productDB.id = ?2")
    Optional<StorageProductDB> findByStorageDB_LocalUnitDB_LocalUnitIDAndProductDB_Id(Long localUnitID, Long id);


}
