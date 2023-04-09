package fr.croixrouge.exposition.dto;

import fr.croixrouge.model.Event;

public class EventResponse {
    private String name;
    private String description;
    private String start;
    private String end;
    private String referrerId;
    private String localUnitId;

    public EventResponse() {
    }

    public EventResponse(String name, String description, String start, String end, String referrerId, String localUnitId) {
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.referrerId = referrerId;
        this.localUnitId = localUnitId;
    }

    public static EventResponse fromEvent(Event event) {
        return new EventResponse(
                event.getName(),
                event.getDescription(),
                event.getStart().toString(),
                event.getEnd().toString(),
                event.getReferrerId(),
                event.getLocalUnitId()
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
}
