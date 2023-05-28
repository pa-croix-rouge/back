package fr.croixrouge.exposition.dto.event;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;
import fr.croixrouge.model.EventTimeWindow;

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
    private Long referrerId;
    private Long localUnitId;
    private int eventTimeWindowDuration;
    private int eventTimeWindowOccurrence;
    private int eventTimeWindowMaxParticipants;

    public SingleEventCreationRequest() {
    }

    public SingleEventCreationRequest(String name, String description, Timestamp start, Long referrerId, Long localUnitId, int eventTimeWindowDuration, int eventTimeWindowOccurrence, int eventTimeWindowMaxParticipants) {
        this.name = name;
        this.description = description;
        this.start = start;
        this.referrerId = referrerId;
        this.localUnitId = localUnitId;
        this.eventTimeWindowDuration = eventTimeWindowDuration;
        this.eventTimeWindowOccurrence = eventTimeWindowOccurrence;
        this.eventTimeWindowMaxParticipants = eventTimeWindowMaxParticipants;
    }

    public Event toEvent() {
        List<EventTimeWindow> timeWindows = new ArrayList<>();
        for (int i = 0; i < eventTimeWindowOccurrence; i++) {
            ZonedDateTime startDateTime = SingleEventCreationRequest.toLocalDateTime(start).plusMinutes((long) i * eventTimeWindowDuration);
            ZonedDateTime endDateTime = SingleEventCreationRequest.toLocalDateTime(start).plusMinutes((long) (i + 1) * eventTimeWindowDuration);
            timeWindows.add(new EventTimeWindow(
                    null,
                    startDateTime,
                    endDateTime,
                    eventTimeWindowMaxParticipants,
                    new ArrayList<>()
            ));
        }
        return new Event(
                null,
                name,
                description,
                new ID(referrerId),
                new ID(localUnitId),
                List.of(new EventSession(
                        null,
                        timeWindows
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
