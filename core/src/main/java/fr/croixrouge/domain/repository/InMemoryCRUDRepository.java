package fr.croixrouge.domain.repository;

import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;

import java.util.List;
import java.util.Optional;

public class InMemoryCRUDRepository<K extends ID, V extends Entity<K>> implements CRUDRepository<K, V> {


    protected final List<V> objects;

    protected final IDGenerator<K> idGenerator;

    public InMemoryCRUDRepository(List<V> objects, IDGenerator<K> idGenerator) {
        this.objects = objects;
        this.idGenerator = idGenerator;
    }

    @Override
    public Optional<V> findById(K id) {
        return objects.stream().filter(object -> object.getId().equals(id)).findFirst();
    }

    @Override
    public K save(V object) {

        if (object.getId() == null) {
            object.setId(idGenerator.generate());
        }

        objects.removeIf(o -> o.getId().equals(object.getId()));
        objects.add(object);
        return object.getId();
    }

    @Override
    public void delete(V object) {
        objects.remove(object);
    }

    @Override
    public List<V> findAll() {
        return objects;
    }
}
