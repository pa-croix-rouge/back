package fr.croixrouge.storage.repository.memory;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.InMemoryCRUDRepository;
import fr.croixrouge.domain.repository.TimeStampIDGenerator;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.repository.StorageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryStorageRepository extends InMemoryCRUDRepository<ID, Storage> implements StorageRepository {
    public InMemoryStorageRepository(List<Storage> objects) {
        super(objects, new TimeStampIDGenerator());
    }

    public InMemoryStorageRepository() {
        super(new ArrayList<>(), new TimeStampIDGenerator());
    }

    @Override
    public Optional<Storage> findByLocalUnitIdAndId(ID localUnitId, ID id) {
        return this.objects.stream().filter(storage -> storage.getLocalUnit().getId().equals(localUnitId) && storage.getId().equals(id)).findFirst();
    }

    @Override
    public List<Storage> findAllByLocalUnitId(ID localUnitId) {
        return this.objects.stream().filter(storage -> storage.getLocalUnit().getId().equals(localUnitId)).toList();
    }

    @Override
    public boolean update(Storage storage) {
        return false;
    }
}
