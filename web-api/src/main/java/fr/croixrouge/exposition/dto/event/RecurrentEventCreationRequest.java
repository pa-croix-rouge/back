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
    private Long referrerId;
    private Long localUnitId;
    private Timestamp firstStart;
    private Timestamp firstEnd;
    private int duration;
    private int frequency;
    private int maxParticipants;

    public RecurrentEventCreationRequest() {
    }

    public RecurrentEventCreationRequest(String name, String description, Long referrerId, Long localUnitId, Timestamp firstStart, Timestamp firstEnd, int duration, int frequency, int maxParticipants) {
        this.name = name;
        this.description = description;
        this.referrerId = referrerId;
        this.localUnitId = localUnitId;
        this.firstStart = firstStart;
        this.firstEnd = firstEnd;
        this.duration = duration;
        this.frequency = frequency;
        this.maxParticipants = maxParticipants;
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
                    maxParticipants,
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

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Timestamp getFirstStart() {
        return firstStart;
    }

    public void setFirstStart(Timestamp firstStart) {
        this.firstStart = firstStart;
    }

    public Timestamp getFirstEnd() {
        return firstEnd;
    }

    public void setFirstEnd(Timestamp firstEnd) {
        this.firstEnd = firstEnd;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }
}
