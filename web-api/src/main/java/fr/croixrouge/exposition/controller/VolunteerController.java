package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Volunteer;
import fr.croixrouge.exposition.dto.core.VolunteerCreationRequest;
import fr.croixrouge.exposition.dto.core.VolunteerResponse;
import fr.croixrouge.service.VolunteerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/volunteer")
public class VolunteerController extends CRUDController<ID, Volunteer, VolunteerService, VolunteerResponse, VolunteerCreationRequest> {

    public VolunteerController(VolunteerService service) {
        super(service);
    }

    @Override
    public VolunteerResponse toDTO(Volunteer model) {
        return new VolunteerResponse(model.getUser().getUsername(), model.getFirstName(), model.getLastName(), model.getPhoneNumber(), model.isValidated());
    }
}
