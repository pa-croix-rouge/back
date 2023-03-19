package fr.croixrouge.presentation.dto;

public class LocalUnitRequest {

    private String postalCode;

    public LocalUnitRequest() {
    }

    public LocalUnitRequest(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
