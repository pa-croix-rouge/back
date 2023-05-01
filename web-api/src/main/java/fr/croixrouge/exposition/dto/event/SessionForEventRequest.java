package fr.croixrouge.exposition.dto.event;

public class SessionForEventRequest {
    private String eventId;

    public SessionForEventRequest() {
    }

    public SessionForEventRequest(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
