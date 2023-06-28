package fr.croixrouge.storage.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDRepository;
import fr.croixrouge.storage.model.BeneficiaryProduct;

import java.util.List;

public interface BeneficiaryProductRepository extends CRUDRepository<ID, BeneficiaryProduct> {

    List<BeneficiaryProduct> findAll(ID userId, ID productId);
}
