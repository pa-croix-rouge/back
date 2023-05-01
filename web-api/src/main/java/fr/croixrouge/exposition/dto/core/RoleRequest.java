package fr.croixrouge.exposition.dto.core;

public class RoleRequest {
    private String localUnitId;

    public RoleRequest() {
    }

    public RoleRequest(String localUnitId) {
        this.localUnitId = localUnitId;
    }

    public String getLocalUnitId() {
        return localUnitId;
    }

    public void setLocalUnitId(String localUnitId) {
        this.localUnitId = localUnitId;
    }
}
