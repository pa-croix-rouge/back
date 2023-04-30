package fr.croixrouge.domain.model;

public class Entity<K extends ID> {

    protected K id;

    public Entity(K id) {
        this.id = id;
    }

    public K getId() {
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }
}
