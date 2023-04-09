package fr.croixrouge.exposition.controller;

import fr.croixrouge.exposition.dto.EventRequest;
import fr.croixrouge.exposition.dto.EventResponse;
import fr.croixrouge.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<EventResponse> getEventById(@RequestBody EventRequest eventRequest) {
        final Optional<EventResponse> eventResponse = eventService.getEventById(eventRequest.getEventId()).map(EventResponse::fromEvent);
        return eventResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
