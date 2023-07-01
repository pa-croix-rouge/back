package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.Beneficiary;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.exposition.dto.core.BeneficiaryCreationRequest;
import fr.croixrouge.exposition.dto.core.BeneficiaryResponse;
import fr.croixrouge.exposition.dto.core.FamilyMemberCreationRequest;
import fr.croixrouge.service.AuthenticationService;
import fr.croixrouge.service.BeneficiaryService;
import fr.croixrouge.service.LocalUnitService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/beneficiaries")
public class BeneficiaryController extends CRUDController<ID, Beneficiary, BeneficiaryService, BeneficiaryResponse, BeneficiaryCreationRequest> {

    private final LocalUnitService localUnitService;
    private final AuthenticationService authenticationService;

    public BeneficiaryController(BeneficiaryService service, LocalUnitService localUnitService, AuthenticationService authenticationService) {
        super(service);
        this.localUnitService = localUnitService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/token")
    public ResponseEntity<BeneficiaryResponse> get(HttpServletRequest request) {
        String username = authenticationService.getUserIdFromJwtToken(request);
        Beneficiary beneficiary = service.findByUsername(username);
        return ResponseEntity.ok(this.toDTO(beneficiary));
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateRole(@PathVariable ID id, @RequestBody BeneficiaryCreationRequest beneficiaryCreationRequest) {
        service.updateBeneficiary(id, beneficiaryCreationRequest);
        return ResponseEntity.ok().build();
    }

    //todo : only admin can update beneficiary
    @PostMapping("/validate/{id}")
    public ResponseEntity<BeneficiaryResponse> validateBeneficiary(@PathVariable ID id, HttpServletRequest request) {
        Beneficiary beneficiary = service.findById(id);
        if (beneficiary == null) {
            return ResponseEntity.notFound().build();
        }
        String username = authenticationService.getUserIdFromJwtToken(request);
        LocalUnit localUnit = beneficiary.getUser().getLocalUnit();
        if (!localUnit.getManagerUsername().equals(username)) {
            return ResponseEntity.status(403).build();
        }

        service.validateBeneficiaryAccount(beneficiary);

        return ResponseEntity.ok().build();
    }

    //todo : only admin can update beneficiary
    @PostMapping("/invalidate/{id}")
    public ResponseEntity<BeneficiaryResponse> invalidateBeneficiary(@PathVariable ID id, HttpServletRequest request) {
        Beneficiary beneficiary = service.findById(id);
        if (beneficiary == null) {
            return ResponseEntity.notFound().build();
        }
        String username = authenticationService.getUserIdFromJwtToken(request);
        LocalUnit localUnit = beneficiary.getUser().getLocalUnit();
        if (!localUnit.getManagerUsername().equals(username)) {
            return ResponseEntity.status(403).build();
        }

        service.invalidateBeneficiaryAccount(beneficiary);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BeneficiaryResponse> register(@RequestBody BeneficiaryCreationRequest creationRequest) {
        LocalUnit localUnit = this.localUnitService.getLocalUnitByCode(creationRequest.getLocalUnitCode());
        if (localUnit == null) {
            return ResponseEntity.notFound().build();
        }
        User user = new User(null, creationRequest.getUsername(), creationRequest.getPassword(), localUnit, List.of());
        Beneficiary beneficiary = new Beneficiary(null, user, creationRequest.getFirstName(), creationRequest.getLastName(), creationRequest.getPhoneNumber(), false, creationRequest.getBirthDate(), creationRequest.getSocialWorkerNumber(), creationRequest.getFamilyMembers().stream().map(FamilyMemberCreationRequest::toModel).toList());

        ID beneficiaryId = service.save(beneficiary);
        if (beneficiaryId == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(this.toDTO(beneficiary));
    }

    @Override
    public BeneficiaryResponse toDTO(Beneficiary model) {
        return new BeneficiaryResponse(
                model.getId().value(),
                model.getUser().getUsername(),
                model.getFirstName(),
                model.getLastName(),
                model.getBirthDate(),
                model.getPhoneNumber(),
                model.isValidated(),
                model.getUser().getLocalUnit().getId()
        );
    }

    @GetMapping("/localunit")
    public ResponseEntity<List<BeneficiaryResponse>> getByLocalUnit(HttpServletRequest request) {
        ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        List<Beneficiary> beneficiaries = service.findAllByLocalUnitId(localUnitId);
        return ResponseEntity.ok(beneficiaries.stream().map(this::toDTO).toList());
    }
}
