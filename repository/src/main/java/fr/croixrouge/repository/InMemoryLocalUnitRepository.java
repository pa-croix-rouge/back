package fr.croixrouge.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.domain.repository.LocalUnitRepository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


public class InMemoryLocalUnitRepository implements LocalUnitRepository {

    private final ConcurrentHashMap<ID, LocalUnit> localUnits;

    public InMemoryLocalUnitRepository(ConcurrentHashMap<ID, LocalUnit> localUnits) {
        this.localUnits = localUnits;
    }

    public InMemoryLocalUnitRepository() {
        this.localUnits = new ConcurrentHashMap<>();
    }

    @Override
    public Optional<LocalUnit> findById(ID localUnitId) {
        return Optional.ofNullable(localUnits.get(localUnitId));
    }

    @Override
    public Optional<LocalUnit> findByPostalCode(String postalCode) {
        return localUnits.values().stream().filter(localUnit -> localUnit.getAddress().getPostalCode().equals(postalCode)).findFirst();
    }

    @Override
    public void save(LocalUnit localUnit) {
        localUnits.put(localUnit.getId(), localUnit);
    }
}
