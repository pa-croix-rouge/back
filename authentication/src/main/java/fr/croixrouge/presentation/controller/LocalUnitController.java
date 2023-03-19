package fr.croixrouge.presentation.controller;

import fr.croixrouge.exception.LocalUnitNotFoundException;
import fr.croixrouge.presentation.dto.LocalUnitRequest;
import fr.croixrouge.presentation.dto.LocalUnitResponse;
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

    public LocalUnitController(LocalUnitService localUnitService) {
        this.localUnitService = localUnitService;
    }

    @GetMapping
    public ResponseEntity<LocalUnitResponse> getLocalUnitFromPostalCode(@RequestBody LocalUnitRequest localUnitRequest) {
        try {
            LocalUnitResponse localUnitResponse = LocalUnitResponse.fromLocalUnit(localUnitService.getLocalUnitByPostalCode(localUnitRequest.getPostalCode()));
            return ResponseEntity.ok(localUnitResponse);
        } catch (LocalUnitNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
