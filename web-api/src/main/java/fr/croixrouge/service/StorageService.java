package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.repository.StorageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorageService extends CRUDService<ID, Storage, StorageRepository> {
    public StorageService(StorageRepository repository) {
        super(repository);
    }

    public List<Storage> findAllByLocalUnitId(ID localUnitId) {
        return repository.findAllByLocalUnitId(localUnitId);
    }

    public Storage findByLocalUnitIdAndId(ID localUnitId, ID id) {
        return repository.findByLocalUnitIdAndId(localUnitId, id).orElse(null);
    }
}
