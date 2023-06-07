package fr.croixrouge.repository.db.storage;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StorageDBRepository extends CrudRepository<StorageDB, Long> {

    @Query("select s from StorageDB s where s.localUnitDB.localUnitID = ?1")
    List<StorageDB> findByLocalUnitDB_LocalUnitID(Long localUnitID);
}
