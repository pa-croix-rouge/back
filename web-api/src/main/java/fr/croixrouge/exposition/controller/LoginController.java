package fr.croixrouge.exposition.controller;


import fr.croixrouge.domain.model.Volunteer;
import fr.croixrouge.exposition.dto.core.LoginRequest;
import fr.croixrouge.exposition.dto.core.LoginResponse;
import fr.croixrouge.exposition.error.ErrorHandler;
import fr.croixrouge.service.AuthenticationService;
import fr.croixrouge.service.VolunteerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController extends ErrorHandler {

    private final AuthenticationService service;

    public LoginController(AuthenticationService service, VolunteerService volunteerService) {
        this.service = service;
    }

    @PostMapping("/volunteer")
    public ResponseEntity<LoginResponse> volunteerLogin(@RequestBody LoginRequest loginRequest) {
        System.out.println("volunteerLogin");
        try { // TODO Controller Exception handling
            return ResponseEntity.ok(service.authenticateVolunteer(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/beneficiary")
    public ResponseEntity<LoginResponse> beneficiaryLogin(@RequestBody LoginRequest loginRequest) {
        try { // TODO Controller Exception handling
            return ResponseEntity.ok(service.authenticateBeneficiary(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
