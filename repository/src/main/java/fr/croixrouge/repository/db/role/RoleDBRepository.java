package fr.croixrouge.repository.db.role;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface RoleDBRepository extends CrudRepository<RoleDB, Long> {
    @Query("select r from RoleDB r where r.localUnitDB.localUnitID = ?1")
    List<RoleDB> findByLocalUnitDB_LocalUnitID(@Nullable Long localUnitID);

}
