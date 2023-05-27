package fr.croixrouge.repository.db.localunit;

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

    private LocalUnit toLocalUnit(LocalUnitDB localUnitDB) {
        return new LocalUnit(
                new ID(localUnitDB.getLocalUnitID()),
                localUnitDB.getName(),
                null,
                null,
                null
        );
    }

    private LocalUnitDB toLocalUnitDB(LocalUnit localUnit) {
        return new LocalUnitDB(
                localUnit.getId().value(),
                null,
                localUnit.getName()
        );
    }

    @Override
    public Optional<LocalUnit> findById(ID id) {
        return localUnitDBRepository.findById(id).map(this::toLocalUnit);
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
        return Optional.empty();
    }

    @Override
    public Optional<LocalUnit> findByCode(String code) {
        return Optional.empty();
    }
}
