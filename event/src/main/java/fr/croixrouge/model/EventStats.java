package fr.croixrouge.model;

import java.util.Objects;

public class EventStats {
    private final int numberOfEventsOverTheMonth;
    private final int totalParticipantsOverTheMonth;
    private final int numberOfEventsOverTheYear;
    private final int totalParticipantsOverTheYear;

    public EventStats(int numberOfEventsOverTheMonth, int totalParticipantsOverTheMonth, int numberOfEventsOverTheYear, int totalParticipantsOverTheYear) {
        this.numberOfEventsOverTheMonth = numberOfEventsOverTheMonth;
        this.totalParticipantsOverTheMonth = totalParticipantsOverTheMonth;
        this.numberOfEventsOverTheYear = numberOfEventsOverTheYear;
        this.totalParticipantsOverTheYear = totalParticipantsOverTheYear;
    }

    public int getNumberOfEventsOverTheMonth() {
        return numberOfEventsOverTheMonth;
    }

    public int getTotalParticipantsOverTheMonth() {
        return totalParticipantsOverTheMonth;
    }

    public int getNumberOfEventsOverTheYear() {
        return numberOfEventsOverTheYear;
    }

    public int getTotalParticipantsOverTheYear() {
        return totalParticipantsOverTheYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventStats that = (EventStats) o;
        return numberOfEventsOverTheMonth == that.numberOfEventsOverTheMonth && totalParticipantsOverTheMonth == that.totalParticipantsOverTheMonth && numberOfEventsOverTheYear == that.numberOfEventsOverTheYear && totalParticipantsOverTheYear == that.totalParticipantsOverTheYear;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfEventsOverTheMonth, totalParticipantsOverTheMonth, numberOfEventsOverTheYear, totalParticipantsOverTheYear);
    }

    @Override
    public String toString() {
        return "EventStats{" +
                "numberOfEventsOverTheMonth=" + numberOfEventsOverTheMonth +
                ", totalParticipantsOverTheMonth=" + totalParticipantsOverTheMonth +
                ", numberOfEventsOverTheYear=" + numberOfEventsOverTheYear +
                ", totalParticipantsOverTheYear=" + totalParticipantsOverTheYear +
                '}';
    }
}
