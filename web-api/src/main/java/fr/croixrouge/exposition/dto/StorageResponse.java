package fr.croixrouge.exposition.dto;

import fr.croixrouge.exposition.dto.core.AddressDTO;
import fr.croixrouge.exposition.dto.core.LocalUnitResponse;
import fr.croixrouge.storage.model.Storage;

public class StorageResponse {

    private final Long id;

    AddressDTO address;

    LocalUnitResponse localUnit;

    public StorageResponse(Storage storage) {
        this.id = storage.getId().value();
        this.address = new AddressDTO(storage.getAddress());
        this.localUnit = LocalUnitResponse.fromLocalUnit(storage.getLocalUnit());
    }

    public Long getId() {
        return id;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public LocalUnitResponse getLocalUnit() {
        return localUnit;
    }
}
