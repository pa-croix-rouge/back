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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
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

    //todo : only admin can update volunteer
    @PostMapping("/validate/{id}")
    public ResponseEntity<BeneficiaryResponse> validateBeneficiary(@PathVariable ID id, HttpServletRequest request) {
        Beneficiary beneficiary = service.findByUserId(id);
        if (beneficiary == null) {
            return ResponseEntity.notFound().build();
        }
        String username = authenticationService.getUserIdFromJwtToken(request);
        LocalUnit localUnit = beneficiary.getUser().getLocalUnit();
        if (!localUnit.getManagerUsername().equals(username)) {
            return ResponseEntity.status(403).build();
        }
        boolean success = service.validateBeneficiaryAccount(beneficiary);
        if (!success) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

    //todo : only admin can update volunteer
    @PostMapping("/invalidate/{id}")
    public ResponseEntity<BeneficiaryResponse> invalidateBeneficiary(@PathVariable ID id, HttpServletRequest request) {
        Beneficiary beneficiary = service.findByUserId(id);
        if (beneficiary == null) {
            return ResponseEntity.notFound().build();
        }
        String username = authenticationService.getUserIdFromJwtToken(request);
        LocalUnit localUnit = beneficiary.getUser().getLocalUnit();
        if (!localUnit.getManagerUsername().equals(username)) {
            return ResponseEntity.status(403).build();
        }
        boolean success = service.invalidateBeneficiaryAccount(beneficiary);
        if (!success) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
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
                model.getUser().getUsername(),
                model.getFirstName(),
                model.getLastName(),
                model.getPhoneNumber(),
                model.isValidated(),
                model.getUser().getLocalUnit().getId()
        );
    }
}
