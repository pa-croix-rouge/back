package fr.croixrouge.exposition.dto.core;

import fr.croixrouge.domain.model.FamilyMember;

import java.time.LocalDate;

public class FamilyMemberResponse {

    public Long id;
    public String firstName;
    public String lastName;
    public LocalDate birthDate;

    public FamilyMemberResponse(Long id, String firstName, String lastName, LocalDate birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public FamilyMemberResponse(FamilyMember familyMember) {
        this(familyMember.getId().value(), familyMember.getFirstName(), familyMember.getLastName(), familyMember.getBirthDate());
    }

    public Long getId() {
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
