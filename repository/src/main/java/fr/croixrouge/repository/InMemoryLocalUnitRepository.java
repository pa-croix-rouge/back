package fr.croixrouge.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.domain.repository.InMemoryCRUDRepository;
import fr.croixrouge.domain.repository.LocalUnitRepository;
import fr.croixrouge.domain.repository.TimeStampIDGenerator;

import java.util.ArrayList;
import java.util.Optional;


public class InMemoryLocalUnitRepository extends InMemoryCRUDRepository<ID, LocalUnit> implements LocalUnitRepository {

    public InMemoryLocalUnitRepository(ArrayList<LocalUnit> localUnits) {
        super(localUnits, new TimeStampIDGenerator());
    }

    public InMemoryLocalUnitRepository() {
        super(new ArrayList<>(), new TimeStampIDGenerator());
    }

    @Override
    public Optional<LocalUnit> findByPostalCode(String postalCode) {
        return objects.stream().filter(localUnit -> localUnit.getAddress().getPostalCode().equals(postalCode)).findFirst();
    }
}
