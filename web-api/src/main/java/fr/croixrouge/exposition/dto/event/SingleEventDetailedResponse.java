package fr.croixrouge.exposition.dto.event;

import fr.croixrouge.domain.model.ID;
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
    private List<Long> participants;

    public SingleEventDetailedResponse() {
    }

    public SingleEventDetailedResponse(Long eventId, Long sessionId, String name, String description, String start, String end, Long referrerId, Long localUnitId, int maxParticipants, List<Long> participants) {
        this.eventId = eventId;
        this.sessionId = sessionId;
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.referrerId = referrerId;
        this.localUnitId = localUnitId;
        this.maxParticipants = maxParticipants;
        this.participants = participants;
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
                eventSession.getParticipants().stream().map(ID::value).toList()
        );
    }

    public Long getEventId() {
        return eventId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public Long getReferrerId() {
        return referrerId;
    }

    public Long getLocalUnitId() {
        return localUnitId;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public List<Long> getParticipants() {
        return participants;
    }
}
