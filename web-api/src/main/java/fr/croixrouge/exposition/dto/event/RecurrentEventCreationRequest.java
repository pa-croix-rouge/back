package fr.croixrouge.exposition.dto.event;

import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.domain.model.Volunteer;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;
import fr.croixrouge.model.EventTimeWindow;

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
    private int frequency;
    private int eventTimeWindowDuration;
    private int eventTimeWindowOccurrence;
    private int eventTimeWindowMaxParticipants;

    public RecurrentEventCreationRequest() {
    }

    public RecurrentEventCreationRequest(String name, String description, Long referrerId, Long localUnitId, Timestamp firstStart, Timestamp firstEnd, int frequency, int eventTimeWindowDuration, int eventTimeWindowOccurrence, int eventTimeWindowMaxParticipants) {
        this.name = name;
        this.description = description;
        this.referrerId = referrerId;
        this.localUnitId = localUnitId;
        this.firstStart = firstStart;
        this.firstEnd = firstEnd;
        this.frequency = frequency;
        this.eventTimeWindowDuration = eventTimeWindowDuration;
        this.eventTimeWindowOccurrence = eventTimeWindowOccurrence;
        this.eventTimeWindowMaxParticipants = eventTimeWindowMaxParticipants;
    }

    public Event toEvent(Volunteer referrer, LocalUnit localUnit) {
        ZonedDateTime startDateTime = RecurrentEventCreationRequest.toLocalDateTime(firstStart);
        ZonedDateTime endDateTime = RecurrentEventCreationRequest.toLocalDateTime(firstEnd);

        List<EventSession> eventSessions = new ArrayList<>();

        for (ZonedDateTime sessionTime = startDateTime; sessionTime.isBefore(endDateTime); sessionTime = sessionTime.plusDays(frequency)) {
            List<EventTimeWindow> timeWindows = new ArrayList<>();

            for (int i = 0; i < eventTimeWindowOccurrence; i++) {
                ZonedDateTime timeWindowStartDateTime = sessionTime.plusMinutes((long) i * eventTimeWindowDuration);
                ZonedDateTime timeWindowEndDateTime = sessionTime.plusMinutes((long) (i + 1) * eventTimeWindowDuration);
                timeWindows.add(new EventTimeWindow(
                        null,
                        timeWindowStartDateTime,
                        timeWindowEndDateTime,
                        eventTimeWindowMaxParticipants,
                        new ArrayList<>()
                ));
            }
            eventSessions.add(new EventSession(
                    null,
                    timeWindows
            ));
        }

        return new Event(
                null,
                name,
                description,
                referrer,
                localUnit,
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

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getEventTimeWindowDuration() {
        return eventTimeWindowDuration;
    }

    public void setEventTimeWindowDuration(int eventTimeWindowDuration) {
        this.eventTimeWindowDuration = eventTimeWindowDuration;
    }

    public int getEventTimeWindowOccurrence() {
        return eventTimeWindowOccurrence;
    }

    public void setEventTimeWindowOccurrence(int eventTimeWindowOccurrence) {
        this.eventTimeWindowOccurrence = eventTimeWindowOccurrence;
    }

    public int getEventTimeWindowMaxParticipants() {
        return eventTimeWindowMaxParticipants;
    }

    public void setEventTimeWindowMaxParticipants(int eventTimeWindowMaxParticipants) {
        this.eventTimeWindowMaxParticipants = eventTimeWindowMaxParticipants;
    }
}
