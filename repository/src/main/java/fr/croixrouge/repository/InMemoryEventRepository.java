package fr.croixrouge.repository;

import fr.croixrouge.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

    @Override
    public List<Event> findByLocalUnitId(String localUnitId) {
        return this.events.values().stream().filter(event -> event.getLocalUnitId().equals(localUnitId)).collect(Collectors.toList());
    }

    @Override
    public List<Event> findByLocalUnitIdAndMonth(String localUnitId, int month) {
        return this.events.values().stream().filter(event ->
                event.getLocalUnitId().equals(localUnitId) &&
                (event.getStart().getMonthValue() == month || event.getEnd().getMonthValue() == month))
                .collect(Collectors.toList());
    }
}
