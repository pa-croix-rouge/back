package fr.croixrouge.service;

import fr.croixrouge.model.Event;
import fr.croixrouge.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Optional<Event> getEventById(String eventId, String sessionId) {
        return eventRepository.findById(eventId, sessionId);
    }

    public Optional<Event> getSessionsByEventId(String eventId) {
        return eventRepository.findByEventId(eventId);
    }

    public List<Event> getEventsByLocalUnitId(String localUnitId) {
        return eventRepository.findByLocalUnitId(localUnitId);
    }

    public List<Event> getEventsByLocalUnitIdAndMonth(String localUnitId, int month, int year) {
        return eventRepository.findByLocalUnitIdAndMonth(localUnitId, month, year);
    }

    public void addEvent(Event event) {
        eventRepository.save(event);
    }

    public void deleteEvent(String eventId) {
        eventRepository.delete(eventId);
    }

    public boolean registerParticipant(String eventId, String sessionId, String participantId) {
        return eventRepository.registerParticipant(eventId, sessionId, participantId);
    }
}
