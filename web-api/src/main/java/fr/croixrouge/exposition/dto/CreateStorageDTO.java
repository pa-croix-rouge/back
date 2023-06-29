package fr.croixrouge.exposition.dto;

import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.exposition.dto.core.AddressDTO;
import fr.croixrouge.storage.model.Storage;

public class CreateStorageDTO extends CreationDTO<Storage> {

    private final String name;

    private final Long localUnitID;

    private final AddressDTO address;

    public CreateStorageDTO(String name, Long localUnitID, AddressDTO address) {
        this.name = name;
        this.localUnitID = localUnitID;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public Long getLocalUnitID() {
        return localUnitID;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public Storage toModel(LocalUnit localUnit) {
        return new Storage(null, name, localUnit, address.toModel());
    }

    @Override
    public Storage toModel() {
        return null;
    }
}
