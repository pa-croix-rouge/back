package fr.croixrouge.repository;

import fr.croixrouge.domain.model.Beneficiary;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.BeneficiaryRepository;
import fr.croixrouge.domain.repository.InMemoryCRUDRepository;
import fr.croixrouge.domain.repository.TimeStampIDGenerator;

import java.util.List;
import java.util.Optional;

public class InMemoryBeneficiaryRepository extends InMemoryCRUDRepository<ID, Beneficiary> implements BeneficiaryRepository {

    public InMemoryBeneficiaryRepository(List<Beneficiary> objects) {
        super(objects, new TimeStampIDGenerator());
    }

    @Override
    public Optional<Beneficiary> findByUserId(ID id) {
        return this.findById(id);
    }

    @Override
    public Optional<Beneficiary> findByUsername(String username) {
        return this.objects.stream().filter(o -> o.getUser().getUsername().equals(username)).findFirst();
    }

    //todo: implement this
    @Override
    public boolean validateBeneficiaryAccount(Beneficiary beneficiary) {
        return false;
    }


    //todo: implement this
    @Override
    public boolean invalidateBeneficiaryAccount(Beneficiary beneficiary) {
        return false;
    }

    @Override
    public List<Beneficiary> findAllByLocalUnitId(ID id) {
        return this.objects.stream().filter(o -> o.getUser().getLocalUnit().getId().equals(id)).toList();
    }
}
