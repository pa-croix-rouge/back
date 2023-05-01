package fr.croixrouge.exposition.dto.event;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class SingleEventCreationRequest {
    private String name;
    private String description;
    private Timestamp start;
    private Timestamp end;
    private String referrerId;
    private String localUnitId;

    public SingleEventCreationRequest() {
    }

    public SingleEventCreationRequest(String name, String description, Timestamp start, Timestamp end, String referrerId, String localUnitId) {
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.referrerId = referrerId;
        this.localUnitId = localUnitId;
    }

    public Event toEvent() {
        ZonedDateTime startDateTime = SingleEventCreationRequest.toLocalDateTime(start);
        ZonedDateTime endDateTime = SingleEventCreationRequest.toLocalDateTime(end);
        return new Event(
                null,
                name,
                description,
                new ID(referrerId),
                new ID(localUnitId),
                startDateTime,
                endDateTime,
                List.of(new EventSession(
                        null,
                        startDateTime,
                        endDateTime,
                        new ArrayList<>()
                )),
                1);
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
        return SingleEventCreationRequest.toLocalDateTime(start);
    }

    public ZonedDateTime getEnd() {
        return SingleEventCreationRequest.toLocalDateTime(end);
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
