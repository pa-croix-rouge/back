package fr.croixrouge.exposition.dto.core;

public class BeneficiaryResponse {
    public String username;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public boolean isValidated;
    public String localUnitId;

    public BeneficiaryResponse(String username, String firstName, String lastName, String phoneNumber, boolean isValidated, String localUnitId) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.isValidated = isValidated;
        this.localUnitId = localUnitId;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public String getLocalUnitId() {
        return localUnitId;
    }
}
