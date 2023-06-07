package fr.croixrouge.exposition.dto;

import fr.croixrouge.exposition.dto.core.AddressDTO;
import fr.croixrouge.storage.model.Storage;

public class StorageResponse {

    private final Long id;

    AddressDTO address;

    Long localUnitId;

    public StorageResponse(Storage storage) {
        this.id = storage.getId().value();
        this.address = new AddressDTO(storage.getAddress());
        this.localUnitId = storage.getLocalUnit().getId().value();
    }

    public StorageResponse(Long id, AddressDTO address, Long localUnitId) {
        this.id = id;
        this.address = address;
        this.localUnitId = localUnitId;
    }

    public Long getId() {
        return id;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public Long getLocalUnitId() {
        return localUnitId;
    }
}
