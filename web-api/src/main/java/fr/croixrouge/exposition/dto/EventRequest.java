package fr.croixrouge.exposition.dto;

public class EventRequest {

    private String eventId;

    public EventRequest() {
    }

    public EventRequest(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
