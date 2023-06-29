package fr.croixrouge.repository.db.beneficiary;

import fr.croixrouge.domain.model.Beneficiary;
import fr.croixrouge.domain.model.FamilyMember;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.BeneficiaryRepository;
import fr.croixrouge.repository.db.user.InDBUserRepository;
import fr.croixrouge.repository.db.user.UserDBRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class InDBBeneficiaryRepository implements BeneficiaryRepository {

    private final Logger logger = LoggerFactory.getLogger(InDBBeneficiaryRepository.class);

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
                StreamSupport.stream(familyMemberDBRepository.findByBeneficiaryDB_Id(beneficiaryDB.getId()).spliterator(), false).map(familyMemberDB -> new FamilyMember(new ID(familyMemberDB.getId()), familyMemberDB.getFirstname(), familyMemberDB.getLastname(), familyMemberDB.getBirthdate())).toList()
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
    public boolean validateBeneficiaryAccount(Beneficiary beneficiary) {
        //todo
        return false;
    }

    @Override
    public boolean invalidateBeneficiaryAccount(Beneficiary beneficiary) {
        //todo
        return false;
    }

    @Override
    public Optional<Beneficiary> findById(ID id) {
        return beneficiaryDBRepository.findById(id.value()).map(this::toBeneficiary);
    }

    @Override
    public ID save(Beneficiary object) {
        logger.info("InDBBeneficiaryRepository.save " + object.toString());
        inDBUserRepository.save(object.getUser());
        var beneficiaryDB = beneficiaryDBRepository.save(toBeneficiaryDB(object));
        logger.info("InDBBeneficiaryRepository.save " + beneficiaryDB.getId());
        object.setId(new ID(beneficiaryDB.getId()));
        object.getFamilyMembers().forEach(familyMember -> familyMemberDBRepository.save(toFamilyMemberDB(familyMember, beneficiaryDB)));
        return new ID(beneficiaryDB.getId());
    }

    @Override
    public void delete(Beneficiary object) {
        logger.info("InDBVolunteerRepository.delete " + object.toString());
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
