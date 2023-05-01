package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Volunteer;
import fr.croixrouge.domain.repository.VolunteerRepository;
import org.springframework.stereotype.Service;

@Service
public class VolunteerService extends CRUDService<ID, Volunteer, VolunteerRepository> {

    public VolunteerService(VolunteerRepository repository) {
        super(repository);
    }

    public Volunteer findByUserId(ID id) {
        return this.repository.findByUserId(id).orElse(null);
    }
}
