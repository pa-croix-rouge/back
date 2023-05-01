package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.repository.StorageRepository;
import org.springframework.stereotype.Service;

@Service
public class StorageService extends CRUDService<ID, Storage, StorageRepository> {
    public StorageService(StorageRepository repository) {
        super(repository);
    }
}
