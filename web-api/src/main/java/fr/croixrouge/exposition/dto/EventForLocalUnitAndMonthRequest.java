package fr.croixrouge.exposition.dto;

public class EventForLocalUnitAndMonthRequest {
    private String localUnitId;
    private int month;

    public EventForLocalUnitAndMonthRequest() {
    }

    public EventForLocalUnitAndMonthRequest(String localUnitId, int month) {
        this.localUnitId = localUnitId;
        this.month = month;
    }

    public String getLocalUnitId() {
        return localUnitId;
    }

    public int getMonth() {
        return month;
    }
}
