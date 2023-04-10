package fr.croixrouge.model;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

public class Event {
    private final String id;
    private final String name;
    private final String description;
    private final String referrerId;
    private final String localUnitId;
    private final ZonedDateTime firstStart;
    private final ZonedDateTime lastEnd;
    private final List<EventSession> sessions;
    private final int occurrences;

    public Event(String id, String name, String description, String referrerId, String localUnitId, ZonedDateTime firstStart, ZonedDateTime lastEnd, List<EventSession> sessions, int occurrences) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.referrerId = referrerId;
        this.localUnitId = localUnitId;
        this.firstStart = firstStart;
        this.lastEnd = lastEnd;
        this.sessions = sessions;
        this.occurrences = occurrences;
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

    public String getReferrerId() {
        return referrerId;
    }

    public String getLocalUnitId() {
        return localUnitId;
    }

    public ZonedDateTime getFirstStart() {
        return firstStart;
    }

    public ZonedDateTime getLastEnd() {
        return lastEnd;
    }

    public List<EventSession> getSessions() {
        return sessions;
    }

    public int getOccurrences() {
        return occurrences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return occurrences == event.occurrences && Objects.equals(id, event.id) && Objects.equals(name, event.name) && Objects.equals(description, event.description) && Objects.equals(referrerId, event.referrerId) && Objects.equals(localUnitId, event.localUnitId) && Objects.equals(firstStart, event.firstStart) && Objects.equals(lastEnd, event.lastEnd) && Objects.equals(sessions, event.sessions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, referrerId, localUnitId, firstStart, lastEnd, sessions, occurrences);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", referrerId='" + referrerId + '\'' +
                ", localUnitId='" + localUnitId + '\'' +
                ", firstStart=" + firstStart +
                ", lastEnd=" + lastEnd +
                ", sessions=" + sessions +
                ", occurrences=" + occurrences +
                '}';
    }
}
