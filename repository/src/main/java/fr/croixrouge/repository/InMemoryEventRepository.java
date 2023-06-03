package fr.croixrouge.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.InMemoryCRUDRepository;
import fr.croixrouge.domain.repository.TimeStampIDGenerator;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryEventRepository extends InMemoryCRUDRepository<ID, Event> implements EventRepository {

    public InMemoryEventRepository(List<Event> objects) {
        super(objects, new TimeStampIDGenerator());
    }

    public InMemoryEventRepository() {
        super(new ArrayList<>(), new TimeStampIDGenerator());
    }

    @Override
    public Optional<Event> findByEventIdSessionId(ID eventId, ID sessionId) {
        final Optional<Event> event =  this.findById(eventId);
        if (event.isEmpty()) {
            return Optional.empty();
        }
        final Optional<EventSession> session = event.get().getSessions().stream().filter(s -> s.getId().equals(sessionId)).findFirst();
        if (session.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new Event(event.get().getId(), event.get().getName(), event.get().getDescription(), event.get().getReferrer(), event.get().getLocalUnit(), event.get().getFirstStart(), event.get().getLastEnd(), List.of(session.get()), event.get().getOccurrences()));
    }

    @Override
    public List<Event> findByLocalUnitId(ID localUnitId) {
        return this.objects.stream().filter(event -> event.getLocalUnit().getId().equals(localUnitId)).collect(Collectors.toList());
    }

    @Override
    public List<EventSession> findByLocalUnitIdOver12Month(ID localUnitId) {
        ChronoZonedDateTime<LocalDate> now = ZonedDateTime.now();
        return this.objects.stream()
                .filter(event -> event.getLocalUnit().getId().equals(localUnitId))
                .map(Event::getSessions)
                .flatMap(List::stream)
                .filter(session -> session.getStart().isAfter(now.minus(12, ChronoUnit.MONTHS)))
                .toList();
    }

    @Override
    public List<Event> findByLocalUnitIdAndMonth(ID localUnitId, int month, int year) {
        List<Event> localUnitEvents = this.objects.stream().filter(event -> event.getLocalUnit().getId().equals(localUnitId)).toList();
        List<Event> result = new ArrayList<>();
        for (Event event : localUnitEvents) {
            List<EventSession> sessionsInMonth = event.getSessions().stream().filter(session -> session.getStart().getMonthValue() == month && session.getStart().getYear() == year || session.getEnd().getMonthValue() == month && session.getEnd().getYear() == year).toList();
            if (!sessionsInMonth.isEmpty()) {
                result.add(new Event(event.getId(), event.getName(), event.getDescription(), event.getReferrer(), event.getLocalUnit(), event.getFirstStart(), event.getLastEnd(), sessionsInMonth, event.getOccurrences()));
            }
        }
        return result;
    }

    @Override
    public void updateEventSession(EventSession event) {
        Event eventToUpdate = this.findById(event.getId()).orElse(null);
        if (eventToUpdate == null) {
            return;
        }

        eventToUpdate.getSessions().remove(eventToUpdate);
        eventToUpdate.getSessions().add(event);
    }

    @Override
    public ID save(Event event) {
        ID eventId = idGenerator.generate();
        Event eventToSave = new Event(eventId, event.getName(), event.getDescription(), event.getReferrer(), event.getLocalUnit(), event.getFirstStart(), event.getLastEnd(), new ArrayList<>(), event.getOccurrences());
        for (EventSession session : event.getSessions()) {
            eventToSave.getSessions().add(new EventSession(new ID((long) eventToSave.getSessions().size()), session.getStart(), session.getEnd(), session.getMaxParticipants(), new ArrayList<>()));
        }
        this.objects.add(eventToSave);
        return eventId;
    }

    @Override
    public boolean updateSingleEvent(ID eventId, ID sessionId, Event event) {
        Event eventToUpdate = this.findById(eventId).orElse(null);
        if (eventToUpdate == null) {
            return false;
        }
        EventSession sessionToUpdate = eventToUpdate.getSessions().stream().filter(s -> s.getId().equals(sessionId)).findFirst().orElse(null);
        if (sessionToUpdate == null) {
            return false;
        }
        List<EventSession> updatedSessions = new ArrayList<>();
        for (EventSession session : eventToUpdate.getSessions()) {
            if (session.getId().equals(sessionId)) {
                updatedSessions.add(new EventSession(session.getId(), event.getSessions().get(0).getStart(), event.getSessions().get(0).getEnd(), event.getSessions().get(0).getMaxParticipants(), session.getParticipants()));
            } else {
                updatedSessions.add(session);
            }
        }
        Event updatedEvent = new Event(eventId, event.getName(), event.getDescription(), event.getReferrer(), event.getLocalUnit(), event.getFirstStart(), event.getLastEnd(), updatedSessions, eventToUpdate.getOccurrences());
        this.objects.remove(eventToUpdate);
        this.objects.add(updatedEvent);
        return true;
    }

    @Override
    public boolean updateEventSessions(ID eventId, ID sessionId, Event event) {
        Event eventToUpdate = this.findById(eventId).orElse(null);
        if (eventToUpdate == null) {
            return false;
        }
        EventSession sessionToUpdate = eventToUpdate.getSessions().stream().filter(s -> s.getId().equals(sessionId)).findFirst().orElse(null);
        if (sessionToUpdate == null) {
            return false;
        }
        List<EventSession> updatedSessions = new ArrayList<>();
        for (EventSession session : eventToUpdate.getSessions()) {
            if (session.getId().equals(sessionId)) {
                updatedSessions.add(new EventSession(session.getId(), event.getSessions().get(0).getStart(), event.getSessions().get(0).getEnd(), event.getSessions().get(0).getMaxParticipants(), session.getParticipants()));
            } else {
                updatedSessions.add(new EventSession(session.getId(), session.getStart(), session.getEnd(), event.getSessions().get(0).getMaxParticipants(), session.getParticipants()));
            }
        }
        Event updatedEvent = new Event(eventId, event.getName(), event.getDescription(), event.getReferrer(), event.getLocalUnit(), event.getFirstStart(), event.getLastEnd(), updatedSessions, eventToUpdate.getOccurrences());
        this.objects.remove(eventToUpdate);
        this.objects.add(updatedEvent);
        return true;
    }

    @Override
    public boolean deleteEventSession(ID eventId, ID sessionId) {
        Event event = this.findById(eventId).orElse(null);
        if (event == null) {
            return false;
        }
        EventSession session = event.getSessions().stream().filter(s -> s.getId().equals(sessionId)).findFirst().orElse(null);
        if (session == null) {
            return false;
        }
        this.objects.remove(event);
        event.getSessions().remove(session);
        this.objects.add(event);
        return true;
    }
}
