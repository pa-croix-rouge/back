package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.domain.model.Volunteer;
import fr.croixrouge.exposition.dto.core.VolunteerCreationRequest;
import fr.croixrouge.exposition.dto.core.VolunteerResponse;
import fr.croixrouge.exposition.error.ErrorHandler;
import fr.croixrouge.service.AuthenticationService;
import fr.croixrouge.service.LocalUnitService;
import fr.croixrouge.service.UserService;
import fr.croixrouge.service.VolunteerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/volunteer")
public class VolunteerController extends ErrorHandler {

    private final VolunteerService service;

    private final LocalUnitService localUnitService;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public VolunteerController(VolunteerService service, LocalUnitService localUnitService, UserService userService, AuthenticationService authenticationService) {
        this.service = service;
        this.localUnitService = localUnitService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    public VolunteerResponse toDTO(Volunteer model) {
        return new VolunteerResponse(model.getId().value(), model.getUser().getUsername(), model.getFirstName(), model.getLastName(), model.getPhoneNumber(), model.isValidated(), model.getUser().getLocalUnit().getId().value());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<VolunteerResponse> get(@PathVariable String id) {
        Volunteer volunteer = service.findById(new ID(id));
        return ResponseEntity.ok(this.toDTO(volunteer));
    }

    @GetMapping
    public ResponseEntity<List<VolunteerResponse>> findAll(HttpServletRequest request) {
        String username = authenticationService.getUsernameFromJwtToken(request);
        Volunteer volunteer = service.findByUsername(username);
        var test = service.findAllByLocalUnitId(volunteer.getUser().getLocalUnit().getId());
        return ResponseEntity.ok(service.findAllByLocalUnitId(volunteer.getUser().getLocalUnit().getId()).stream().map(this::toDTO).sorted(Comparator.comparing(v -> v.username)).collect(Collectors.toList()));
    }

    @GetMapping(value = "/token", produces = "application/json")
    public ResponseEntity<VolunteerResponse> get(HttpServletRequest request) {
        String username = authenticationService.getUsernameFromJwtToken(request);
        Volunteer volunteer = service.findByUsername(username);
        return ResponseEntity.ok(this.toDTO(volunteer));
    }

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ID> post(@RequestBody VolunteerCreationRequest model) {
        LocalUnit localUnit = this.localUnitService.getLocalUnitByCode(model.getLocalUnitCode());
        if (localUnit == null) {
            return ResponseEntity.notFound().build();
        }
        User user = new User(null, model.getUsername(), model.getPassword(), localUnit, List.of());
        Volunteer volunteer = new Volunteer(null, user, model.getFirstName(), model.getLastName(), model.getPhoneNumber(), false);

        ID volunteerId = service.save(volunteer);
        if (volunteerId == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(volunteerId);
    }

    //todo : only admin can update volunteer
    @PostMapping("/validate/{id}")
    public ResponseEntity<Void> validateVolunteer(@PathVariable ID id, HttpServletRequest request) {
        Volunteer volunteer = service.findById(id);
        if (volunteer == null) {
            return ResponseEntity.notFound().build();
        }
        String username = authenticationService.getUsernameFromJwtToken(request);
        LocalUnit localUnit = volunteer.getUser().getLocalUnit();
        if (!localUnit.getManagerUsername().equals(username)) {
            return ResponseEntity.status(403).build();
        }
        boolean success = service.validateVolunteerAccount(volunteer);
        if (!success) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/invalidate/{id}")
    public ResponseEntity<Void> invalidateVolunteer(@PathVariable ID id, HttpServletRequest request) {
        Volunteer volunteer = service.findById(id);
        if (volunteer == null) {
            return ResponseEntity.notFound().build();
        }
        String username = authenticationService.getUsernameFromJwtToken(request);
        LocalUnit localUnit = volunteer.getUser().getLocalUnit();
        if (!localUnit.getManagerUsername().equals(username)) {
            return ResponseEntity.status(403).build();
        }
        boolean success = service.invalidateVolunteerAccount(volunteer);
        if (!success) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id, HttpServletRequest request) {
        Volunteer volunteer = service.findById(new ID(id));
        if (volunteer == null) {
            return ResponseEntity.notFound().build();
        }
        String username = authenticationService.getUsernameFromJwtToken(request);
        LocalUnit localUnit = volunteer.getUser().getLocalUnit();
        if (!localUnit.getManagerUsername().equals(username)
                && !volunteer.getUser().getUsername().equals(username)) {
            System.out.println("username: " + username + " manager: " + localUnit.getManagerUsername() + " volunteer: " + volunteer.getUser().getUsername());
            return ResponseEntity.status(403).build();
        }
        this.service.delete(volunteer);
        return ResponseEntity.ok().build();
    }
}
