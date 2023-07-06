package fr.croixrouge.exposition.controller;

import fr.croixrouge.service.ValidateAccountService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/create-account")
public class ValidateAccountController {
    private final ValidateAccountService validateAccountService;

    public ValidateAccountController(ValidateAccountService validateAccountService) {
        this.validateAccountService = validateAccountService;
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> validateEmail(HttpServletRequest request) {
        if(validateAccountService.validateEmail(request.getParameter("token"))) {
            return ResponseEntity.ok("Account validated");
        }
        return ResponseEntity.badRequest().body("Invalid token");
    }
}
