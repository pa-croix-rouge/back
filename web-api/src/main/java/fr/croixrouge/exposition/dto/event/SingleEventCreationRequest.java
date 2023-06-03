package fr.croixrouge.exposition.dto.event;

import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.domain.model.Volunteer;
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
    private Long referrerId;
    private Long localUnitId;
    private int maxParticipants;

    public SingleEventCreationRequest() {
    }

    public SingleEventCreationRequest(String name, String description, Timestamp start, Timestamp end, Long referrerId, Long localUnitId, int maxParticipants) {
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.referrerId = referrerId;
        this.localUnitId = localUnitId;
        this.maxParticipants = maxParticipants;
    }

    public Event toEvent(Volunteer referrer, LocalUnit localUnit) {
        ZonedDateTime startDateTime = SingleEventCreationRequest.toLocalDateTime(start);
        ZonedDateTime endDateTime = SingleEventCreationRequest.toLocalDateTime(end);
        return new Event(
                null,
                name,
                description,
                referrer,
                localUnit,
                startDateTime,
                endDateTime,
                List.of(new EventSession(
                        null,
                        startDateTime,
                        endDateTime,
                        maxParticipants,
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

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
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
}
