package fr.croixrouge.model;

import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;

import java.time.ZonedDateTime;
import java.util.List;

public class EventSession extends Entity<ID> {
    private final List<EventTimeWindow> timeWindows;

    public EventSession(ID id, List<EventTimeWindow> timeWindows) {
        super(id);
        this.timeWindows = timeWindows;
    }

    public ID getId() {
        return id;
    }

    public ZonedDateTime getStart() {
        return this.timeWindows.get(0).getStart();
    }

    public ZonedDateTime getEnd() {
        return this.timeWindows.get(this.timeWindows.size() - 1).getEnd();
    }

    public int getMaxParticipants() {
        return this.timeWindows.stream().map(EventTimeWindow::getMaxParticipants).reduce(0, Integer::sum);
    }

    public int getParticipants() {
        return this.timeWindows.stream().map(eventTimeWindow -> eventTimeWindow.getParticipants().size()).reduce(0, Integer::sum);
    }

    public List<EventTimeWindow> getTimeWindows() {
        return timeWindows;
    }
}
