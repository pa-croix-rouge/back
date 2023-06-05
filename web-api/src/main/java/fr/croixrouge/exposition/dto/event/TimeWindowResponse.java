package fr.croixrouge.exposition.dto.event;

import fr.croixrouge.domain.model.ID;

import java.util.List;

public class TimeWindowResponse {
    private String timeWindowId;
    private String start;
    private String end;
    private int maxParticipants;
    private List<String> participants;

    public TimeWindowResponse() {
    }

    public TimeWindowResponse(String timeWindowId, String start, String end, int maxParticipants, List<String> participants) {
        this.timeWindowId = timeWindowId;
        this.start = start;
        this.end = end;
        this.maxParticipants = maxParticipants;
        this.participants = participants;
    }

    public static TimeWindowResponse fromTimeWindow(fr.croixrouge.model.EventTimeWindow timeWindow) {
        return new TimeWindowResponse(
                timeWindow.getId().value(),
                timeWindow.getStart().toString(),
                timeWindow.getEnd().toString(),
                timeWindow.getMaxParticipants(),
                timeWindow.getParticipants().stream().map(ID::value).toList()
        );
    }

    public String getTimeWindowId() {
        return timeWindowId;
    }

    public void setTimeWindowId(String timeWindowId) {
        this.timeWindowId = timeWindowId;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }
}
