package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.domain.model.Volunteer;
import fr.croixrouge.exposition.dto.core.VolunteerCreationRequest;
import fr.croixrouge.exposition.dto.core.VolunteerResponse;
import fr.croixrouge.service.LocalUnitService;
import fr.croixrouge.service.UserService;
import fr.croixrouge.service.VolunteerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/volunteer")
public class VolunteerController extends CRUDController<ID, Volunteer, VolunteerService, VolunteerResponse, VolunteerCreationRequest> {

    private final LocalUnitService localUnitService;
    private final UserService userService;

    public VolunteerController(VolunteerService service, LocalUnitService localUnitService, UserService userService) {
        super(service);
        this.localUnitService = localUnitService;
        this.userService = userService;
    }

    @Override
    public VolunteerResponse toDTO(Volunteer model) {
        return new VolunteerResponse(model.getUser().getUsername(), model.getFirstName(), model.getLastName(), model.getPhoneNumber(), model.isValidated());
    }

    @Override
    @PostMapping
    public ResponseEntity<ID> post(@RequestBody VolunteerCreationRequest model) {
        LocalUnit localUnit = this.localUnitService.getLocalUnitByCode(model.getLocalUnitCode());
        if (localUnit == null) {
            return ResponseEntity.notFound().build();
        }
        User user = new User(null, model.getUsername(), model.getPassword(), List.of());
        ID userId = this.userService.save(user);
        if (userId == null) {
            return ResponseEntity.internalServerError().build();
        }
        Volunteer volunteer = new Volunteer(null, user, model.getFirstName(), model.getLastName(), model.getPhoneNumber(), false, localUnit.getId());
        ID volunteerId = service.save(volunteer);
        if (volunteerId == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(volunteerId);
    }

    @PostMapping("/validate/{id}")
    public ResponseEntity validateVolunteer(@PathVariable ID id) {
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
    public ResponseEntity invalidateVolunteer(@PathVariable ID id) {
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
