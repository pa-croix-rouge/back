package fr.croixrouge.exposition.dto.core;

import fr.croixrouge.domain.model.FamilyMember;

import java.time.LocalDate;

public class FamilyMemberCreationRequest {
    public String firstName;
    public String lastName;
    public LocalDate birthDate;

    public FamilyMember toModel() {
        return new FamilyMember(null, firstName, lastName, birthDate);
    }
}
