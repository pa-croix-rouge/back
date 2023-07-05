package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Role;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.domain.model.Volunteer;
import fr.croixrouge.domain.repository.VolunteerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

@Service
public class VolunteerService extends CRUDService<ID, Volunteer, VolunteerRepository> {

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    public VolunteerService(VolunteerRepository repository, RoleService roleService, PasswordEncoder passwordEncoder) {
        super(repository);
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ID save(Volunteer volunteer) {

        if (volunteer.getId() != null) {
            return super.save(volunteer);
        }

        var volunteerRole = roleService.getCommonRole(Role.COMMON_VOLUNTEER_ROLE_NAME);
        var newVolunteer = new Volunteer(
                null,
                new User(null,
                        volunteer.getUser().getUsername(),
                        passwordEncoder.encode(volunteer.getUser().getPassword()),
                        volunteer.getUser().getLocalUnit(),
                        Stream.concat(Stream.of(volunteerRole), volunteer.getUser().getRoles().stream()).toList()
                ),
                volunteer.getFirstName(),
                volunteer.getLastName(),
                volunteer.getPhoneNumber(),
                volunteer.isValidated()
        );

        return super.save(newVolunteer);
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
