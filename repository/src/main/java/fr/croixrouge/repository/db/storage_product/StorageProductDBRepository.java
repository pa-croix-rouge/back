package fr.croixrouge.repository.db.storage_product;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StorageProductDBRepository extends CrudRepository<StorageProductDB, Long> {
    @Query("select s from StorageProductDB s where s.productDB.id = ?1 and s.storageDB.id = ?2")
    Optional<StorageProductDB> findByProductDB_IdAndStorageDB_Id(Long id, Long id1);
    
    
    
}
