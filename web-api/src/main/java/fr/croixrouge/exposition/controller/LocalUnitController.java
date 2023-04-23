package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.exception.LocalUnitNotFoundException;
import fr.croixrouge.exception.UserNotFoundException;
import fr.croixrouge.exposition.dto.LocalUnitRequest;
import fr.croixrouge.exposition.dto.LocalUnitResponse;
import fr.croixrouge.service.LocalUnitService;
import fr.croixrouge.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/localunit")
public class LocalUnitController {

    private final LocalUnitService localUnitService;
    private final UserService authenticationService;

    public LocalUnitController(LocalUnitService localUnitService, UserService authenticationService) {
        this.localUnitService = localUnitService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public ResponseEntity<LocalUnitResponse> getLocalUnitFromId(@RequestBody LocalUnitRequest localUnitRequest) {
        try {
            LocalUnit localUnit = localUnitService.getLocalUnitByPostalCode(localUnitRequest.getLocalUnitId());
            LocalUnitResponse localUnitResponse = LocalUnitResponse.fromLocalUnit(localUnit);
            return ResponseEntity.ok(localUnitResponse);
        } catch (LocalUnitNotFoundException | UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
