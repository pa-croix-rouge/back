package fr.croixrouge.exposition.dto.event;

public class EventRegistrationRequest {
    private String eventId;
    private String participantId;

    public EventRegistrationRequest() {
    }

    public EventRegistrationRequest(String eventId, String participantId) {
        this.eventId = eventId;
        this.participantId = participantId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }
}
