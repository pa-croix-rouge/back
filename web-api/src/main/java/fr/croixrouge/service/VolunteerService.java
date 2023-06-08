package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Volunteer;
import fr.croixrouge.domain.repository.VolunteerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolunteerService extends CRUDService<ID, Volunteer, VolunteerRepository> {

    public VolunteerService(VolunteerRepository repository) {
        super(repository);
    }

    public Volunteer findByUserId(ID id) {
        return this.repository.findByUserId(id).orElseThrow();
    }

    public Volunteer findByUsername(String username) {
        return this.repository.findByUsername(username).orElseThrow();
    }

    public List<Volunteer> findAllByLocalUnitId(ID id) {
        return this.repository.findAllByLocalUnitId(id);
    }

    public boolean validateVolunteerAccount(Volunteer volunteer) {
        Volunteer updatedVolunteer = new Volunteer(volunteer.getId(),
                volunteer.getUser(),
                volunteer.getFirstName(),
                volunteer.getLastName(),
                volunteer.getPhoneNumber(),
                true);

        return this.save(updatedVolunteer) != null;
    }

    public boolean invalidateVolunteerAccount(Volunteer volunteer) {
        Volunteer updatedVolunteer = new Volunteer(volunteer.getId(),
                volunteer.getUser(),
                volunteer.getFirstName(),
                volunteer.getLastName(),
                volunteer.getPhoneNumber(),
                false);
        return this.save(updatedVolunteer) != null;
    }

    public void deleteVolunteerAccount(Volunteer volunteer) {
        this.delete(volunteer);
    }
}
