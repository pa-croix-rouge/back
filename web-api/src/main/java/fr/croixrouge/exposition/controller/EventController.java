package fr.croixrouge.exposition.controller;

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
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/details")
    public ResponseEntity<SingleEventDetailedResponse> getEventById(@RequestBody SingleEventRequest singleEventRequest) {
        final Optional<SingleEventDetailedResponse> eventResponse = eventService.getEventById(singleEventRequest.getEventId(), singleEventRequest.getSessionId()).map(event -> SingleEventDetailedResponse.fromEvent(event, event.getSessions().get(0)));
        return eventResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/details")
    public ResponseEntity createEvent(@RequestBody SingleEventCreationRequest singleEventCreationRequest) {
        eventService.addEvent(singleEventCreationRequest.toEvent());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/details")
    public ResponseEntity deleteEvent(@RequestBody SingleEventRequest singleEventRequest) {
        eventService.deleteEvent(singleEventRequest.getEventId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventResponse>> getEventsByLocalUnitId(@RequestBody EventForLocalUnitRequest eventForLocalUnitRequest) {
        final List<EventResponse> eventResponse = new ArrayList<>();
        final List<Event> events = eventService.getEventsByLocalUnitId(eventForLocalUnitRequest.getLocalUnitId());
        for (Event event : events) {
            for (EventSession session : event.getSessions()) {
                eventResponse.add(EventResponse.fromEvent(event, session));
            }
        }
        return ResponseEntity.ok(eventResponse);
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getEventsByLocalUnitIdAndMonth(@RequestBody EventForLocalUnitAndMonthRequest eventForLocalUnitAndMonthRequest) {
        final List<EventResponse> eventResponse = new ArrayList<>();
        final List<Event> events = eventService.getEventsByLocalUnitIdAndMonth(eventForLocalUnitAndMonthRequest.getLocalUnitId(), eventForLocalUnitAndMonthRequest.getMonth(), eventForLocalUnitAndMonthRequest.getYear());
        for (Event event : events) {
            for (EventSession session : event.getSessions()) {
                eventResponse.add(EventResponse.fromEvent(event, session));
            }
        }
        return ResponseEntity.ok(eventResponse);
    }

    @PostMapping("/register")
    public ResponseEntity registerParticipant(@RequestBody EventRegistrationRequest eventRegistrationRequest) {
        eventService.registerParticipant(eventRegistrationRequest.getEventId(), eventRegistrationRequest.getSessionId(), eventRegistrationRequest.getParticipantId());
        return ResponseEntity.ok().build();
    }
}
