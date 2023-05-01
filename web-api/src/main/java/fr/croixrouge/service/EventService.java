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

    public Optional<Event> getEventByIdSessionId(ID eventId, ID sessionId) {
        return eventRepository.findByEventIdSessionId(eventId, sessionId);
    }

    public Optional<Event> getEventById(ID eventId) {
        return eventRepository.findById(eventId);
    }

    public List<Event> getEventsByLocalUnitId(ID localUnitId) {
        return eventRepository.findByLocalUnitId(localUnitId);
    }

    public List<Event> getEventsByLocalUnitIdAndMonth(ID localUnitId, int month, int year) {
        return eventRepository.findByLocalUnitIdAndMonth(localUnitId, month, year);
    }

    public ID addEvent(Event event) {
        return eventRepository.save(event);
    }

    public void deleteEvent(Event event) {
        eventRepository.delete(event);
    }

    public boolean registerParticipant(ID eventId, ID sessionId, ID participantId) {
        return eventRepository.registerParticipant(eventId, sessionId, participantId);
    }
}
