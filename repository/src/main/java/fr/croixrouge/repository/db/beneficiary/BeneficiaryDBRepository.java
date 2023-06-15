package fr.croixrouge.repository.db.beneficiary;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BeneficiaryDBRepository extends CrudRepository<BeneficiaryDB, Long> {
    @Query("select v from VolunteerDB v where v.userDB.userID = ?1")
    Optional<BeneficiaryDB> findByUserDB_UserID(Long userID);

    @Query("select v from VolunteerDB v where upper(v.userDB.username) = upper(?1)")
    Optional<BeneficiaryDB> findByUserDB_UsernameIgnoreCase(String username);
}
