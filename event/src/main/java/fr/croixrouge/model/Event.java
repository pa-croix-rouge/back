package fr.croixrouge.model;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

public class Event {
    private final String id;
    private final String name;
    private final String description;
    private final ZonedDateTime start;
    private final ZonedDateTime end;
    private final String referrerId;
    private final String localUnitId;
    private final List<String> participants;


    public Event(String id, String name, String description, ZonedDateTime start, ZonedDateTime end, String referrerId, String localUnitId, List<String> participants) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.referrerId = referrerId;
        this.localUnitId = localUnitId;
        this.participants = participants;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public String getReferrerId() {
        return referrerId;
    }

    public String getLocalUnitId() {
        return localUnitId;
    }

    public List<String> getParticipants() {
        return participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(name, event.name) && Objects.equals(description, event.description) && Objects.equals(start, event.start) && Objects.equals(end, event.end) && Objects.equals(referrerId, event.referrerId) && Objects.equals(localUnitId, event.localUnitId) && Objects.equals(participants, event.participants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, start, end, referrerId, localUnitId, participants);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", referrerId='" + referrerId + '\'' +
                ", localUnitId='" + localUnitId + '\'' +
                ", participants=" + participants +
                '}';
    }
}
