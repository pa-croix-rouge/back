package fr.croixrouge.service;

import fr.croixrouge.domain.model.Beneficiary;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.domain.repository.BeneficiaryRepository;
import fr.croixrouge.exposition.dto.core.BeneficiaryCreationRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeneficiaryService extends CRUDService<ID, Beneficiary, BeneficiaryRepository> {

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    public BeneficiaryService(BeneficiaryRepository repository, RoleService roleService, PasswordEncoder passwordEncoder) {
        super(repository);
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ID save(Beneficiary beneficiary) {

        if (beneficiary.getId() != null) {
            return super.save(beneficiary);
        }

        var volunteerRole = roleService.getCommonRole("Bénéficiaire");
        var newVolunteer = new Beneficiary(
                null,
                new User(null,
                        beneficiary.getUser().getUsername(),
                        passwordEncoder.encode(beneficiary.getUser().getPassword()),
                        beneficiary.getUser().getLocalUnit(),
                        List.of(volunteerRole)),
                beneficiary.getFirstName(),
                beneficiary.getLastName(),
                beneficiary.getPhoneNumber(),
                beneficiary.isValidated(),
                beneficiary.getBirthDate(),
                beneficiary.getSocialWorkerNumber(),
                beneficiary.getFamilyMembers()

        );

        return super.save(newVolunteer);
    }

    public Beneficiary findByUserId(ID id) {
        return this.repository.findByUserId(id).orElseThrow();
    }

    public Beneficiary findByUsername(String username) {
        return this.repository.findByUsername(username).orElseThrow();
    }

    public boolean validateBeneficiaryAccount(Beneficiary beneficiary) {
        return this.repository.setValidateBeneficiaryAccount(beneficiary.getId(), true);
    }

    public boolean invalidateBeneficiaryAccount(Beneficiary beneficiary) {
        return this.repository.setValidateBeneficiaryAccount(beneficiary.getId(), false);
    }

    public void updateBeneficiary(ID id, BeneficiaryCreationRequest beneficiaryCreationRequest) {
        var beneficiary = findById(id);

        var newUser = new User(
                beneficiary.getUser().getId(),
                beneficiaryCreationRequest.getUsername() == null ? beneficiary.getUser().getUsername() : beneficiaryCreationRequest.getUsername(),
                beneficiaryCreationRequest.getPassword() == null ? beneficiary.getUser().getPassword() : beneficiaryCreationRequest.getPassword(),
                beneficiary.getUser().getLocalUnit(),
                beneficiary.getUser().getRoles()
        );

        var newBeneficiary = new Beneficiary(id,
                newUser,
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
