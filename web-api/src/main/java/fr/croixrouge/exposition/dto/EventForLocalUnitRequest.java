package fr.croixrouge.exposition.dto;

public class EventForLocalUnitRequest {
    private String localUnitId;

    public EventForLocalUnitRequest() {
    }

    public EventForLocalUnitRequest(String localUnitId) {
        this.localUnitId = localUnitId;
    }

    public String getLocalUnitId() {
        return localUnitId;
    }
}
