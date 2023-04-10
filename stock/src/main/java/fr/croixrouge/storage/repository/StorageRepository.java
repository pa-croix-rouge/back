package fr.croixrouge.storage.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.Storage;

import java.util.Optional;

public interface StorageRepository {

    Optional<Storage> findById(ID id);

    void save(Storage storage);

    void delete(Storage storage);
}
