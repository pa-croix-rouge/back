package fr.croixrouge.exposition.dto.event;

import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;

import java.util.List;

public class SingleEventDetailedResponse {
    private Long eventId;
    private Long sessionId;
    private String name;
    private String description;
    private String start;
    private String end;
    private Long referrerId;
    private Long localUnitId;
    private int maxParticipants;
    private List<TimeWindowResponse> timeWindows;

    public SingleEventDetailedResponse() {
    }

    public SingleEventDetailedResponse(Long eventId, Long sessionId, String name, String description, String start, String end, Long referrerId, Long localUnitId, int maxParticipants, List<TimeWindowResponse> timeWindows) {
        this.eventId = eventId;
        this.sessionId = sessionId;
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.referrerId = referrerId;
        this.localUnitId = localUnitId;
        this.maxParticipants = maxParticipants;
        this.timeWindows = timeWindows;
    }

    public static SingleEventDetailedResponse fromEvent(Event event, EventSession eventSession) {
        return new SingleEventDetailedResponse(
                event.getId().value(),
                eventSession.getId().value(),
                event.getName(),
                event.getDescription(),
                eventSession.getStart().toString(),
                eventSession.getEnd().toString(),
                event.getReferrerId().value(),
                event.getLocalUnitId().value(),
                eventSession.getMaxParticipants(),
                eventSession.getTimeWindows().stream().map(TimeWindowResponse::fromTimeWindow).toList()
        );
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
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

    public Long getReferrerId() {
        return referrerId;
    }

    public void setReferrerId(Long referrerId) {
        this.referrerId = referrerId;
    }

    public Long getLocalUnitId() {
        return localUnitId;
    }

    public void setLocalUnitId(Long localUnitId) {
        this.localUnitId = localUnitId;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public List<TimeWindowResponse> getTimeWindows() {
        return timeWindows;
    }

    public void setTimeWindows(List<TimeWindowResponse> timeWindows) {
        this.timeWindows = timeWindows;
    }
}
