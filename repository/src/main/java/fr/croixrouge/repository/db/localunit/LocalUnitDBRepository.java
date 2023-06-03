package fr.croixrouge.repository.db.localunit;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LocalUnitDBRepository extends CrudRepository<LocalUnitDB, Long> {

    @Query("select l from LocalUnitDB l where upper(l.postalCode) = upper(?1)")
    Optional<LocalUnitDB> findByPostalCodeIgnoreCase(String postalCode);

    @Query("select l from LocalUnitDB l where upper(l.code) = upper(?1)")
    Optional<LocalUnitDB> findByCodeIgnoreCase(String code);


}
