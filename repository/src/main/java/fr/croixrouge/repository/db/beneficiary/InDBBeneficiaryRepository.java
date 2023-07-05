package fr.croixrouge.repository.db.beneficiary;

import fr.croixrouge.domain.model.Beneficiary;
import fr.croixrouge.domain.model.FamilyMember;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.BeneficiaryRepository;
import fr.croixrouge.repository.db.user.InDBUserRepository;
import fr.croixrouge.repository.db.user.UserDBRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class InDBBeneficiaryRepository implements BeneficiaryRepository {

    private final BeneficiaryDBRepository beneficiaryDBRepository;

    private final FamilyMemberDBRepository familyMemberDBRepository;

    private final UserDBRepository userDBRepository;

    private final InDBUserRepository inDBUserRepository;

    public InDBBeneficiaryRepository(BeneficiaryDBRepository beneficiaryDBRepository, FamilyMemberDBRepository familyMemberDBRepository, UserDBRepository userDBRepository, InDBUserRepository inDBUserRepository) {
        this.beneficiaryDBRepository = beneficiaryDBRepository;
        this.familyMemberDBRepository = familyMemberDBRepository;
        this.userDBRepository = userDBRepository;
        this.inDBUserRepository = inDBUserRepository;
    }

    public BeneficiaryDB toBeneficiaryDB(Beneficiary beneficiary) {
        return new BeneficiaryDB(beneficiary.getId() == null ? null : beneficiary.getId().value(),
                beneficiary.getFirstName(),
                beneficiary.getLastName(),
                beneficiary.getPhoneNumber(),
                inDBUserRepository.toUserDB(beneficiary.getUser()),
                beneficiary.isValidated(),
                beneficiary.getBirthDate(),
                beneficiary.getSocialWorkerNumber()
        );
    }

    private FamilyMemberDB toFamilyMemberDB(FamilyMember familyMember, BeneficiaryDB beneficiaryDB) {
        return new FamilyMemberDB(familyMember.getId() == null ? null : familyMember.getId().value(),
                familyMember.getFirstName(),
                familyMember.getLastName(),
                familyMember.getBirthDate(),
                beneficiaryDB
        );
    }

    public Beneficiary toBeneficiary(BeneficiaryDB beneficiaryDB) {
        return new Beneficiary(
                new ID(beneficiaryDB.getId()),
                inDBUserRepository.toUser(beneficiaryDB.getUserDB()),
                beneficiaryDB.getFirstname(),
                beneficiaryDB.getLastname(),
                beneficiaryDB.getPhonenumber(),
                beneficiaryDB.getValidated(),
                beneficiaryDB.getBirthdate(),
                beneficiaryDB.getSocialWorkerNumber(),
                StreamSupport.stream(familyMemberDBRepository.findByBeneficiaryDB_Id(beneficiaryDB.getId()).spliterator(), false)
                        .map(familyMemberDB -> new FamilyMember(new ID(familyMemberDB.getId()),
                                familyMemberDB.getFirstname(),
                                familyMemberDB.getLastname(),
                                familyMemberDB.getBirthdate()))
                        .toList()
        );
    }

    @Override
    public Optional<Beneficiary> findByUserId(ID id) {
        return beneficiaryDBRepository.findByUserDB_UserID(id.value()).map(this::toBeneficiary);
    }

    @Override
    public Optional<Beneficiary> findByUsername(String username) {
        return beneficiaryDBRepository.findByUserDB_UsernameIgnoreCase(username).map(this::toBeneficiary);
    }

    @Override
    public boolean setValidateBeneficiaryAccount(ID id, boolean valid) {
        beneficiaryDBRepository.updateValidatedById(valid, id.value());
        return false;
    }

    @Override
    public Optional<Beneficiary> findById(ID id) {
        return beneficiaryDBRepository.findById(id.value()).map(this::toBeneficiary);
    }

    @Override
    public ID save(Beneficiary object) {
        var userID = inDBUserRepository.save(object.getUser());
        object.setId(userID);
        var beneficiaryDB = beneficiaryDBRepository.save(toBeneficiaryDB(object));

        var familyMembers = familyMemberDBRepository.findByBeneficiaryDB_Id(beneficiaryDB.getId());
        for (var familyMember : familyMembers) {
            if (object.getFamilyMembers().stream().noneMatch(familyMember1 -> familyMember1.getId().value().equals(familyMember.getId()))) {
                familyMemberDBRepository.delete(familyMember);
            }
        }

        object.getFamilyMembers().forEach(familyMember -> familyMemberDBRepository.save(toFamilyMemberDB(familyMember, beneficiaryDB)));
        return userID;
    }

    @Override
    public void delete(Beneficiary object) {
        beneficiaryDBRepository.delete(toBeneficiaryDB(object));
        userDBRepository.delete(toBeneficiaryDB(object).getUserDB());
    }

    @Override
    public List<Beneficiary> findAll() {
        return StreamSupport.stream(beneficiaryDBRepository.findAll().spliterator(), false).map(this::toBeneficiary).toList();
    }

    @Override
    public List<Beneficiary> findAllByLocalUnitId(ID id) {
        return beneficiaryDBRepository.findByLocalUnitDB_Id(id.value()).stream().map(this::toBeneficiary).toList();
    }
}
