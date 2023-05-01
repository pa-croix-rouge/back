package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.event.*;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;
import fr.croixrouge.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/event")
public class EventController extends CRUDController<ID, Event, EventService, EventResponse, EventCreationRequest> {

    public EventController(EventService eventService) {
        super(eventService);
    }

    @Override
    public EventResponse toDTO(Event model) {
        return null;
    }

    @GetMapping("/details")
    public ResponseEntity<SingleEventDetailedResponse> getEventById(@RequestBody SingleEventRequest singleEventRequest) {
        final Optional<SingleEventDetailedResponse> eventResponse = service.findByEventIdAndSessionId(new ID(singleEventRequest.getEventId()), new ID(singleEventRequest.getSessionId())).map(event -> SingleEventDetailedResponse.fromEvent(event, event.getSessions().get(0)));
        return eventResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/details")
    public ResponseEntity<String> createSingleEvent(@RequestBody SingleEventCreationRequest singleEventCreationRequest) {
        String eventId = service.save(singleEventCreationRequest.toEvent()).value();
        return ResponseEntity.ok(eventId);
    }

    @DeleteMapping("/details")
    public ResponseEntity deleteEvent(@RequestBody SingleEventRequest singleEventRequest) {
        Event event = service.findById(new ID(singleEventRequest.getEventId()));
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        service.delete(event);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventResponse>> getEventsByLocalUnitId(@RequestBody EventForLocalUnitRequest eventForLocalUnitRequest) {
        final List<EventResponse> eventResponse = new ArrayList<>();
        final List<Event> events = service.findEventsByLocalUnitId(new ID(eventForLocalUnitRequest.getLocalUnitId()));
        for (Event event : events) {
            for (EventSession session : event.getSessions()) {
                eventResponse.add(EventResponse.fromEvent(event, session));
            }
        }
        return ResponseEntity.ok(eventResponse);
    }

    @GetMapping("/date")
    public ResponseEntity<List<EventResponse>> getEventsByLocalUnitIdAndMonth(@RequestBody EventForLocalUnitAndMonthRequest eventForLocalUnitAndMonthRequest) {
        final List<EventResponse> eventResponse = new ArrayList<>();
        final List<Event> events = service.findEventsByLocalUnitIdAndMonth(new ID(eventForLocalUnitAndMonthRequest.getLocalUnitId()), eventForLocalUnitAndMonthRequest.getMonth(), eventForLocalUnitAndMonthRequest.getYear());
        for (Event event : events) {
            for (EventSession session : event.getSessions()) {
                eventResponse.add(EventResponse.fromEvent(event, session));
            }
        }
        return ResponseEntity.ok(eventResponse);
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<EventResponse>> getEventSessionsByEventId(@RequestBody SessionForEventRequest sessionForEventRequest) {
        final List<EventResponse> eventResponse = new ArrayList<>();
        final Event event = service.findById(new ID(sessionForEventRequest.getEventId()));
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        for (EventSession session : event.getSessions()) {
            eventResponse.add(EventResponse.fromEvent(event, session));
        }
        return ResponseEntity.ok(eventResponse);
    }

    @PostMapping("/sessions")
    public ResponseEntity<String> createRecurrentEvent(@RequestBody RecurrentEventCreationRequest recurrentEventCreationRequest) {
        String eventId = service.save(recurrentEventCreationRequest.toEvent()).value();
        return ResponseEntity.ok(eventId);
    }

    @PostMapping("/register")
    public ResponseEntity registerParticipant(@RequestBody EventRegistrationRequest eventRegistrationRequest) {
        service.registerParticipant(new ID(eventRegistrationRequest.getEventId()), new ID(eventRegistrationRequest.getSessionId()), new ID(eventRegistrationRequest.getParticipantId()));
        return ResponseEntity.ok().build();
    }
}
