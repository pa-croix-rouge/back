package fr.croixrouge.domain.repository;

import fr.croixrouge.domain.model.LocalUnit;

import java.util.Optional;

public interface CRUDRepository<K, V> {

    Optional<V> findById(K id);

    void save(V object);

    void delete(V object);
}
