package fr.croixrouge.exposition.dto.event;

public class EventRegistrationRequest {
    private String eventId;
    private String sessionId;
    private String timeWindowId;
    private String participantId;

    public EventRegistrationRequest() {
    }

    public EventRegistrationRequest(String eventId, String sessionId, String timeWindowId, String participantId) {
        this.eventId = eventId;
        this.sessionId = sessionId;
        this.timeWindowId = timeWindowId;
        this.participantId = participantId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getTimeWindowId() {
        return timeWindowId;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setTimeWindowId(String timeWindowId) {
        this.timeWindowId = timeWindowId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }
}
