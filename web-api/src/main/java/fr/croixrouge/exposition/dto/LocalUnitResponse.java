package fr.croixrouge.exposition.dto;

import fr.croixrouge.domain.model.LocalUnit;

public class LocalUnitResponse {

    private final String Id;

    private final String name;
    private final AddressDTO address;
    private final String managerName;

    public LocalUnitResponse(String id, String name, AddressDTO address, String managerName) {
        this.Id = id;
        this.name = name;
        this.address = address;
        this.managerName = managerName;
    }

    public static LocalUnitResponse fromLocalUnit(LocalUnit localUnit) {
        return new LocalUnitResponse(
                localUnit.getLocalUnitId(),
                localUnit.getName(),
                new AddressDTO(localUnit.getAddress()),
                localUnit.getManager().getUsername()
        );
    }

    public AddressDTO getAddress() {
        return address;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return name;
    }


    public String getManagerName() {
        return managerName;
    }
}
