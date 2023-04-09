package fr.croixrouge.domain.repository;

import fr.croixrouge.domain.model.ID;

public interface IDGenerator<K extends ID> {

    K generate();
}
