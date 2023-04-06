package fr.croix.rouge.storage.repository;

import fr.croix.rouge.storage.model.Storage;
import fr.croixrouge.domain.model.ID;

import java.util.Optional;

public interface StorageRepository {

    Optional<Storage> findById(ID id);

    void save(Storage storage);

    void delete(Storage storage);
}
