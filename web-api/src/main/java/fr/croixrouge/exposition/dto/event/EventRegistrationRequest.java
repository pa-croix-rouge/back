package fr.croixrouge.exposition.dto.event;

public class EventRegistrationRequest {
    private Long eventId;
    private Long sessionId;
    private Long participantId;

    public EventRegistrationRequest() {
    }

    public EventRegistrationRequest(Long eventId, Long sessionId, Long participantId) {
        this.eventId = eventId;
        this.sessionId = sessionId;
        this.participantId = participantId;
    }

    public Long getEventId() {
        return eventId;
    }

    public Long getSessionId() {
        return sessionId;
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

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }
}
