package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.exposition.dto.core.AddressDTO;
import fr.croixrouge.exposition.dto.core.LocalUnitCreationRequest;
import fr.croixrouge.exposition.dto.core.LocalUnitResponse;
import fr.croixrouge.service.LocalUnitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/localunit")
public class LocalUnitController extends CRUDController<ID, LocalUnit, LocalUnitService, LocalUnitResponse, LocalUnitCreationRequest> {

    public LocalUnitController(LocalUnitService localUnitService) {
        super(localUnitService);
    }

    @Override
    public LocalUnitResponse toDTO(LocalUnit model) {
        return new LocalUnitResponse(model.getId().value(), model.getName(), new AddressDTO(model.getAddress()), model.getManager().getUsername());
    }

    @GetMapping("/postcode/{code}")
    public ResponseEntity<LocalUnitResponse> getLocalUnitFromId(@PathVariable String code) {
        LocalUnit localUnit = service.getLocalUnitByPostalCode(code);
        LocalUnitResponse localUnitResponse = this.toDTO(localUnit);
        return ResponseEntity.ok(localUnitResponse);
    }
}
