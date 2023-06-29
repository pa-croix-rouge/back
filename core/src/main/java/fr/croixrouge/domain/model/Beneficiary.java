package fr.croixrouge.domain.model;

import java.time.LocalDate;
import java.util.List;

public class Beneficiary extends Entity<ID> {

    private final User user;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final boolean isValidated;
    private final LocalDate birthDate;
    private final String socialWorkerNumber;
    private final List<FamilyMember> familyMembers;

    public Beneficiary(ID id, User user, String firstName, String lastName, String phoneNumber, boolean isValidated, LocalDate birthDate, String socialWorkerNumber, List<FamilyMember> familyMembers) {
        super(id);
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.isValidated = isValidated;
        this.birthDate = birthDate;
        this.socialWorkerNumber = socialWorkerNumber;
        this.familyMembers = familyMembers;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getSocialWorkerNumber() {
        return socialWorkerNumber;
    }

    public List<FamilyMember> getFamilyMembers() {
        return familyMembers;
    }
}
