package fr.croixrouge.domain.model;

import java.time.ZonedDateTime;

public class FamilyMember {
    private final String firstName;
    private final String lastName;
    private final ZonedDateTime birthDate;

    public FamilyMember(String firstName, String lastName, ZonedDateTime birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public ZonedDateTime getBirthDate() {
        return birthDate;
    }
}
