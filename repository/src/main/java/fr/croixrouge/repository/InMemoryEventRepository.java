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
        return Optional.of(new Event(event.get().getId(), event.get().getName(), event.get().getDescription(), event.get().getReferrerId(), event.get().getLocalUnitId(), event.get().getFirstStart(), event.get().getLastEnd(), List.of(session.get()), event.get().getOccurrences()));
    }

    @Override
    public List<Event> findByLocalUnitId(ID localUnitId) {
        return this.objects.stream().filter(event -> event.getLocalUnitId().equals(localUnitId)).collect(Collectors.toList());
    }

    @Override
    public List<EventSession> findByLocalUnitIdOver12Month(ID localUnitId) {
        ChronoZonedDateTime<LocalDate> now = ZonedDateTime.now();
        return this.objects.stream().filter(event -> event.getLocalUnitId().equals(localUnitId)).map(Event::getSessions).flatMap(List::stream).filter(session -> session.getStart().isAfter(now.minus(12, ChronoUnit.MONTHS))).toList();
    }

    @Override
    public List<Event> findByLocalUnitIdAndMonth(ID localUnitId, int month, int year) {
        List<Event> localUnitEvents = this.objects.stream().filter(event -> event.getLocalUnitId().equals(localUnitId)).toList();
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
    public ID save(Event event) {
        ID eventId = idGenerator.generate();
        Event eventToSave = new Event(eventId, event.getName(), event.getDescription(), event.getReferrerId(), event.getLocalUnitId(), event.getFirstStart(), event.getLastEnd(), new ArrayList<>(), event.getOccurrences());
        for (EventSession session : event.getSessions()) {
            eventToSave.getSessions().add(new EventSession(new ID(String.valueOf(eventToSave.getSessions().size())), session.getStart(), session.getEnd(), session.getMaxParticipants(), new ArrayList<>()));
        }
        this.objects.add(eventToSave);
        return eventId;
    }

    @Override
    public boolean registerParticipant(ID eventId, ID sessionId, ID participantId) {
        Event event = this.findById(eventId).orElse(null);
        if (event == null) {
            return false;
        }
        EventSession session = event.getSessions().stream().filter(s -> s.getId().equals(sessionId)).findFirst().orElse(null);
        if (session == null) {
            return false;
        }
        if (session.getParticipants().size() >= session.getMaxParticipants()) {
            return false;
        }
        if (session.getParticipants().contains(participantId)) {
            return false;
        }
        session.getParticipants().add(participantId);
        this.objects.add(event);
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
        event.getSessions().remove(session);
        this.objects.add(event);
        return true;
    }
}
