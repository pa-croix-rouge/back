package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.event.*;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;
import fr.croixrouge.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
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

    @GetMapping("/all/{localUnitId}")
    public ResponseEntity<List<EventResponse>> getEventsByLocalUnitId(@PathVariable ID localUnitId) {
        final List<EventResponse> eventResponse = new ArrayList<>();
        final List<Event> events = service.findEventsByLocalUnitId(localUnitId);
        for (Event event : events) {
            for (EventSession session : event.getSessions()) {
                eventResponse.add(EventResponse.fromEvent(event, session));
            }
        }
        return ResponseEntity.ok(eventResponse);
    }

    @GetMapping("/stats/{localUnitId}")
    public ResponseEntity<EventStatsResponse> getEventsStatsByLocalUnitId(@PathVariable ID localUnitId) {
        final List<EventSession> sessions = service.findByLocalUnitIdOver12Month(localUnitId);
        final YearMonth now = YearMonth.now();
        final List<EventSession> sessionList = new ArrayList<>(sessions);
        final List<EventSession> sessionListOverMonth = new ArrayList<>(sessions.stream().filter(session -> {
            YearMonth sessionDate = YearMonth.from(session.getStart());
            return sessionDate.equals(now);
        }).toList());
        final EventStatsResponse eventStatsResponse = new EventStatsResponse(sessionListOverMonth.size(), sessionListOverMonth.stream().map(eventSession -> eventSession.getParticipants().size()).reduce(0, Integer::sum), sessionList.size(), sessionList.stream().map(eventSession -> eventSession.getParticipants().size()).reduce(0, Integer::sum));
        return ResponseEntity.ok(eventStatsResponse);
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
    public ResponseEntity<String> registerParticipant(@RequestBody EventRegistrationRequest eventRegistrationRequest) {
        boolean isRegistered = service.registerParticipant(new ID(eventRegistrationRequest.getEventId()), new ID(eventRegistrationRequest.getSessionId()), new ID(eventRegistrationRequest.getParticipantId()));
        if (!isRegistered) {
            return ResponseEntity.internalServerError().body("Cannot register participant, event session doesn't exist, is full or participant already registered");
        }
        return ResponseEntity.ok().build();
    }
}
