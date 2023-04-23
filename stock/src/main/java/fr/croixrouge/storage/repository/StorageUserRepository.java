package fr.croixrouge.storage.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDRepository;
import fr.croixrouge.storage.model.StorageUser;

public interface StorageUserRepository extends CRUDRepository<ID, StorageUser> {
}
