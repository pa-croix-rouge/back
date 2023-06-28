package fr.croixrouge.storage.repository.memory;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.InMemoryCRUDRepository;
import fr.croixrouge.domain.repository.TimeStampIDGenerator;
import fr.croixrouge.storage.model.BeneficiaryProduct;
import fr.croixrouge.storage.repository.BeneficiaryProductRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryBeneficiaryProductRepository extends InMemoryCRUDRepository<ID, BeneficiaryProduct> implements BeneficiaryProductRepository {
    public InMemoryBeneficiaryProductRepository(List<BeneficiaryProduct> objects) {
        super(objects, new TimeStampIDGenerator());
    }

    public InMemoryBeneficiaryProductRepository() {
        this(new ArrayList<>());
    }

    @Override
    public List<BeneficiaryProduct> findAll(ID userId, ID productId) {
        return new ArrayList<>(objects.stream().filter(p -> p.product().getId().equals(productId) && p.getBeneficiary().getId().equals(userId)).toList());
    }
}
