package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.event.*;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;
import fr.croixrouge.service.EventService;
import fr.croixrouge.service.LocalUnitService;
import fr.croixrouge.service.VolunteerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/event")
public class EventController extends CRUDController<ID, Event, EventService, EventResponse, EventCreationRequest> {

    private final VolunteerService volunteerService;

    private final LocalUnitService localUnitService;

    public EventController(EventService eventService, VolunteerService volunteerService, LocalUnitService localUnitService) {
        super(eventService);
        this.volunteerService = volunteerService;
        this.localUnitService = localUnitService;
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

    @PostMapping(value = "/details", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createSingleEvent(@RequestBody SingleEventCreationRequest singleEventCreationRequest) {
        var referrer = volunteerService.findById(new ID(singleEventCreationRequest.getReferrerId()));
        var localUnit = localUnitService.findById(new ID(singleEventCreationRequest.getLocalUnitId()));

        Long eventId = service.save(singleEventCreationRequest.toEvent(referrer, localUnit)).value();
        return ResponseEntity.ok(String.valueOf(eventId));
    }

    @PostMapping(value = "/details/{eventId}/{sessionId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> updateSingleEvent(@PathVariable ID eventId, @PathVariable ID sessionId, @RequestBody SingleEventCreationRequest singleEventCreationRequest) {
        var referrer = volunteerService.findById(new ID(singleEventCreationRequest.getReferrerId()));
        var localUnit = localUnitService.findById(new ID(singleEventCreationRequest.getLocalUnitId()));
        var event = service.findById(eventId);

        boolean result = service.updateSingleEvent(eventId, sessionId, singleEventCreationRequest.toEvent(referrer, localUnit));
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
        return ResponseEntity.ok(eventResponse.stream().sorted(Comparator.comparing(EventResponse::getStart)).toList());
    }

    @GetMapping("/trimester")
    public ResponseEntity<List<EventResponse>> getEventsByLocalUnitIdForTrimester(@RequestParam("localUnitId") ID localUnitId, @RequestParam("month") int month, @RequestParam("year") int year) {
        final List<EventResponse> eventResponse = new ArrayList<>();
        final List<Event> events = service.findEventsByLocalUnitIdAndTrimester(localUnitId, month, year);
        for (Event event : events) {
            for (EventSession session : event.getSessions()) {
                eventResponse.add(EventResponse.fromEvent(event, session));
            }
        }
        return ResponseEntity.ok(eventResponse.stream().sorted(Comparator.comparing(EventResponse::getStart)).toList());
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

    @PostMapping(value = "/sessions",consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createRecurrentEvent(@RequestBody RecurrentEventCreationRequest recurrentEventCreationRequest) {
        var referrer = volunteerService.findById(new ID(recurrentEventCreationRequest.getReferrerId()));
        var localUnit = localUnitService.findById(new ID(recurrentEventCreationRequest.getLocalUnitId()));

        Long eventId = service.save(recurrentEventCreationRequest.toEvent(referrer, localUnit)).value();
        return ResponseEntity.ok(String.valueOf(eventId));
    }

    @PostMapping(value = "/sessions/{eventId}/{sessionId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> updateRecurrentEvent(@PathVariable ID eventId, @PathVariable ID sessionId, @RequestBody SingleEventCreationRequest singleEventCreationRequest) {
        var referrer = volunteerService.findById(new ID(singleEventCreationRequest.getReferrerId()));
        var localUnit = localUnitService.findById(new ID(singleEventCreationRequest.getLocalUnitId()));

        boolean result = service.updateEventSessions(eventId, sessionId, singleEventCreationRequest.toEvent(referrer, localUnit),  singleEventCreationRequest.getEventTimeWindowDuration(), singleEventCreationRequest.getEventTimeWindowOccurrence(), singleEventCreationRequest.getEventTimeWindowMaxParticipants());
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

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> registerParticipant(@RequestBody EventRegistrationRequest eventRegistrationRequest) {
        boolean isRegistered = service.registerParticipant(new ID(eventRegistrationRequest.getEventId()), new ID(eventRegistrationRequest.getSessionId()), new ID(eventRegistrationRequest.getTimeWindowId()), new ID(eventRegistrationRequest.getParticipantId()));
        if (!isRegistered) {
            return ResponseEntity.internalServerError().body("Cannot register participant, event session doesn't exist, is full or participant already registered");
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/unregister", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> deleteParticipant(@RequestBody EventRegistrationRequest eventRegistrationRequest) {
        boolean isRegistered = service.removeParticipant(new ID(eventRegistrationRequest.getEventId()), new ID(eventRegistrationRequest.getSessionId()), new ID(eventRegistrationRequest.getTimeWindowId()), new ID(eventRegistrationRequest.getParticipantId()));
        if (!isRegistered) {
            return ResponseEntity.internalServerError().body("Cannot register participant, event session doesn't exist, is full or participant already registered");
        }
        return ResponseEntity.ok().build();
    }
}
