package fr.croixrouge.domain.model;

public class Beneficiary extends Entity<ID>{

    private final User user;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final boolean isValidated;
    private final ID localUnitId;

    public Beneficiary(ID id, User user, String firstName, String lastName, String phoneNumber, boolean isValidated, ID localUnitId) {
        super(id);
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.isValidated = isValidated;
        this.localUnitId = localUnitId;
    }

    public User getUser() {
        return user;
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

    public ID getLocalUnitId() {
        return localUnitId;
    }
}
