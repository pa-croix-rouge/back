package fr.croixrouge.exposition.dto.event;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;

import java.util.List;

public class SingleEventDetailedResponse {
    private String eventId;
    private String sessionId;
    private String name;
    private String description;
    private String start;
    private String end;
    private String referrerId;
    private String localUnitId;
    private int maxParticipants;
    private List<String> participants;

    public SingleEventDetailedResponse() {
    }

    public SingleEventDetailedResponse(String eventId, String sessionId, String name, String description, String start, String end, String referrerId, String localUnitId, int maxParticipants, List<String> participants) {
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

    public String getEventId() {
        return eventId;
    }

    public String getSessionId() {
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

    public String getReferrerId() {
        return referrerId;
    }

    public String getLocalUnitId() {
        return localUnitId;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public List<String> getParticipants() {
        return participants;
    }
}
