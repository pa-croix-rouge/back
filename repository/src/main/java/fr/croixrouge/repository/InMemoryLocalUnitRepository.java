package fr.croixrouge.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.domain.repository.InMemoryCRUDRepository;
import fr.croixrouge.domain.repository.LocalUnitRepository;
import fr.croixrouge.domain.repository.TimeStampIDGenerator;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;


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

    @Override
    public Optional<LocalUnit> findByCode(String code) {
        return objects.stream().filter(localUnit -> localUnit.getCode().equals(code)).findFirst();
    }

    @Override
    public String regenerateSecret(ID localUnitId) {
        LocalUnit localUnit = objects.stream().filter(lu -> lu.getId().equals(localUnitId)).findFirst().orElse(null);
        if (localUnit == null) {
            return null;
        }
        Random random = new Random();
        String newCode = String.format("%s-%03d", localUnit.getAddress().getPostalCode(), random.nextInt(1000));
        LocalUnit updatedLocalUnit = new LocalUnit(localUnit.getId(), localUnit.getName(), localUnit.getAddress(), localUnit.getManagerUsername(), newCode);
        objects.remove(localUnit);
        objects.add(updatedLocalUnit);
        return newCode;
    }
}
