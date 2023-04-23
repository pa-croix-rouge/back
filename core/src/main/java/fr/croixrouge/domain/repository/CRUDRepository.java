package fr.croixrouge.domain.repository;

import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;

import java.util.List;
import java.util.Optional;

public interface CRUDRepository<K extends ID, V extends Entity<K>> {
    Optional<V> findById(K id);

    void save(V object);

    void delete(V object);

    List<V> findAll();
}
