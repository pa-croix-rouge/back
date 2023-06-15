package fr.croixrouge.repository.db.beneficiary;

import fr.croixrouge.domain.model.Beneficiary;
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

    private final UserDBRepository userDBRepository;

    private final InDBUserRepository inDBUserRepository;

    public InDBBeneficiaryRepository(BeneficiaryDBRepository beneficiaryDBRepository, UserDBRepository userDBRepository, InDBUserRepository inDBUserRepository) {
        this.beneficiaryDBRepository = beneficiaryDBRepository;
        this.userDBRepository = userDBRepository;
        this.inDBUserRepository = inDBUserRepository;
    }

    private BeneficiaryDB toBeneficiaryDB(Beneficiary beneficiary) {
        return new BeneficiaryDB(beneficiary.getId() == null ? null : beneficiary.getId().value(),
                inDBUserRepository.toUserDB(beneficiary.getUser()),
                beneficiary.getFirstName(),
                beneficiary.getLastName(),
                beneficiary.getPhoneNumber(),
                beneficiary.isValidated(),
                beneficiary.getBirthDate(),
                beneficiary.getSocialWorkerNumber()
        );
    }

    private Beneficiary toBeneficiary(BeneficiaryDB beneficiaryDB) {
        return new Beneficiary(
                new ID(beneficiaryDB.getId()),
                inDBUserRepository.toUser(beneficiaryDB.getUserDB()),
                beneficiaryDB.getFirstname(),
                beneficiaryDB.getLastname(),
                beneficiaryDB.getPhonenumber(),
                beneficiaryDB.getValidated(),
                beneficiaryDB.getBirthdate(),
                beneficiaryDB.getSocialWorkerNumber(),
                //faire les membres de la famille
                List.of()
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
}
