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

    @GetMapping("/details/{eventId}/{sessionId}")
    public ResponseEntity<SingleEventDetailedResponse> getEventById(@PathVariable ID eventId, @PathVariable ID sessionId) {
        final Optional<SingleEventDetailedResponse> eventResponse = service.findByEventIdAndSessionId(eventId, sessionId).map(event -> SingleEventDetailedResponse.fromEvent(event, event.getSessions().get(0)));
        return eventResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/details")
    public ResponseEntity<String> createSingleEvent(@RequestBody SingleEventCreationRequest singleEventCreationRequest) {
        String eventId = service.save(singleEventCreationRequest.toEvent()).value();
        return ResponseEntity.ok(eventId);
    }

    @PostMapping("/details/{eventId}/{sessionId}")
    public ResponseEntity<String> updateSingleEvent(@PathVariable ID eventId, @PathVariable ID sessionId, @RequestBody SingleEventCreationRequest singleEventCreationRequest) {
        boolean result = service.updateSingleEvent(eventId, sessionId, singleEventCreationRequest.toEvent());
        if (!result) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/details/{eventId}/{sessionId}")
    public ResponseEntity<String> deleteEvent(@PathVariable ID eventId, @PathVariable ID sessionId) {
        boolean result = service.deleteEvent(eventId, sessionId);
        if (!result) {
            return ResponseEntity.notFound().build();
        }
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
        final EventStatsResponse eventStatsResponse = new EventStatsResponse(
                sessionListOverMonth.size(),
                sessionListOverMonth.stream().map(
                        eventSession -> eventSession.getTimeWindows().stream().map(
                                eventTimeWindow -> eventTimeWindow.getParticipants().size()
                        ).reduce(0, Integer::sum)
                ).reduce(0, Integer::sum),
                sessionList.size(),
                sessionList.stream().map(
                        eventSession -> eventSession.getTimeWindows().stream().map(
                                eventTimeWindow -> eventTimeWindow.getParticipants().size()
                        ).reduce(0, Integer::sum)
                ).reduce(0, Integer::sum));
        return ResponseEntity.ok(eventStatsResponse);
    }

    @GetMapping("/date")
    public ResponseEntity<List<EventResponse>> getEventsByLocalUnitIdAndMonth(@RequestParam("localUnitId") ID localUnitId, @RequestParam("month") int month, @RequestParam("year") int year) {
        final List<EventResponse> eventResponse = new ArrayList<>();
        final List<Event> events = service.findEventsByLocalUnitIdAndMonth(localUnitId, month, year);
        for (Event event : events) {
            for (EventSession session : event.getSessions()) {
                eventResponse.add(EventResponse.fromEvent(event, session));
            }
        }
        return ResponseEntity.ok(eventResponse);
    }

    @GetMapping("/sessions/{eventId}")
    public ResponseEntity<List<EventResponse>> getEventSessionsByEventId(@PathVariable ID eventId) {
        final List<EventResponse> eventResponse = new ArrayList<>();
        final Event event = service.findById(eventId);
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

    @PostMapping("/sessions/{eventId}/{sessionId}")
    public ResponseEntity<String> updateRecurrentEvent(@PathVariable ID eventId, @PathVariable ID sessionId, @RequestBody SingleEventCreationRequest singleEventCreationRequest) {
        boolean result = service.updateEventSessions(eventId, sessionId, singleEventCreationRequest.toEvent(), singleEventCreationRequest.getEventTimeWindowDuration(), singleEventCreationRequest.getEventTimeWindowOccurrence(), singleEventCreationRequest.getEventTimeWindowMaxParticipants());
        if (!result) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/sessions/{eventId}")
    public ResponseEntity<String> deleteEventSessionsByEventId(@PathVariable ID eventId) {
        final Event event = service.findById(eventId);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        service.deleteEventSessions(event);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerParticipant(@RequestBody EventRegistrationRequest eventRegistrationRequest) {
        boolean isRegistered = service.registerParticipant(new ID(eventRegistrationRequest.getEventId()), new ID(eventRegistrationRequest.getSessionId()), new ID(eventRegistrationRequest.getTimeWindowId()), new ID(eventRegistrationRequest.getParticipantId()));
        if (!isRegistered) {
            return ResponseEntity.internalServerError().body("Cannot register participant, event session doesn't exist, is full or participant already registered");
        }
        return ResponseEntity.ok().build();
    }
}
