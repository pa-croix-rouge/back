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
        Department department = Department.getDepartmentFromPostalCode("91");
        String postalCode = "91240";
        String city = "St Michel sur Orge";
        String streetNumberAndName = "76 rue des Liers";
        Address address = new Address(department, postalCode, city, streetNumberAndName);
        String name = "Unite Local du Val d'Orge";
        String managerId = "2";
        LocalUnit localUnit = new LocalUnit(name, address, managerId);
        localUnits.put(postalCode, localUnit);
    }

    @Override
    public Optional<LocalUnit> findByPostalCode(String postalCode) {
        return Optional.ofNullable(localUnits.get(postalCode));
    }

    @Override
    public void save(LocalUnit localUnit) {
        localUnits.put(localUnit.getAddress().getPostalCode(), localUnit);
    }
}
