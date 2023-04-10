package fr.croixrouge.repository;

import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;

import java.util.ArrayList;
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
    public Optional<Event> findById(String eventId, String sessionId) {
        final Optional<Event> event =  Optional.ofNullable(events.get(eventId));
        if (event.isPresent()) {
            final Optional<EventSession> session = event.get().getSessions().stream().filter(s -> s.getId().equals(sessionId)).findFirst();
            if (session.isPresent()) {
                return Optional.of(new Event(event.get().getId(), event.get().getName(), event.get().getDescription(), event.get().getReferrerId(), event.get().getLocalUnitId(), event.get().getFirstStart(), event.get().getLastEnd(), List.of(session.get()), event.get().getOccurrences()));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Event> findByLocalUnitId(String localUnitId) {
        return this.events.values().stream().filter(event -> event.getLocalUnitId().equals(localUnitId)).collect(Collectors.toList());
    }

    @Override
    public List<Event> findByLocalUnitIdAndMonth(String localUnitId, int month, int year) {
        List<Event> localUnitEvents = this.events.values().stream().filter(event -> event.getLocalUnitId().equals(localUnitId)).toList();
        List<Event> result = new ArrayList<>();
        for (Event event : localUnitEvents) {
            List<EventSession> sessionsInMonth = event.getSessions().stream().filter(session -> session.getStart().getMonthValue() == month && session.getStart().getYear() == year || session.getEnd().getMonthValue() == month && session.getEnd().getYear() == year).toList();
            if (!sessionsInMonth.isEmpty()) {
                result.add(new Event(event.getId(), event.getName(), event.getDescription(), event.getReferrerId(), event.getLocalUnitId(), event.getFirstStart(), event.getLastEnd(), sessionsInMonth, event.getOccurrences()));
            }
        }
        return result;
    }

    @Override
    public void save(Event event) {
        String eventId = String.valueOf(nextId.getAndIncrement());
        Event eventToSave = new Event(eventId, event.getName(), event.getDescription(), event.getReferrerId(), event.getLocalUnitId(), event.getFirstStart(), event.getLastEnd(), new ArrayList<>(), event.getOccurrences());
        for (EventSession session : event.getSessions()) {
            eventToSave.getSessions().add(new EventSession(String.valueOf(eventToSave.getSessions().size()), session.getStart(), session.getEnd(), new ArrayList<>()));
        }
        this.events.put(eventId, eventToSave);
    }

    @Override
    public void delete(String eventId) {
        this.events.remove(eventId);
    }

    @Override
    public boolean registerParticipant(String eventId, String sessionId, String participantId) {
        Event event = this.events.get(eventId);
        if (event == null) {
            return false;
        }
        EventSession session = event.getSessions().stream().filter(s -> s.getId().equals(sessionId)).findFirst().orElse(null);
        if (session == null) {
            return false;
        }
        session.getParticipants().add(participantId);
        this.events.put(eventId, event);
        return true;
    }
}
