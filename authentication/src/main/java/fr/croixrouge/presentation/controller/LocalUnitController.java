package fr.croixrouge.presentation.controller;

import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.exception.LocalUnitNotFoundException;
import fr.croixrouge.exception.UserNotFoundException;
import fr.croixrouge.presentation.dto.LocalUnitRequest;
import fr.croixrouge.presentation.dto.LocalUnitResponse;
import fr.croixrouge.service.AuthenticationService;
import fr.croixrouge.service.LocalUnitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/localunit")
public class LocalUnitController {

    private final LocalUnitService localUnitService;
    private final AuthenticationService authenticationService;

    public LocalUnitController(LocalUnitService localUnitService, AuthenticationService authenticationService) {
        this.localUnitService = localUnitService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public ResponseEntity<LocalUnitResponse> getLocalUnitFromPostalCode(@RequestBody LocalUnitRequest localUnitRequest) {
        try {
            LocalUnit localUnit = localUnitService.getLocalUnitByPostalCode(localUnitRequest.getPostalCode());
            User manager = authenticationService.getUserById(localUnit.getManagerId());
            LocalUnitResponse localUnitResponse = LocalUnitResponse.fromLocalUnit(localUnit, manager);
            return ResponseEntity.ok(localUnitResponse);
        } catch (LocalUnitNotFoundException | UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
