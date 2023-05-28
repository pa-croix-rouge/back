package fr.croixrouge.exposition.dto.event;

import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;

public class EventResponse {
    private Long eventId;
    private Long sessionId;
    private String name;
    private String description;
    private String start;
    private String end;
    private Long referrerId;
    private Long localUnitId;
    private int maxParticipants;
    private int numberOfParticipants;
    private boolean isRecurring;

    public EventResponse() {
    }

    public EventResponse(Long eventId, Long sessionId, String name, String description, String start, String end, Long referrerId, Long localUnitId, int maxParticipants, int numberOfParticipants, boolean isRecurring) {
        this.eventId = eventId;
        this.sessionId = sessionId;
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.referrerId = referrerId;
        this.localUnitId = localUnitId;
        this.maxParticipants = maxParticipants;
        this.numberOfParticipants = numberOfParticipants;
        this.isRecurring = isRecurring;
    }

    public static EventResponse fromEvent(Event event, EventSession eventSession) {
        return new EventResponse(
                event.getId().value(),
                eventSession.getId().value(),
                event.getName(),
                event.getDescription(),
                eventSession.getStart().toString(),
                eventSession.getEnd().toString(),
                event.getReferrerId().value(),
                event.getLocalUnitId().value(),
                eventSession.getMaxParticipants(),
                eventSession.getParticipants().size(),
                event.getOccurrences() > 1
        );
    }

    public Long getEventId() {
        return eventId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public Long getReferrerId() {
        return referrerId;
    }

    public Long getLocalUnitId() {
        return localUnitId;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public boolean isRecurring() {
        return isRecurring;
    }
}
