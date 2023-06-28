package fr.croixrouge.storage.service;

import fr.croixrouge.domain.model.Beneficiary;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.BeneficiaryProduct;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.repository.BeneficiaryProductRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BeneficiaryProductService {

    private final BeneficiaryProductRepository beneficiaryProductRepository;

    private final StorageProductService storageProductService;

    public BeneficiaryProductService(BeneficiaryProductRepository beneficiaryProductRepository, StorageProductService storageProductService) {
        this.beneficiaryProductRepository = beneficiaryProductRepository;
        this.storageProductService = storageProductService;
    }

    public void addProduct(Beneficiary beneficiary, StorageProduct storageProduct, int quantity) {
        addProduct(beneficiary, storageProduct, quantity, LocalDateTime.now());
    }

    public ID addProduct(Beneficiary beneficiary, StorageProduct storageProduct, int quantity, LocalDateTime date) {
        if (!canAddProduct(beneficiary, storageProduct.getStorage(), storageProduct.getProduct(), quantity)) {
            throw new IllegalArgumentException("Limit reached");
        }

        BeneficiaryProduct userProduct;
        var already = beneficiaryProductRepository.findByID(storageProduct.getStorage().getId(), storageProduct.getProduct().getId(), date);

        if (already.isPresent()) {
            userProduct = already.get();
            userProduct = new BeneficiaryProduct(userProduct.getId(), beneficiary, storageProduct.getProduct(), storageProduct.getStorage(), date, userProduct.quantity() + quantity);

            beneficiaryProductRepository.save(userProduct);
            storageProductService.removeProduct(storageProduct.getId(), quantity);
        } else {
            userProduct = new BeneficiaryProduct(null, beneficiary, storageProduct.getProduct(), storageProduct.getStorage(), date, quantity);
        }

        var id = beneficiaryProductRepository.save(userProduct);
        storageProductService.removeProduct(storageProduct.getId(), quantity);

        return id;
    }

    public boolean canAddProduct(Beneficiary beneficiary, Storage storage, Product product, int quantity) {
        List<BeneficiaryProduct> products = new ArrayList<>(beneficiaryProductRepository.findAll(beneficiary.getId(), storage.getId()));
        products.add(new BeneficiaryProduct(null, beneficiary, product, storage, LocalDateTime.now(), quantity));

        return !product.getLimit().isLimitReached(products);
    }
}
