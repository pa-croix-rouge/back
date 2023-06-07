package fr.croixrouge.exposition.dto;

import fr.croixrouge.exposition.dto.core.AddressDTO;
import fr.croixrouge.storage.model.Storage;

public class StorageResponse {

    private final Long id;
    private final String name;

    AddressDTO address;

    Long localUnitId;

    public StorageResponse(Storage storage) {
        this.id = storage.getId().value();
        this.name = storage.getName();
        this.address = new AddressDTO(storage.getAddress());
        this.localUnitId = storage.getLocalUnit().getId().value();
    }

    public StorageResponse(Long id, String name, AddressDTO address, Long localUnitId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.localUnitId = localUnitId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public Long getLocalUnitId() {
        return localUnitId;
    }
}
