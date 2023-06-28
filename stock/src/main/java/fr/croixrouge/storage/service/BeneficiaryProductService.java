package fr.croixrouge.storage.service;

import fr.croixrouge.domain.model.Beneficiary;
import fr.croixrouge.storage.model.BeneficiaryProduct;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.repository.BeneficiaryProductRepository;

import java.time.LocalDateTime;
import java.util.List;

public class BeneficiaryProductService {

    private final BeneficiaryProductRepository beneficiaryProductRepository;

    private final StorageProductService storageProductService;

    public BeneficiaryProductService(BeneficiaryProductRepository beneficiaryProductRepository, StorageProductService storageProductService) {
        this.beneficiaryProductRepository = beneficiaryProductRepository;
        this.storageProductService = storageProductService;
    }

    public void addProduct(Beneficiary beneficiary, Storage storage, Product product, int quantity) {
        addProduct(beneficiary, storage, product, quantity, LocalDateTime.now());
    }

    public void addProduct(Beneficiary beneficiary, Storage storage, Product product, int quantity, LocalDateTime date) {
        beneficiaryProductRepository.save(new BeneficiaryProduct(null, beneficiary, product, storage, date, quantity));
        storageProductService.removeProduct(storage, product, quantity);
    }

    public boolean canAddProduct(Beneficiary beneficiary, Storage storage, Product product, int quantity) {
        List<BeneficiaryProduct> products = beneficiaryProductRepository.findAll(beneficiary.getId(), storage.getId());
        products.add(new BeneficiaryProduct(null, beneficiary, product, storage, LocalDateTime.now(), quantity));
        return !product.getLimit().isLimitReached(products);

    }
}
