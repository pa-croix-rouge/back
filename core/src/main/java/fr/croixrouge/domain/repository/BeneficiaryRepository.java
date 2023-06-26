package fr.croixrouge.domain.repository;

import fr.croixrouge.domain.model.Beneficiary;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Volunteer;
import jdk.jfr.Registered;

import java.util.Optional;


public interface BeneficiaryRepository extends CRUDRepository<ID, Beneficiary>{

    Optional<Beneficiary> findByUserId(ID id);

    Optional<Beneficiary> findByUsername(String username);

    boolean validateBeneficiaryAccount(Beneficiary beneficiary);

    boolean invalidateBeneficiaryAccount(Beneficiary beneficiary);
}
