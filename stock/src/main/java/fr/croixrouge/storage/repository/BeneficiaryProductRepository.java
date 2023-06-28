package fr.croixrouge.storage.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDRepository;
import fr.croixrouge.storage.model.BeneficiaryProduct;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BeneficiaryProductRepository extends CRUDRepository<ID, BeneficiaryProduct> {

    List<BeneficiaryProduct> findAll(ID userId, ID productId);

    Optional<BeneficiaryProduct> findByID(ID storageId, ID productId, LocalDateTime date);
}
