package fr.croixrouge.exposition.dto.event;

public class EventForLocalUnitAndMonthRequest {
    private String localUnitId;
    private int month;
    private int year;

    public EventForLocalUnitAndMonthRequest() {
    }

    public EventForLocalUnitAndMonthRequest(String localUnitId, int month, int year) {
        this.localUnitId = localUnitId;
        this.month = month;
        this.year = year;
    }

    public String getLocalUnitId() {
        return localUnitId;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}
