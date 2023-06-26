package fr.croixrouge.repository.db.beneficiary;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BeneficiaryDBRepository extends CrudRepository<BeneficiaryDB, Long> {
    @Query("select v from BeneficiaryDB v where v.userDB.userID = ?1")
    Optional<BeneficiaryDB> findByUserDB_UserID(Long userID);

    @Query("select v from BeneficiaryDB v where upper(v.userDB.username) = upper(?1)")
    Optional<BeneficiaryDB> findByUserDB_UsernameIgnoreCase(String username);

    @Query("select v from BeneficiaryDB v where v.userDB.localUnitDB.localUnitID = ?1")
    List<BeneficiaryDB> findByLocalUnitDB_Id(Long id);
}
