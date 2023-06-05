package fr.croixrouge.exposition.dto.event;

import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;

import java.util.List;

public class EventResponse {
    private String eventId;
    private String sessionId;
    private String name;
    private String description;
    private String start;
    private String end;
    private String referrerId;
    private String localUnitId;
    private int maxParticipants;
    private int numberOfParticipants;
    private List<TimeWindowResponse> timeWindows;
    private boolean isRecurring;

    public EventResponse() {
    }

    public EventResponse(String eventId, String sessionId, String name, String description, String start, String end, String referrerId, String localUnitId, int maxParticipants, int numberOfParticipants, List<TimeWindowResponse> timeWindows, boolean isRecurring) {
        this.eventId = eventId;
        this.sessionId = sessionId;
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.referrerId = referrerId;
        this.localUnitId = localUnitId;
        this.maxParticipants = maxParticipants;
        this.numberOfParticipants = numberOfParticipants;
        this.timeWindows = timeWindows;
        this.isRecurring = isRecurring;
    }

    public static EventResponse fromEvent(Event event, EventSession eventSession) {
        return new EventResponse(
                event.getId().value(),
                eventSession.getId().value(),
                event.getName(),
                event.getDescription(),
                eventSession.getStart().toString(),
                eventSession.getEnd().toString(),
                event.getReferrerId().value(),
                event.getLocalUnitId().value(),
                eventSession.getMaxParticipants(),
                eventSession.getParticipants(),
                eventSession.getTimeWindows().stream().map(TimeWindowResponse::fromTimeWindow).toList(),
                event.getOccurrences() > 1
        );
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getReferrerId() {
        return referrerId;
    }

    public void setReferrerId(String referrerId) {
        this.referrerId = referrerId;
    }

    public String getLocalUnitId() {
        return localUnitId;
    }

    public void setLocalUnitId(String localUnitId) {
        this.localUnitId = localUnitId;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public List<TimeWindowResponse> getTimeWindows() {
        return timeWindows;
    }

    public void setTimeWindows(List<TimeWindowResponse> timeWindows) {
        this.timeWindows = timeWindows;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }
}
