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

    public Optional<Event> getEventById(String eventId) {
        return eventRepository.findById(eventId);
    }

    public List<Event> getEventsByLocalUnitId(String localUnitId) {
        return eventRepository.findByLocalUnitId(localUnitId);
    }

    public List<Event> getEventsByLocalUnitIdAndMonth(String localUnitId, int month) {
        return eventRepository.findByLocalUnitIdAndMonth(localUnitId, month);
    }
}
