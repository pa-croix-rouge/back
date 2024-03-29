package fr.croixrouge.domain.repository;

import fr.croixrouge.domain.model.Beneficiary;
import fr.croixrouge.domain.model.ID;

import java.util.List;
import java.util.Optional;


public interface BeneficiaryRepository extends CRUDRepository<ID, Beneficiary> {

    Optional<Beneficiary> findByUserId(ID id);

    Optional<Beneficiary> findByUsername(String username);

    boolean setValidateBeneficiaryAccount(ID id, boolean valid);

    List<Beneficiary> findAllByLocalUnitId(ID id);

    void updateSolde(ID id, Double solde);
}
