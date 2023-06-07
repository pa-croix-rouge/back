package fr.croixrouge.repository.db.storage;

import fr.croixrouge.domain.model.Address;
import fr.croixrouge.domain.model.Department;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.repository.db.localunit.InDBLocalUnitRepository;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.repository.StorageRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class InDBStorageRepository implements StorageRepository {

    private final StorageDBRepository storageDBRepository;

    private final InDBLocalUnitRepository inDBLocalUnitRepository;


    public InDBStorageRepository(StorageDBRepository storageDBRepository, InDBLocalUnitRepository inDBLocalUnitRepository) {
        this.storageDBRepository = storageDBRepository;
        this.inDBLocalUnitRepository = inDBLocalUnitRepository;
    }

    public Storage toStorage(StorageDB storageDB) {
        return new Storage(
                new ID(storageDB.getId()),
                inDBLocalUnitRepository.toLocalUnit(storageDB.getLocalUnitDB()),
                new Address(
                        Department.getDepartmentFromPostalCode( storageDB.getDepartment()),
                        storageDB.getPostalCode(),
                        storageDB.getCity(),
                        storageDB.getStreetNumberAndName())
        );
    }

    public StorageDB toStorageDB(Storage storage) {
        return new StorageDB(
                storage.getId() == null ? null :storage.getId().value(),
                storage.getAddress().getDepartment().getCode(),
                storage.getAddress().getStreetNumberAndName(),
                storage.getAddress().getCity(),
                storage.getAddress().getPostalCode(),
                inDBLocalUnitRepository.toLocalUnitDB(storage.getLocalUnit())
        );
    }

    @Override
    public Optional<Storage> findById(ID id) {
        return storageDBRepository.findById(id.value()).map(this::toStorage);
    }

    @Override
    public ID save(Storage object) {
        return new ID(storageDBRepository.save(toStorageDB(object)).getId());
    }

    @Override
    public void delete(Storage object) {
        storageDBRepository.delete(toStorageDB(object));
    }

    @Override
    public List<Storage> findAll() {
        return StreamSupport.stream(storageDBRepository.findAll().spliterator(), false)
                .map(this::toStorage)
                .toList();
    }

    @Override
    public List<Storage> findAllByLocalUnitId(ID localUnitId) {
        return storageDBRepository.findByLocalUnitDB_LocalUnitID(localUnitId.value()).stream()
                .map(this::toStorage)
                .toList();
    }
}
