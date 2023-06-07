package fr.croixrouge.service;

import fr.croixrouge.domain.model.Beneficiary;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.BeneficiaryRepository;
import org.springframework.stereotype.Service;

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

}
