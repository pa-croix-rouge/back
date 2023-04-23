package fr.croixrouge.storage.repository.memory;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDInMemoryRepository;
import fr.croixrouge.domain.repository.TimeStampIDGenerator;
import fr.croixrouge.storage.model.StorageUser;
import fr.croixrouge.storage.repository.StorageUserRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryStorageUserRepository extends CRUDInMemoryRepository<ID, StorageUser> implements StorageUserRepository {
    public InMemoryStorageUserRepository(List<StorageUser> objects) {
        super(objects, new TimeStampIDGenerator());
    }

    public InMemoryStorageUserRepository() {
        this(new ArrayList<>());
    }
}
