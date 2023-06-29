package fr.croixrouge.service;

import fr.croixrouge.storage.repository.BeneficiaryProductRepository;
import fr.croixrouge.storage.service.StorageProductService;
import org.springframework.stereotype.Service;

@Service
public class BeneficiaryProductService extends fr.croixrouge.storage.service.BeneficiaryProductService {
    public BeneficiaryProductService(BeneficiaryProductRepository beneficiaryProductRepository, StorageProductService storageProductService) {
        super(beneficiaryProductRepository, storageProductService);
    }
}
