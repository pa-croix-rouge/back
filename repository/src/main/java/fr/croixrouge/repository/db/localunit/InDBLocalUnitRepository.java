package fr.croixrouge.repository.db.localunit;

import fr.croixrouge.domain.model.Address;
import fr.croixrouge.domain.model.Department;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.domain.repository.LocalUnitRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class InDBLocalUnitRepository implements LocalUnitRepository {

    private final LocalUnitDBRepository localUnitDBRepository;

    public InDBLocalUnitRepository(LocalUnitDBRepository localUnitDBRepository) {
        this.localUnitDBRepository = localUnitDBRepository;
    }

    public LocalUnit toLocalUnit(LocalUnitDB localUnitDB) {
        if(localUnitDB == null)
            return null;

        return new LocalUnit(
                new ID(localUnitDB.getLocalUnitID()),
                localUnitDB.getName(),
                new Address(
                        Department.getDepartmentFromPostalCode(localUnitDB.getDepartment()),
                        localUnitDB.getPostalCode(),
                        localUnitDB.getCity(),
                        localUnitDB.getStreetNumberAndName()),
                localUnitDB.getManagerUsername(),
                localUnitDB.getCode()
        );
    }

    public LocalUnitDB toLocalUnitDB(LocalUnit localUnit) {
        if(localUnit == null)
            return null;

        return new LocalUnitDB(
                localUnit.getId().value(),
                localUnit.getName(),
                localUnit.getCode(),
                localUnit.getAddress().getDepartment().getCode(),
                localUnit.getAddress().getStreetNumberAndName(),
                localUnit.getAddress().getCity(),
                localUnit.getAddress().getPostalCode(),
                localUnit.getManagerUsername()
        );
    }

    @Override
    public Optional<LocalUnit> findById(ID id) {
        return localUnitDBRepository.findById(id.value()).map(this::toLocalUnit);
    }

    @Override
    public ID save(LocalUnit object) {
        LocalUnitDB localUnitDB = localUnitDBRepository.save(toLocalUnitDB(object));
        return new ID(localUnitDB.getLocalUnitID());
    }

    @Override
    public void delete(LocalUnit object) {
        localUnitDBRepository.delete(toLocalUnitDB(object));
    }

    @Override
    public List<LocalUnit> findAll() {
        return StreamSupport.stream(localUnitDBRepository.findAll().spliterator(), false).map(this::toLocalUnit).toList();
    }

    @Override
    public Optional<LocalUnit> findByPostalCode(String postalCode) {
        return localUnitDBRepository.findByPostalCodeIgnoreCase(postalCode).map(this::toLocalUnit);
    }

    @Override
    public Optional<LocalUnit> findByCode(String code) {
        return localUnitDBRepository.findByCodeIgnoreCase(code).map(this::toLocalUnit);
    }
}
