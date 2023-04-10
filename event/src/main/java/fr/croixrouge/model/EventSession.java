package fr.croixrouge.model;

import java.time.ZonedDateTime;
import java.util.List;

public class EventSession {
    private final String id;
    private final ZonedDateTime start;
    private final ZonedDateTime end;
    private final List<String> participants;

    public EventSession(String id, ZonedDateTime start, ZonedDateTime end, List<String> participants) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.participants = participants;
    }

    public String getId() {
        return id;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public List<String> getParticipants() {
        return participants;
    }
}
