package fr.croixrouge.repository;

import fr.croixrouge.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryEventRepository implements EventRepository {

    private final AtomicInteger nextId = new AtomicInteger(0);

    private final ConcurrentHashMap<String, Event> events;

    public InMemoryEventRepository(ConcurrentHashMap<String, Event> events) {
        this.events = events;
        nextId.set(events.size() + 1);
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
    public List<Event> findByLocalUnitIdAndMonth(String localUnitId, int month, int year) {
        return this.events.values().stream().filter(event ->
                event.getLocalUnitId().equals(localUnitId) &&
                ((event.getStart().getMonthValue() == month && event.getStart().getYear() == year))
                || event.getEnd().getMonthValue() == month && event.getEnd().getYear() == year)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Event event) {
        String eventId = String.valueOf(nextId.getAndIncrement());
        this.events.put(eventId, event);
    }

    @Override
    public void delete(String eventId) {
        this.events.remove(eventId);
    }

    @Override
    public void registerParticipant(String eventId, String participantId) {
        Event event = this.events.get(eventId);
        event.getParticipants().add(participantId);
        this.events.put(eventId, event);
    }
}
