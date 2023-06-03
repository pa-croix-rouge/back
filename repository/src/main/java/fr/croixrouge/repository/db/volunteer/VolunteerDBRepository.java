package fr.croixrouge.repository.db.volunteer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VolunteerDBRepository extends CrudRepository<VolunteerDB, Long> {
    @Query("select v from VolunteerDB v where v.userDB.userID = ?1")
    Optional<VolunteerDB> findByUserDB_UserID(Long userID);

    @Query("select v from VolunteerDB v where upper(v.userDB.username) = upper(?1)")
    Optional<VolunteerDB> findByUserDB_UsernameIgnoreCase(String username);


}
