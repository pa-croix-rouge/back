package fr.croixrouge.repository.db.volunteer;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Volunteer;
import fr.croixrouge.domain.repository.VolunteerRepository;
import fr.croixrouge.repository.db.localunit.InDBLocalUnitRepository;
import fr.croixrouge.repository.db.user.InDBUserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class InDBVolunteerRepository implements VolunteerRepository {

    private final VolunteerDBRepository volunteerDBRepository;

    private final InDBUserRepository inDBUserRepository;

    private final InDBLocalUnitRepository inDBLocalUnitRepository;


    public InDBVolunteerRepository(VolunteerDBRepository volunteerDBRepository, InDBUserRepository inDBUserRepository, InDBLocalUnitRepository inDBLocalUnitRepository) {
        this.volunteerDBRepository = volunteerDBRepository;
        this.inDBUserRepository = inDBUserRepository;
        this.inDBLocalUnitRepository = inDBLocalUnitRepository;
    }

    private VolunteerDB toVolunteerDB(Volunteer volunteer) {
        return new VolunteerDB(volunteer.getId().value(),
                inDBUserRepository.toUserDB(volunteer.getUser()),
                volunteer.getFirstName(),
                volunteer.getLastName(),
                volunteer.getPhoneNumber(),
                inDBLocalUnitRepository.toLocalUnitDB(volunteer.getLocalUnit()),
                volunteer.isValidated()
        );
    }

    private Volunteer toVolunteer(VolunteerDB volunteerDB) {
        return new Volunteer(
                new ID(volunteerDB.getId()),
                inDBUserRepository.toUser(volunteerDB.getUserDB()),
                volunteerDB.getFirstname(),
                volunteerDB.getLastname(),
                volunteerDB.getPhonenumber(),
                volunteerDB.getValidated(),
                inDBLocalUnitRepository.toLocalUnit(volunteerDB.getLocalUnitDB())
        );
    }

    @Override
    public Optional<Volunteer> findById(ID id) {
        return volunteerDBRepository.findById(id.value()).map(this::toVolunteer);
    }

    @Override
    public ID save(Volunteer object) {
        return new ID(volunteerDBRepository.save(toVolunteerDB(object)).getId());
    }

    @Override
    public void delete(Volunteer object) {
        volunteerDBRepository.delete(toVolunteerDB(object));
    }

    @Override
    public List<Volunteer> findAll() {
        return StreamSupport.stream(volunteerDBRepository.findAll().spliterator(), false).map(this::toVolunteer).toList();
    }

    @Override
    public Optional<Volunteer> findByUserId(ID id) {
        return Optional.empty();
    }

    @Override
    public Optional<Volunteer> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public boolean validateVolunteerAccount(Volunteer volunteer) {
        return false;
    }

    @Override
    public boolean invalidateVolunteerAccount(Volunteer volunteer) {
        return false;
    }
}
