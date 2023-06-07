package fr.croixrouge.model;

import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.domain.model.Volunteer;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

public class Event extends Entity<ID> {
    private final String name;
    private final String description;
    private final Volunteer referrerId;
    private final LocalUnit localUnitId;
    private final List<EventSession> sessions;
    private final int occurrences;

    public Event(ID id, String name, String description, Volunteer referrerId, LocalUnit localUnitId, List<EventSession> sessions, int occurrences) {
        super(id);
        this.name = name;
        this.description = description;
        this.referrerId = referrerId;
        this.localUnitId = localUnitId;
        this.sessions = sessions;
        this.occurrences = occurrences;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Volunteer getReferrer() {
        return referrerId;
    }

    public LocalUnit getLocalUnit() {
        return localUnitId;
    }

    public ZonedDateTime getFirstStart() {
        return this.sessions.get(0).getStart();
    }

    public ZonedDateTime getLastEnd() {
        return this.sessions.get(this.sessions.size() - 1).getEnd();
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
        return occurrences == event.occurrences && Objects.equals(id, event.id) && Objects.equals(name, event.name) && Objects.equals(description, event.description) && Objects.equals(referrerId, event.referrerId) && Objects.equals(localUnitId, event.localUnitId) && Objects.equals(sessions, event.sessions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, referrerId, localUnitId, sessions, occurrences);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", referrerId='" + referrerId + '\'' +
                ", localUnitId='" + localUnitId + '\'' +
                ", sessions=" + sessions +
                ", occurrences=" + occurrences +
                '}';
    }
}
