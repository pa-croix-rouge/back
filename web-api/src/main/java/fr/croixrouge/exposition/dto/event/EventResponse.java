package fr.croixrouge.exposition.dto.event;

import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;

import java.time.ZoneId;
import java.util.List;

public class EventResponse {
    private Long eventId;
    private Long sessionId;
    private String name;
    private String description;
    private String start;
    private String end;
    private Long referrerId;
    private Long localUnitId;
    private int maxParticipants;
    private int numberOfParticipants;
    private List<TimeWindowResponse> timeWindows;
    private boolean isRecurring;

    public EventResponse() {
    }

    public EventResponse(Long eventId, Long sessionId, String name, String description, String start, String end, Long referrerId, Long localUnitId, int maxParticipants, int numberOfParticipants, List<TimeWindowResponse> timeWindows, boolean isRecurring) {
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
                eventSession.getStart().withZoneSameInstant(ZoneId.of("Europe/Paris")).toString(),
                eventSession.getEnd().withZoneSameInstant(ZoneId.of("Europe/Paris")).toString(),
                event.getReferrer().getId().value(),
                event.getLocalUnit().getId().value(),
                eventSession.getMaxParticipants(),
                eventSession.getParticipants(),
                eventSession.getTimeWindows().stream().map(TimeWindowResponse::fromTimeWindow).toList(),
                event.getOccurrences() > 1
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
