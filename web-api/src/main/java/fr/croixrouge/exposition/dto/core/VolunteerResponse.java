package fr.croixrouge.exposition.dto.core;

public class VolunteerResponse {
    public Long id;
    public String username;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public boolean isValidated;
    public Long localUnitId;

    public VolunteerResponse(Long id, String username, String firstName, String lastName, String phoneNumber, boolean isValidated, Long localUnitId) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.isValidated = isValidated;
        this.localUnitId = localUnitId;
    }

    public Long getId() {
        return id;
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

    public boolean getIsValidated() {
        return isValidated;
    }

    public boolean isValidated() {
        return isValidated;
    }
}
