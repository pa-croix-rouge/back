package fr.croixrouge.domain.model;

import java.util.Objects;

public class Volunteer extends Entity<ID> {
    private final User user;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final boolean isValidated;
    private final LocalUnit localUnit;

    public Volunteer(ID id, User user, String firstName, String lastName, String phoneNumber, boolean isValidated, LocalUnit localUnit) {
        super(id);
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.isValidated = isValidated;
        this.localUnit = localUnit;
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

    public LocalUnit getLocalUnit() {
        return localUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Volunteer volunteer = (Volunteer) o;
        return isValidated == volunteer.isValidated && Objects.equals(user, volunteer.user) && Objects.equals(firstName, volunteer.firstName) && Objects.equals(lastName, volunteer.lastName) && Objects.equals(phoneNumber, volunteer.phoneNumber) && Objects.equals(localUnit, volunteer.localUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, firstName, lastName, phoneNumber, isValidated, localUnit);
    }

    @Override
    public String
    toString() {
        return "Volunteer{" +
                "user=" + user +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isValidated=" + isValidated +
                ", localUnitId=" + localUnit.getId() +
                '}';
    }
}
