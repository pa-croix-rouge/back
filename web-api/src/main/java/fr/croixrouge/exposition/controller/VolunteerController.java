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
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VOLUNTEER_READ')")
    public ResponseEntity<VolunteerResponse> get(@PathVariable String id) {
        Volunteer volunteer = service.findById(new ID(id));
        return ResponseEntity.ok(this.toDTO(volunteer));
    }

    @GetMapping
    public ResponseEntity<List<VolunteerResponse>> findAll(HttpServletRequest request) {
        String username = authenticationService.getUserIdFromJwtToken(request);
        Volunteer volunteer = service.findByUsername(username);
        return ResponseEntity.ok(service.findAllByLocalUnitId(volunteer.getUser().getLocalUnit().getId()).stream().map(this::toDTO).sorted(Comparator.comparing(v -> v.username)).collect(Collectors.toList()));
    }

    @GetMapping("/token")
    public ResponseEntity<VolunteerResponse> get(HttpServletRequest request) {
        String username = authenticationService.getUserIdFromJwtToken(request);
        Volunteer volunteer = service.findByUsername(username);
        return ResponseEntity.ok(this.toDTO(volunteer));
    }

    @PostMapping("/register")
    public ResponseEntity<ID> post(@RequestBody VolunteerCreationRequest model) {
        LocalUnit localUnit = this.localUnitService.getLocalUnitByCode(model.getLocalUnitCode());
        if (localUnit == null) {
            return ResponseEntity.notFound().build();
        }
        User user = new User(null, model.getUsername(), model.getPassword(), localUnit, List.of());
        ID userId = this.userService.save(user);
        if (userId == null) {
            return ResponseEntity.internalServerError().build();
        }
        user.setId(userId);
        Volunteer volunteer = new Volunteer(null, user, model.getFirstName(), model.getLastName(), model.getPhoneNumber(), false);
        ID volunteerId = service.save(volunteer);
        if (volunteerId == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(volunteerId);
    }

    @PostMapping("/validate/{id}")
    public ResponseEntity<String> validateVolunteer(@PathVariable ID id) {
        Volunteer volunteer = service.findById(id);
        if (volunteer == null) {
            return ResponseEntity.notFound().build();
        }
        boolean success = service.validateVolunteerAccount(volunteer);
        if (!success) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/invalidate/{id}")
    public ResponseEntity<String> invalidateVolunteer(@PathVariable ID id) {
        Volunteer volunteer = service.findById(id);
        if (volunteer == null) {
            return ResponseEntity.notFound().build();
        }
        boolean success = service.invalidateVolunteerAccount(volunteer);
        if (!success) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }
}
