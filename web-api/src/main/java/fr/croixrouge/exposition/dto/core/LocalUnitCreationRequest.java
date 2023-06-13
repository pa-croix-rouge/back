package fr.croixrouge.exposition.dto.core;

import fr.croixrouge.domain.model.Address;
import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.exposition.dto.CreationDTO;

public class LocalUnitCreationRequest extends CreationDTO<LocalUnit> {

    private String name;
    private AddressDTO address;
    private String managerUsername;
    private String code;

    public LocalUnitCreationRequest() {
    }

    public LocalUnitCreationRequest(String name, AddressDTO address, String managerUsername, String code) {
        this.name = name;
        this.address = address;
        this.managerUsername = managerUsername;
        this.code = code;
    }

    @Override
    public LocalUnit toModel() {
        return new LocalUnit(
                null,
                this.name,
                this.address.toModel(),
                this.managerUsername,
                this.code
        );
    }

    public String getName() {
        return name;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public String getManagerUsername() {
        return managerUsername;
    }

    public String getCode() {
        return code;
    }
}
