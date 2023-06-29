package fr.croixrouge.domain.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.LocalUnit;

import java.util.Optional;

public interface LocalUnitRepository extends CRUDRepository<ID, LocalUnit> {

    Optional<LocalUnit> findByPostalCode(String postalCode);

    Optional<LocalUnit> findByCode(String code);

    String regenerateSecret(ID localUnitId);
}
