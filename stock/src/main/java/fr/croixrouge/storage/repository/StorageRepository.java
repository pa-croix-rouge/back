package fr.croixrouge.storage.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDRepository;
import fr.croixrouge.storage.model.Storage;

import java.util.List;

public interface StorageRepository extends CRUDRepository<ID, Storage> {
    List<Storage> findAllByLocalUnitId(ID localUnitId);
}
