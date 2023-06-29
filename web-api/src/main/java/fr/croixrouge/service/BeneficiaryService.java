package fr.croixrouge.service;

import fr.croixrouge.domain.model.Beneficiary;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.BeneficiaryRepository;
import fr.croixrouge.exposition.dto.core.BeneficiaryCreationRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeneficiaryService extends CRUDService<ID, Beneficiary, BeneficiaryRepository> {

    public BeneficiaryService(BeneficiaryRepository repository) {
        super(repository);
    }

    public Beneficiary findByUserId(ID id) {
        return this.repository.findByUserId(id).orElseThrow();
    }

    public Beneficiary findByUsername(String username) {
        return this.repository.findByUsername(username).orElseThrow();
    }

    public boolean validateBeneficiaryAccount(Beneficiary beneficiary) {
        return this.repository.validateBeneficiaryAccount(beneficiary);
    }

    public boolean invalidateBeneficiaryAccount(Beneficiary beneficiary) {
        return this.repository.invalidateBeneficiaryAccount(beneficiary);
    }

    public void updateBeneficiary(ID id, BeneficiaryCreationRequest beneficiaryCreationRequest) {
        var beneficiary = findById(id);

        var newBeneficiary = new Beneficiary(id,
                beneficiary.getUser(),
                beneficiaryCreationRequest.getFirstName() == null ? beneficiary.getFirstName() : beneficiaryCreationRequest.getFirstName(),
                beneficiaryCreationRequest.getLastName() == null ? beneficiary.getLastName() : beneficiaryCreationRequest.getLastName(),
                beneficiaryCreationRequest.getPhoneNumber() == null ? beneficiary.getPhoneNumber() : beneficiaryCreationRequest.getPhoneNumber(),
                beneficiary.isValidated(),
                beneficiaryCreationRequest.getBirthDate() == null ? beneficiary.getBirthDate() : beneficiaryCreationRequest.getBirthDate(),
                beneficiaryCreationRequest.getSocialWorkerNumber() == null ? beneficiary.getSocialWorkerNumber() : beneficiaryCreationRequest.getSocialWorkerNumber(),
                beneficiary.getFamilyMembers());

        save(newBeneficiary);
    }

    public List<Beneficiary> findAllByLocalUnitId(ID id) {
        return this.repository.findAllByLocalUnitId(id);
    }
}
