package fr.croixrouge.model;

import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;

import java.time.ZonedDateTime;
import java.util.List;

public class EventSession extends Entity<ID> {
    private final ZonedDateTime start;
    private final ZonedDateTime end;
    private final int maxParticipants;
    private final List<ID> participants;

    public EventSession(ID id, ZonedDateTime start, ZonedDateTime end, int maxParticipants, List<ID> participants) {
        super(id);
        this.start = start;
        this.end = end;
        this.maxParticipants = maxParticipants;
        this.participants = participants;
    }

    public ID getId() {
        return id;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public List<ID> getParticipants() {
        return participants;
    }
}
