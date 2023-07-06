package fr.croixrouge.exposition.dto.core;

import fr.croixrouge.domain.model.ID;

import java.time.LocalDate;
import java.util.List;

public class BeneficiaryResponse {

    public Long id;

    public String username;
    public String firstName;
    public String lastName;
    public LocalDate birthDate;
    public String phoneNumber;
    public boolean isValidated;
    public Long localUnitId;
    public Long solde;
    public List<FamilyMemberResponse> familyMembers;

    public BeneficiaryResponse(Long id, String username, String firstName, String lastName, LocalDate birthDate, String phoneNumber, boolean isValidated, ID localUnitId, List<FamilyMemberResponse> familyMembers, Long solde) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.isValidated = isValidated;
        this.localUnitId = localUnitId.value();
        this.familyMembers = familyMembers;
        this.solde = solde;
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

    public Long getLocalUnitId() {
        return localUnitId;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Long getSolde() {
        return solde;
    }

    public List<FamilyMemberResponse> getFamilyMembers() {
        return familyMembers;
    }
}
