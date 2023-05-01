package fr.croixrouge.exposition.dto.core;

public class VolunteerResponse {
    public String username;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public boolean isValidated;

    public VolunteerResponse(String username, String firstName, String lastName, String phoneNumber, boolean isValidated) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.isValidated = isValidated;
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
}
