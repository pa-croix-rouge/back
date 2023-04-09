package fr.croixrouge.exposition.controller;

import fr.croixrouge.exposition.dto.event.*;
import fr.croixrouge.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/details")
    public ResponseEntity<EventResponse> getEventById(@RequestBody EventRequest eventRequest) {
        final Optional<EventResponse> eventResponse = eventService.getEventById(eventRequest.getEventId()).map(EventResponse::fromEvent);
        return eventResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/details")
    public ResponseEntity createEvent(@RequestBody EventCreationRequest eventCreationRequest) {
        eventService.addEvent(eventCreationRequest.toEvent());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/details")
    public ResponseEntity deleteEvent(@RequestBody EventRequest eventRequest) {
        eventService.deleteEvent(eventRequest.getEventId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventResponse>> getEventsByLocalUnitId(@RequestBody EventForLocalUnitRequest eventForLocalUnitRequest) {
        final List<EventResponse> eventResponses = eventService.getEventsByLocalUnitId(eventForLocalUnitRequest.getLocalUnitId()).stream().map(EventResponse::fromEvent).collect(Collectors.toList());
        return ResponseEntity.ok(eventResponses);
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getEventsByLocalUnitIdAndMonth(@RequestBody EventForLocalUnitAndMonthRequest eventForLocalUnitAndMonthRequest) {
        final List<EventResponse> eventResponses = eventService.getEventsByLocalUnitIdAndMonth(eventForLocalUnitAndMonthRequest.getLocalUnitId(), eventForLocalUnitAndMonthRequest.getMonth(), eventForLocalUnitAndMonthRequest.getYear()).stream().map(EventResponse::fromEvent).collect(Collectors.toList());
        return ResponseEntity.ok(eventResponses);
    }

    @PostMapping("/register")
    public ResponseEntity registerParticipant(@RequestBody EventRegistrationRequest eventRegistrationRequest) {
        eventService.registerParticipant(eventRegistrationRequest.getEventId(), eventRegistrationRequest.getParticipantId());
        return ResponseEntity.ok().build();
    }
}
