package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.exposition.dto.CreateStorageDTO;
import fr.croixrouge.exposition.dto.StorageResponse;
import fr.croixrouge.service.LocalUnitService;
import fr.croixrouge.service.StorageService;
import fr.croixrouge.storage.model.Storage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/storage")
public class StorageController extends CRUDController<ID, Storage, StorageService, StorageResponse, CreateStorageDTO> {

    private final LocalUnitService localUnitService;

    public StorageController(StorageService service, LocalUnitService localUnitService) {
        super(service);
        this.localUnitService = localUnitService;
    }

    @Override
    public StorageResponse toDTO(Storage model) {
        return new StorageResponse(model);
    }

    @Override
    public Storage toModel(CreateStorageDTO dto) {
        LocalUnit localUnit = localUnitService.findById(new ID(dto.getLocalUnitID()));
        return dto.toModel(localUnit);
    }
}
