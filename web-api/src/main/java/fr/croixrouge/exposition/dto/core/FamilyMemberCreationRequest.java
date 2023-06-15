package fr.croixrouge.exposition.dto.core;

import fr.croixrouge.domain.model.FamilyMember;

import java.time.ZonedDateTime;

public class FamilyMemberCreationRequest {
    public String firstName;
    public String lastName;
    public ZonedDateTime birthDate;

    public FamilyMember toModel() {
        return new FamilyMember(firstName, lastName, birthDate);
    }
}
