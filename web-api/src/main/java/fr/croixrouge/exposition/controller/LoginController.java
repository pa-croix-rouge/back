package fr.croixrouge.exposition.controller;


import fr.croixrouge.exposition.dto.ErrorDTO;
import fr.croixrouge.exposition.dto.core.LoginRequest;
import fr.croixrouge.exposition.dto.core.LoginResponse;
import fr.croixrouge.exposition.error.EmailNotConfirmError;
import fr.croixrouge.exposition.error.ErrorHandler;
import fr.croixrouge.exposition.error.UserNotValidatedByUL;
import fr.croixrouge.service.AuthenticationService;
import fr.croixrouge.service.VolunteerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController extends ErrorHandler {

    private final AuthenticationService service;

    public LoginController(AuthenticationService service, VolunteerService volunteerService) {
        this.service = service;
    }

    @PostMapping(value = "/volunteer", consumes = "application/json", produces = "application/json")
    public ResponseEntity<LoginResponse> volunteerLogin(@RequestBody LoginRequest loginRequest) {
        try { // TODO Controller Exception handling
            return ResponseEntity.ok(service.authenticateVolunteer(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (EmailNotConfirmError e) {
            return ResponseEntity.status(490).build();
        } catch (UserNotValidatedByUL e) {
            return ResponseEntity.status(491).build();
        }
    }

    @PostMapping(value = "/beneficiary", consumes = "application/json", produces = "application/json")
    public ResponseEntity<LoginResponse> beneficiaryLogin(@RequestBody LoginRequest loginRequest) {
        try { // TODO Controller Exception handling
            return ResponseEntity.ok(service.authenticateBeneficiary(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (EmailNotConfirmError e) {
            return ResponseEntity.status(490).build();
        } catch (UserNotValidatedByUL e) {
            return ResponseEntity.status(491).build();
        }
    }

    @GetMapping(value = "/token", produces = "application/json")
    public ResponseEntity<Void> isLogin() {
        return ResponseEntity.ok().build();
    }

}
