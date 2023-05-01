package fr.croixrouge.exposition.dto.core;

public class LocalUnitRequest {

    private String localUnitId;

    public LocalUnitRequest() {
    }

    public LocalUnitRequest(String localUnitId) {
        this.localUnitId = localUnitId;
    }

    public String getLocalUnitId() {
        return localUnitId;
    }

    public void setLocalUnitId(String localUnitId) {
        this.localUnitId = localUnitId;
    }
}
