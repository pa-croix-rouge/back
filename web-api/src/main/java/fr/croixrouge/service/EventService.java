package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
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

    public Optional<Event> getEventById(ID eventId, ID sessionId) {
        return eventRepository.findById(eventId, sessionId);
    }

    public Optional<Event> getSessionsByEventId(ID eventId) {
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

    public void deleteEvent(ID eventId) {
        eventRepository.delete(eventId);
    }

    public boolean registerParticipant(ID eventId, ID sessionId, String participantId) {
        return eventRepository.registerParticipant(eventId, sessionId, participantId);
    }
}
