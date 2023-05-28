package fr.croixrouge.exposition.dto.event;

public class EventRegistrationRequest {
    private Long eventId;
    private Long sessionId;
    private Long timeWindowId;
    private Long participantId;

    public EventRegistrationRequest() {
    }

    public EventRegistrationRequest(Long eventId, Long sessionId, Long timeWindowId, Long participantId) {
        this.eventId = eventId;
        this.sessionId = sessionId;
        this.timeWindowId = timeWindowId;
        this.participantId = participantId;
    }

    public Long getEventId() {
        return eventId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public Ling getTimeWindowId() {
        return timeWindowId;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public void setTimeWindowId(Long timeWindowId) {
        this.timeWindowId = timeWindowId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }
}
