package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.exposition.dto.CreateStorageDTO;
import fr.croixrouge.exposition.dto.StorageResponse;
import fr.croixrouge.service.AuthenticationService;
import fr.croixrouge.service.LocalUnitService;
import fr.croixrouge.service.StorageService;
import fr.croixrouge.storage.model.Storage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/storage")
public class StorageController extends CRUDController<ID, Storage, StorageService, StorageResponse, CreateStorageDTO> {

    private final LocalUnitService localUnitService;

    private final AuthenticationService authenticationService;

    public StorageController(StorageService service, LocalUnitService localUnitService, AuthenticationService authenticationService) {
        super(service);
        this.localUnitService = localUnitService;
        this.authenticationService = authenticationService;
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

    @Override
    public ResponseEntity<List<StorageResponse>> findAll(HttpServletRequest request) {
        final ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        if (localUnitId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.findAllByLocalUnitId(localUnitId).stream().map(this::toDTO).toList());
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> updateStorage(HttpServletRequest request, @PathVariable ID id, @RequestBody CreateStorageDTO createStorageDTO) {
        final ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        if (localUnitId == null) {
            return ResponseEntity.notFound().build();
        }
        Storage storage = service.findByLocalUnitIdAndId(localUnitId, id);
        if (storage == null) {
            return ResponseEntity.notFound().build();
        }
        service.update(new Storage(storage.getId(), createStorageDTO.getName(), storage.getLocalUnit(), createStorageDTO.getAddress().toModel()));
        return ResponseEntity.ok().build();
    }
}
