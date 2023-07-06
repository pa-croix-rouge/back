package fr.croixrouge.domain.model;

import java.time.LocalDate;

public class FamilyMember {
    private final ID id;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthDate;

    public FamilyMember(ID id, String firstName, String lastName, LocalDate birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public ID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}
