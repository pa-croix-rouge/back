package fr.croixrouge.repository;

import fr.croixrouge.model.Event;

import java.util.Optional;

public interface EventRepository {

    Optional<Event> findById(String eventId);
}
