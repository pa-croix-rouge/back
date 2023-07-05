package fr.croixrouge.repository.db.volunteer;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Volunteer;
import fr.croixrouge.domain.repository.VolunteerRepository;
import fr.croixrouge.repository.db.user.InDBUserRepository;
import fr.croixrouge.repository.db.user.UserDBRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class InDBVolunteerRepository implements VolunteerRepository {

    private final Logger logger = LoggerFactory.getLogger(InDBVolunteerRepository.class);

    private final VolunteerDBRepository volunteerDBRepository;

    private final UserDBRepository userDBRepository;

    private final InDBUserRepository inDBUserRepository;


    public InDBVolunteerRepository(VolunteerDBRepository volunteerDBRepository, UserDBRepository userDBRepository, InDBUserRepository inDBUserRepository) {
        this.volunteerDBRepository = volunteerDBRepository;
        this.userDBRepository = userDBRepository;
        this.inDBUserRepository = inDBUserRepository;
    }

    public VolunteerDB toVolunteerDB(Volunteer volunteer) {
        return new VolunteerDB(volunteer.getId() == null ? null : volunteer.getId().value(),
                inDBUserRepository.toUserDB(volunteer.getUser()),
                volunteer.getFirstName(),
                volunteer.getLastName(),
                volunteer.getPhoneNumber(),
                volunteer.isValidated()
        );
    }

    public Volunteer toVolunteer(VolunteerDB volunteerDB) {
        return new Volunteer(
                new ID(volunteerDB.getId()),
                inDBUserRepository.toUser(volunteerDB.getUserDB()),
                volunteerDB.getFirstname(),
                volunteerDB.getLastname(),
                volunteerDB.getPhonenumber(),
                volunteerDB.getValidated()
        );
    }

    @Override
    public Optional<Volunteer> findById(ID id) {
        return volunteerDBRepository.findById(id.value()).map(this::toVolunteer);
    }

    @Override
    public ID save(Volunteer object) {
        logger.info("InDBVolunteerRepository.save " + object.toString());
        var userID = inDBUserRepository.save(object.getUser());
        object.setId(userID);
        var volunteerDB = volunteerDBRepository.save(toVolunteerDB(object));
        logger.info("InDBVolunteerRepository.save " + volunteerDB.getId());
        return userID;
    }

    @Override
    public void delete(Volunteer object) {
        logger.info("InDBVolunteerRepository.delete " + object.toString());
        volunteerDBRepository.delete(toVolunteerDB(object));
        userDBRepository.delete(toVolunteerDB(object).getUserDB());
    }

    @Override
    public List<Volunteer> findAll() {
        return StreamSupport.stream(volunteerDBRepository.findAll().spliterator(), false).map(this::toVolunteer).toList();
    }

    @Override
    public Optional<Volunteer> findByUserId(ID id) {
        return volunteerDBRepository.findByUserDB_UserID(id.value()).map(this::toVolunteer);
    }

    @Override
    public Optional<Volunteer> findByUsername(String username) {
        return volunteerDBRepository.findByUserDB_UsernameIgnoreCase(username).map(this::toVolunteer);
    }

    @Override
    public List<Volunteer> findAllByLocalUnitId(ID id) {
        var test = userDBRepository.findByLocalUnitDB_LocalUnitID( id.value());
        var test3 = volunteerDBRepository.findByUserDB_UserID(id.value());
        var test2 = test.stream()
                .map(user -> volunteerDBRepository.findByUserDB_UserID(user.getUserID()).map(this::toVolunteer)).toList();

        return userDBRepository.findByLocalUnitDB_LocalUnitID( id.value())
                .stream()
                .map(user -> volunteerDBRepository.findByUserDB_UserID(user.getUserID()).map(this::toVolunteer) )
                .flatMap(Optional::stream)
                .toList();
    }

    @Override
    public boolean validateVolunteerAccount(Volunteer volunteer) {
        //todo
        return false;
    }

    @Override
    public boolean invalidateVolunteerAccount(Volunteer volunteer) {
        //todo
        return false;
    }
}
