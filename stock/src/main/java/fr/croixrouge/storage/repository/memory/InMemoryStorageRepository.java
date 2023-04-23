package fr.croixrouge.storage.repository.memory;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDInMemoryRepository;
import fr.croixrouge.domain.repository.TimeStampIDGenerator;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.repository.StorageRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryStorageRepository extends CRUDInMemoryRepository<ID, Storage> implements StorageRepository {
    public InMemoryStorageRepository(List<Storage> objects) {
        super(objects, new TimeStampIDGenerator());
    }

    public InMemoryStorageRepository() {
        super(new ArrayList<>(), new TimeStampIDGenerator());
    }
}
