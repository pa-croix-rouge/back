package fr.croixrouge.domain.repository;

import fr.croixrouge.domain.model.ID;

public class IncrementalIDGenerator implements IDGenerator<ID> {
    private long lastId = 0;

    @Override
    public ID generate() {
        return new ID(++lastId);
    }
}
