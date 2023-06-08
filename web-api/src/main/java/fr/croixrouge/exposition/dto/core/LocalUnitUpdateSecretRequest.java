package fr.croixrouge.exposition.dto.core;

public class LocalUnitUpdateSecretRequest {

    private Long localUnitId;

    public LocalUnitUpdateSecretRequest() {
    }

    public LocalUnitUpdateSecretRequest(Long localUnitId) {
        this.localUnitId = localUnitId;
    }

    public Long getLocalUnitId() {
        return localUnitId;
    }

    public void setLocalUnitId(Long localUnitId) {
        this.localUnitId = localUnitId;
    }
}
