package fr.croixrouge.infrastructure;

import fr.croixrouge.domain.model.Address;
import fr.croixrouge.domain.model.Department;
import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.domain.repository.LocalUnitRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryLocalUnitRepository implements LocalUnitRepository {

    private final ConcurrentHashMap<String, LocalUnit> localUnits;

    public InMemoryLocalUnitRepository() {
        this.localUnits = new ConcurrentHashMap<>();
        initializeValDOrgeLocalUnit();
    }

    private void initializeValDOrgeLocalUnit() {
        String localUnitId = "1";
        Department department = Department.getDepartmentFromPostalCode("91");
        String postalCode = "91240";
        String city = "St Michel sur Orge";
        String streetNumberAndName = "76 rue des Liers";
        Address address = new Address(department, postalCode, city, streetNumberAndName);
        String name = "Unite Local du Val d'Orge";
        String managerId = "2";
        LocalUnit localUnit = new LocalUnit(localUnitId, name, address, managerId);
        localUnits.put(localUnitId, localUnit);
    }

    @Override
    public Optional<LocalUnit> findById(String localUnitId) {
        return Optional.ofNullable(localUnits.get(localUnitId));
    }

    @Override
    public Optional<LocalUnit> findByPostalCode(String postalCode) {
        return localUnits.values().stream().filter(localUnit -> localUnit.getAddress().getPostalCode().equals(postalCode)).findFirst();
    }

    @Override
    public void save(LocalUnit localUnit) {
        localUnits.put(localUnit.getLocalUnitId(), localUnit);
    }
}
