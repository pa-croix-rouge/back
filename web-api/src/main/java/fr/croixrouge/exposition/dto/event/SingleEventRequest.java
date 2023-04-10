package fr.croixrouge.exposition.dto.event;

public class SingleEventRequest {

    private String eventId;
    private String sessionId;

    public SingleEventRequest() {
    }

    public SingleEventRequest(String eventId, String sessionId) {
        this.eventId = eventId;
        this.sessionId = sessionId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
