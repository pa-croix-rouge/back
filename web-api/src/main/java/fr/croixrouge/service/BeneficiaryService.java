package fr.croixrouge.service;

import fr.croixrouge.domain.model.*;
import fr.croixrouge.domain.repository.BeneficiaryRepository;
import fr.croixrouge.exposition.dto.core.BeneficiaryCreationRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class BeneficiaryService extends CRUDService<ID, Beneficiary, BeneficiaryRepository> {

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    public BeneficiaryService(BeneficiaryRepository repository, RoleService roleService, PasswordEncoder passwordEncoder, MailService mailService) {
        super(repository);
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Override
    public ID save(Beneficiary beneficiary) {

        if (beneficiary.getId() != null) {
            return super.save(beneficiary);
        }

        var uuid = mailService.generateToken();

        var benefRole = roleService.getCommonRole(Role.COMMON_BENEFICIARY_ROLE_NAME);
        var newBeneficiary = new Beneficiary(
                null,
                new User(null,
                        beneficiary.getUser().getUsername(),
                        passwordEncoder.encode(beneficiary.getUser().getPassword()),
                        beneficiary.getUser().getLocalUnit(),
                        Stream.concat(Stream.of(benefRole), beneficiary.getUser().getRoles().stream()).toList(),
                        beneficiary.getUser().isEmailValidated(),
                        uuid
                ),
                beneficiary.getFirstName(),
                beneficiary.getLastName(),
                beneficiary.getPhoneNumber(),
                beneficiary.isValidated(),
                beneficiary.getBirthDate(),
                beneficiary.getSocialWorkerNumber(),
                beneficiary.getFamilyMembers()

        );


        try {
            mailService.sendEmailFromTemplate(newBeneficiary.getUser().getUsername(), uuid);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
        return super.save(newBeneficiary);

    }

    public ID saveWithoutEmailSend(Beneficiary beneficiary) {
        if (beneficiary.getId() != null) {
            return super.save(beneficiary);
        }

        var benefRole = roleService.getCommonRole(Role.COMMON_BENEFICIARY_ROLE_NAME);
        var newBeneficiary = new Beneficiary(
                null,
                new User(null,
                        beneficiary.getUser().getUsername(),
                        passwordEncoder.encode(beneficiary.getUser().getPassword()),
                        beneficiary.getUser().getLocalUnit(),
                        Stream.concat(Stream.of(benefRole), beneficiary.getUser().getRoles().stream()).toList(),
                        beneficiary.getUser().isEmailValidated(),
                        null
                ),
                beneficiary.getFirstName(),
                beneficiary.getLastName(),
                beneficiary.getPhoneNumber(),
                beneficiary.isValidated(),
                beneficiary.getBirthDate(),
                beneficiary.getSocialWorkerNumber(),
                beneficiary.getFamilyMembers(),
                beneficiary.getSolde());

        return super.save(newBeneficiary);
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
                beneficiary.getUser().getRoles(),
                beneficiary.getUser().isEmailValidated(),
                beneficiary.getUser().getTokenToValidateEmail()
        );

        var newBeneficiary = new Beneficiary(id,
                newUser,
                beneficiaryCreationRequest.getFirstName() == null || beneficiaryCreationRequest.getFirstName().isEmpty() ? beneficiary.getFirstName() : beneficiaryCreationRequest.getFirstName(),
                beneficiaryCreationRequest.getLastName() == null || beneficiaryCreationRequest.getLastName().isEmpty()? beneficiary.getLastName() : beneficiaryCreationRequest.getLastName(),
                beneficiaryCreationRequest.getPhoneNumber() == null || beneficiaryCreationRequest.getPhoneNumber().isEmpty() ? beneficiary.getPhoneNumber() : beneficiaryCreationRequest.getPhoneNumber(),
                beneficiary.isValidated(),
                beneficiaryCreationRequest.getBirthDate() == null ? beneficiary.getBirthDate() : beneficiaryCreationRequest.getBirthDate(),
                beneficiaryCreationRequest.getSocialWorkerNumber() == null ? beneficiary.getSocialWorkerNumber() : beneficiaryCreationRequest.getSocialWorkerNumber(),
                beneficiaryCreationRequest.getFamilyMembers() == null ? beneficiary.getFamilyMembers() : beneficiaryCreationRequest.getFamilyMembers().stream()
                        .map(familyMember -> new FamilyMember(
                                familyMember.id == null ? null : new ID(familyMember.id),
                                familyMember.firstName,
                                familyMember.lastName,
                                familyMember.birthDate))
                        .toList(),
                beneficiaryCreationRequest.solde == null ? beneficiary.getSolde() : beneficiaryCreationRequest.solde);

        save(newBeneficiary);
    }

    public List<Beneficiary> findAllByLocalUnitId(ID id) {
        return this.repository.findAllByLocalUnitId(id);
    }
}
