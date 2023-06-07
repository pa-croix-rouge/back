package fr.croixrouge.exposition.dto.core;

import fr.croixrouge.domain.model.Beneficiary;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.exposition.dto.CreationDTO;

import java.util.List;

public class BeneficiaryCreationRequest extends CreationDTO<Beneficiary> {
    public String username;
    public String password;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String localUnitCode;

    public BeneficiaryCreationRequest() {
    }

    public BeneficiaryCreationRequest(String username, String password, String firstName, String lastName, String phoneNumber, String localUnitCode) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.localUnitCode = localUnitCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocalUnitCode() {
        return localUnitCode;
    }

    public void setLocalUnitCode(String localUnitCode) {
        this.localUnitCode = localUnitCode;
    }

    @Override
    public Beneficiary toModel() {
        return new Beneficiary(
                null,
                new User(null, this.username, this.password, List.of()),
                this.firstName,
                this.lastName,
                this.phoneNumber,
                false,
                null
        );
    }
}
