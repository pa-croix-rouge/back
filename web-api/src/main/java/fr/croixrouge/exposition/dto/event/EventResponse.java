package fr.croixrouge.exposition.dto.event;

import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;

public class EventResponse {
    private String name;
    private String description;
    private String start;
    private String end;
    private String referrerId;
    private String localUnitId;
    private int maxParticipants;
    private int numberOfParticipants;

    public EventResponse() {
    }

    public EventResponse(String name, String description, String start, String end, String referrerId, String localUnitId, int maxParticipants, int numberOfParticipants) {
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.referrerId = referrerId;
        this.localUnitId = localUnitId;
        this.maxParticipants = maxParticipants;
        this.numberOfParticipants = numberOfParticipants;
    }

    public static EventResponse fromEvent(Event event, EventSession eventSession) {
        return new EventResponse(
                event.getName(),
                event.getDescription(),
                eventSession.getStart().toString(),
                eventSession.getEnd().toString(),
                event.getReferrerId().value(),
                event.getLocalUnitId().value(),
                eventSession.getMaxParticipants(),
                eventSession.getParticipants().size()
        );
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

    public String getReferrerId() {
        return referrerId;
    }

    public String getLocalUnitId() {
        return localUnitId;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }
}
