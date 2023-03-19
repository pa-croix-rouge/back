package fr.croixrouge.domain.repository;

import fr.croixrouge.domain.model.LocalUnit;

import java.util.Optional;

public interface LocalUnitRepository {

    Optional<LocalUnit> findByPostalCode(String postalCode);

    void save(LocalUnit localUnit);
}
