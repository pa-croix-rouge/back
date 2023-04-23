package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.StorageUser;
import fr.croixrouge.storage.repository.StorageUserRepository;
import org.springframework.stereotype.Service;

@Service
public class StorageUserService extends CRUDService<ID, StorageUser, StorageUserRepository> {
    public StorageUserService(StorageUserRepository repository) {
        super(repository);
    }
}
