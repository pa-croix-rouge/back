package fr.croixrouge.exposition.dto.event;

import fr.croixrouge.model.Event;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class EventCreationRequest {
    private String name;
    private String description;
    private Timestamp start;
    private Timestamp end;
    private String referrerId;
    private String localUnitId;

    public EventCreationRequest() {
    }

    public EventCreationRequest(String name, String description, Timestamp start, Timestamp end, String referrerId, String localUnitId) {
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.referrerId = referrerId;
        this.localUnitId = localUnitId;
    }

    public Event toEvent() {
        return new Event(
                null,
                name,
                description,
                EventCreationRequest.toLocalDateTime(start),
                EventCreationRequest.toLocalDateTime(end),
                referrerId,
                localUnitId,
                new ArrayList<>());
    }

    private static ZonedDateTime toLocalDateTime(Timestamp timestamp) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp.getTime()), ZoneId.of("Europe/Paris"));
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ZonedDateTime getStart() {
        return EventCreationRequest.toLocalDateTime(start);
    }

    public ZonedDateTime getEnd() {
        return EventCreationRequest.toLocalDateTime(end);
    }

    public String getReferrerId() {
        return referrerId;
    }

    public String getLocalUnitId() {
        return localUnitId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public void setReferrerId(String referrerId) {
        this.referrerId = referrerId;
    }

    public void setLocalUnitId(String localUnitId) {
        this.localUnitId = localUnitId;
    }
}
