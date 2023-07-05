package fr.croixrouge.repository.db.beneficiary;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FamilyMemberDBRepository extends CrudRepository<FamilyMemberDB, Long> {
    @Query("select f from FamilyMemberDB f where f.beneficiaryDB.id = ?1")
    List<FamilyMemberDB> findByBeneficiaryDB_Id(Long id);

}
