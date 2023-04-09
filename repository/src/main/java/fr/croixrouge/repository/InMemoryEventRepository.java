package fr.croixrouge.repository;

import fr.croixrouge.model.Event;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryEventRepository implements EventRepository {

    private final ConcurrentHashMap<String, Event> events;

    public InMemoryEventRepository(ConcurrentHashMap<String, Event> events) {
        this.events = events;
    }

    public InMemoryEventRepository() {
        this.events = new ConcurrentHashMap<>();
    }

    @Override
    public Optional<Event> findById(String eventId) {
        return Optional.ofNullable(events.get(eventId));
    }
}
