package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Volunteer;
import fr.croixrouge.domain.repository.VolunteerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VolunteerService extends CRUDService<ID, Volunteer, VolunteerRepository> {

    private final PasswordEncoder passwordEncoder;

    public VolunteerService(VolunteerRepository repository, PasswordEncoder passwordEncoder) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
    }

    public Volunteer findByUserId(ID id) {
        return this.repository.findByUserId(id).orElseThrow();
    }

    public Volunteer findByUsername(String username) {
        return this.repository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("Volunteer with username "+username+ " not found\n" + findAll().toString()));
    }

    public List<Volunteer> findAllByLocalUnitId(ID id) {
        return this.repository.findAllByLocalUnitId(id);
    }

    @Override
    public ID save(Volunteer object) {
        if (object.getUser().getId() == null || object.getId() == null) {
            return super.save( new Volunteer(
                    object.getId(),
                    object.getUser().setPassword(passwordEncoder.encode(object.getUser().getPassword())),
                    object.getFirstName(),
                    object.getLastName(),
                    object.getPhoneNumber(),
                    object.isValidated()));
        }
        return super.save(object);
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
