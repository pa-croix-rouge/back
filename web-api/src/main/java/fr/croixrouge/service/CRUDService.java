package fr.croixrouge.service;

import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDRepository;

import java.util.List;

public class CRUDService<K extends ID, V extends Entity<K>, R extends CRUDRepository<K, V>> {

    private final R repository;


    public CRUDService(R repository) {
        this.repository = repository;
    }

    public V findById(K id) {
        return repository.findById(id).orElseThrow();
    }

    public K save(V object) {
        return repository.save(object);
    }

    public void delete(V object) {
        repository.delete(object);
    }

    public List<V> findAll() {
        return repository.findAll();
    }

}
