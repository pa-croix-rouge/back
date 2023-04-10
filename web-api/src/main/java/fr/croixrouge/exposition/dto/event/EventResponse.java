package fr.croixrouge.exposition.dto.event;

import fr.croixrouge.model.Event;

public class EventResponse {
    private String name;
    private String description;
    private String start;
    private String end;
    private String referrerId;
    private String localUnitId;
    private int numberOfParticipants;

    public EventResponse() {
    }

    public EventResponse(String name, String description, String start, String end, String referrerId, String localUnitId, int numberOfParticipants) {
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.referrerId = referrerId;
        this.localUnitId = localUnitId;
        this.numberOfParticipants = numberOfParticipants;
    }

    public static EventResponse fromEvent(Event event) {
        return new EventResponse(
                event.getName(),
                event.getDescription(),
                event.getStart().toString(),
                event.getEnd().toString(),
                event.getReferrerId(),
                event.getLocalUnitId(),
                event.getParticipants().size()
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

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }
}
