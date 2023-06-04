package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;
import fr.croixrouge.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService extends CRUDService<ID, Event, EventRepository> {

    public EventService(EventRepository eventRepository) {
        super(eventRepository);
    }

    public Optional<Event> findByEventIdAndSessionId(ID eventId, ID sessionId) {
        return repository.findByEventIdSessionId(eventId, sessionId);
    }

    public List<Event> findEventsByLocalUnitId(ID localUnitId) {
        return repository.findByLocalUnitId(localUnitId);
    }

    public List<EventSession> findByLocalUnitIdOver12Month(ID localUnitId) {
        return repository.findByLocalUnitIdOver12Month(localUnitId);
    }

    public List<Event> findEventsByLocalUnitIdAndMonth(ID localUnitId, int month, int year) {
        return repository.findByLocalUnitIdAndMonth(localUnitId, month, year);
    }

    public boolean registerParticipant(ID eventId, ID sessionId, ID participantId) {
        this.findById(eventId).getSessions()
                .stream()
                .filter(session -> session.getId().equals(sessionId))
                .filter(session -> session.getParticipants().size() < session.getMaxParticipants())
                .filter(session -> session.getParticipants().stream().noneMatch(participant -> participant.equals(participantId)))
                .findFirst()
                .ifPresentOrElse(session -> {
                    session.getParticipants().add(participantId);
                    repository.updateEventSession(session);
                }, () -> {
                    throw new IllegalArgumentException("Cannot register participant, event session doesn't exist, is full or participant already registered");
                });
        return true;
    }

    public boolean updateSingleEvent(ID eventId, ID sessionId, Event event) {
        return repository.updateSingleEvent(eventId, sessionId, event);
    }

    public boolean updateEventSessions(ID eventId, ID sessionId, Event event, int eventTimeWindowDuration, int eventTimeWindowOccurrence, int eventTimeWindowMaxParticipants) {
        return repository.updateEventSessions(eventId, sessionId, event, eventTimeWindowDuration, eventTimeWindowOccurrence, eventTimeWindowMaxParticipants);
    }

    public boolean deleteEvent(ID eventId, ID sessionId) {
        return repository.deleteEventSession(eventId, sessionId);
    }

    public void deleteEventSessions(Event event) {
        repository.delete(event);
    }
}
