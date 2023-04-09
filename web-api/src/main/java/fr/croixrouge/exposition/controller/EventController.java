package fr.croixrouge.exposition.controller;

import fr.croixrouge.exposition.dto.EventForLocalUnitAndMonthRequest;
import fr.croixrouge.exposition.dto.EventForLocalUnitRequest;
import fr.croixrouge.exposition.dto.EventRequest;
import fr.croixrouge.exposition.dto.EventResponse;
import fr.croixrouge.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/all")
    public ResponseEntity<List<EventResponse>> getEventsByLocalUnitId(@RequestBody EventForLocalUnitRequest eventForLocalUnitRequest) {
        final List<EventResponse> eventResponses = eventService.getEventsByLocalUnitId(eventForLocalUnitRequest.getLocalUnitId()).stream().map(EventResponse::fromEvent).collect(Collectors.toList());
        return ResponseEntity.ok(eventResponses);
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getEventsByLocalUnitIdAndMonth(@RequestBody EventForLocalUnitAndMonthRequest eventForLocalUnitAndMonthRequest) {
        final List<EventResponse> eventResponses = eventService.getEventsByLocalUnitIdAndMonth(eventForLocalUnitAndMonthRequest.getLocalUnitId(), eventForLocalUnitAndMonthRequest.getMonth()).stream().map(EventResponse::fromEvent).collect(Collectors.toList());
        return ResponseEntity.ok(eventResponses);
    }
}
