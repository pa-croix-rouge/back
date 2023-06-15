package fr.croixrouge.storage.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDRepository;
import fr.croixrouge.storage.model.Storage;

import java.util.List;
import java.util.Optional;

public interface StorageRepository extends CRUDRepository<ID, Storage> {
    List<Storage> findAllByLocalUnitId(ID localUnitId);

    Optional<Storage> findByLocalUnitIdAndId(ID localUnitId, ID id);
}
