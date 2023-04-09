package fr.croixrouge.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Event {
    private final String id;
    private final String name;
    private final String description;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final String referrerId;
    private final String localUnitId;


    public Event(String id, String name, String description, LocalDateTime start, LocalDateTime end, String referrerId, String localUnitId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.referrerId = referrerId;
        this.localUnitId = localUnitId;
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

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public String getReferrerId() {
        return referrerId;
    }

    public String getLocalUnitId() {
        return localUnitId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(name, event.name) && Objects.equals(description, event.description) && Objects.equals(start, event.start) && Objects.equals(end, event.end) && Objects.equals(referrerId, event.referrerId) && Objects.equals(localUnitId, event.localUnitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, start, end, referrerId, localUnitId);
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
                '}';
    }
}
