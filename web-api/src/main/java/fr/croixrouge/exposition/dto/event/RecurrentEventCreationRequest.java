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

public class RecurrentEventCreationRequest {
    private String name;
    private String description;
    private String referrerId;
    private String localUnitId;
    private Timestamp firstStart;
    private Timestamp firstEnd;
    private int duration;
    private int frequency;

    public RecurrentEventCreationRequest() {
    }

    public RecurrentEventCreationRequest(String name, String description, String referrerId, String localUnitId, Timestamp firstStart, Timestamp firstEnd, int duration, int frequency) {
        this.name = name;
        this.description = description;
        this.referrerId = referrerId;
        this.localUnitId = localUnitId;
        this.firstStart = firstStart;
        this.firstEnd = firstEnd;
        this.duration = duration;
        this.frequency = frequency;
    }

    public Event toEvent() {
        ZonedDateTime startDateTime = RecurrentEventCreationRequest.toLocalDateTime(firstStart);
        ZonedDateTime endDateTime = RecurrentEventCreationRequest.toLocalDateTime(firstEnd);

        List<EventSession> eventSessions = new ArrayList<>();
        for (ZonedDateTime sessionTime = startDateTime; sessionTime.isBefore(endDateTime); sessionTime = sessionTime.plusDays(frequency)) {
            eventSessions.add(new EventSession(
                    null,
                    sessionTime,
                    sessionTime.plusMinutes(this.duration),
                    new ArrayList<>()
            ));
        }

        return new Event(
                null,
                name,
                description,
                new ID(referrerId),
                new ID(localUnitId),
                startDateTime,
                endDateTime,
                eventSessions,
                eventSessions.size());
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

    public String getReferrerId() {
        return referrerId;
    }

    public String getLocalUnitId() {
        return localUnitId;
    }

    public Timestamp getFirstStart() {
        return firstStart;
    }

    public Timestamp getFirstEnd() {
        return firstEnd;
    }

    public int getDuration() {
        return duration;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReferrerId(String referrerId) {
        this.referrerId = referrerId;
    }

    public void setLocalUnitId(String localUnitId) {
        this.localUnitId = localUnitId;
    }

    public void setFirstStart(Timestamp firstStart) {
        this.firstStart = firstStart;
    }

    public void setFirstEnd(Timestamp firstEnd) {
        this.firstEnd = firstEnd;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
