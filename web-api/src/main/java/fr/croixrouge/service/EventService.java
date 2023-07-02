package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventStats;
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

    public EventStats findByLocalUnitIdOver12Month(ID localUnitId) {
        return repository.findByLocalUnitIdOver12Month(localUnitId);
    }

    public List<Event> findEventsByLocalUnitIdAndMonth(ID localUnitId, int month, int year) {
        return repository.findByLocalUnitIdAndMonth(localUnitId, month, year);
    }

    public List<Event> findEventsByLocalUnitIdAndTrimester(ID localUnitId, int month, int year) {
        return repository.findByLocalUnitIdAndTrimester(localUnitId, month, year);
    }

    public boolean registerParticipant(ID eventId, ID sessionId, ID timeWindow, ID participantId) {
        return repository.registerParticipant(eventId, sessionId, timeWindow, participantId);
    }

    public boolean removeParticipant(ID eventId, ID sessionId, ID timeWindow, ID participantId) {
        return repository.removeParticipant(eventId, sessionId, timeWindow, participantId);
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
