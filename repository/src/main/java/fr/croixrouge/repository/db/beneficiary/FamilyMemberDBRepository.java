package fr.croixrouge.repository.db.beneficiary;

import org.springframework.data.repository.CrudRepository;

public interface FamilyMemberDBRepository extends CrudRepository<FamilyMemberDB, Long> {
    Iterable<FamilyMemberDB> findByBeneficiaryDB_Id(Long id);
}
