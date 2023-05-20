package fr.croixrouge.exposition.dto;

import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.exposition.dto.core.AddressDTO;
import fr.croixrouge.storage.model.Storage;

public class CreateStorageDTO extends CreationDTO<Storage> {

    private final String localUnitID;

    private final AddressDTO address;

    public CreateStorageDTO(String localUnitID, AddressDTO address) {
        this.localUnitID = localUnitID;
        this.address = address;
    }

    public String getLocalUnitID() {
        return localUnitID;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public Storage toModel(LocalUnit localUnit) {
        return new Storage(null, localUnit, address.toModel());
    }

    @Override
    public Storage toModel() {
        return null;
    }
}
