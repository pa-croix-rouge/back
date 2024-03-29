package fr.croixrouge.exposition.dto.core;

import fr.croixrouge.domain.model.LocalUnit;

public class LocalUnitResponse {

    private final Long Id;
    private final String name;
    private final AddressDTO address;
    private final String managerName;
    private final String code;

    public LocalUnitResponse(Long id, String name, AddressDTO address, String managerName, String code) {
        this.Id = id;
        this.name = name;
        this.address = address;
        this.managerName = managerName;
        this.code = code;
    }

    public static LocalUnitResponse fromLocalUnit(LocalUnit localUnit) {
        return new LocalUnitResponse(
                localUnit.getId().value(),
                localUnit.getName(),
                new AddressDTO(localUnit.getAddress()),
                localUnit.getManagerUsername(),
                localUnit.getCode()
        );
    }

    public AddressDTO getAddress() {
        return address;
    }

    public Long getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getManagerName() {
        return managerName;
    }
}
