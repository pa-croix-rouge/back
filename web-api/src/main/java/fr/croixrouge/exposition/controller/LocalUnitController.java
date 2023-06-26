package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.exposition.dto.core.*;
import fr.croixrouge.service.AuthenticationService;
import fr.croixrouge.service.BeneficiaryService;
import fr.croixrouge.service.LocalUnitService;
import fr.croixrouge.service.VolunteerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/localunit")
public class LocalUnitController extends CRUDController<ID, LocalUnit, LocalUnitService, LocalUnitResponse, LocalUnitCreationRequest> {

    private final AuthenticationService authenticationService;
    private final VolunteerService volunteerService;
    private final BeneficiaryService beneficiaryService;

    public LocalUnitController(LocalUnitService localUnitService, AuthenticationService authenticationService, VolunteerService volunteerService, BeneficiaryService beneficiaryService) {
        super(localUnitService);
        this.authenticationService = authenticationService;
        this.volunteerService = volunteerService;
        this.beneficiaryService = beneficiaryService;
    }

    @Override
    public LocalUnitResponse toDTO(LocalUnit model) {
        return new LocalUnitResponse(model.getId().value(), model.getName(), new AddressDTO(model.getAddress()), model.getManagerUsername(), model.getCode());
    }

    @GetMapping("/postcode/{code}")
    public ResponseEntity<LocalUnitResponse> getLocalUnitFromId(@PathVariable String code) {
        LocalUnit localUnit = service.getLocalUnitByPostalCode(code);
        LocalUnitResponse localUnitResponse = this.toDTO(localUnit);
        return ResponseEntity.ok(localUnitResponse);
    }

    @PostMapping("/secret")
    public ResponseEntity<String> updateSecret(HttpServletRequest request, @RequestBody LocalUnitUpdateSecretRequest localUnitUpdateSecretRequest) {
        LocalUnit localUnit = this.service.findById(ID.of(localUnitUpdateSecretRequest.getLocalUnitId()));
        if (localUnit == null) {
            return ResponseEntity.notFound().build();
        }
        String username = authenticationService.getUserIdFromJwtToken(request);
        if (!localUnit.getManagerUsername().equals(username)) {
            return ResponseEntity.badRequest().build();
        }
        String result = this.service.regenerateSecret(localUnit.getId());
        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/stats")
    public ResponseEntity<LocalUnitStatsResponse> getLocalUnitStats(HttpServletRequest request) {
        LocalUnit localUnit = this.service.findById(authenticationService.getUserLocalUnitIdFromJwtToken(request));
        if (localUnit == null) {
            return ResponseEntity.notFound().build();
        }
        int numberOfVolunteers = volunteerService.findAllByLocalUnitId(localUnit.getId()).size();
        int numberOfBeneficiaries = beneficiaryService.findAllByLocalUnitId(localUnit.getId()).size();
        LocalUnitStatsResponse localUnitStatsResponse = new LocalUnitStatsResponse(numberOfVolunteers, numberOfBeneficiaries);
        return ResponseEntity.ok(localUnitStatsResponse);
    }
}
