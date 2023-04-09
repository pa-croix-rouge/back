package fr.croixrouge.service;

import fr.croixrouge.model.Event;
import fr.croixrouge.repository.EventRepository;
import org.springframework.stereotype.Service;

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
}
