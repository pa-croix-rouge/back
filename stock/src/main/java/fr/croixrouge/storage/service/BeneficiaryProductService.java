package fr.croixrouge.storage.service;

import fr.croixrouge.domain.model.Beneficiary;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.BeneficiaryRepository;
import fr.croixrouge.storage.model.BeneficiaryProduct;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.repository.BeneficiaryProductRepository;
import fr.croixrouge.storage.repository.FoodProductRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BeneficiaryProductService {

    protected final BeneficiaryProductRepository beneficiaryProductRepository;

    protected final StorageProductService storageProductService;

    protected final BeneficiaryRepository beneficiaryRepository;

    protected final FoodProductRepository foodProductRepository;

    public BeneficiaryProductService(BeneficiaryProductRepository beneficiaryProductRepository, StorageProductService storageProductService, BeneficiaryRepository beneficiaryRepository, FoodProductRepository foodProductRepository) {
        this.beneficiaryProductRepository = beneficiaryProductRepository;
        this.storageProductService = storageProductService;
        this.beneficiaryRepository = beneficiaryRepository;
        this.foodProductRepository = foodProductRepository;
    }

    public void addProduct(Beneficiary beneficiary, StorageProduct storageProduct, int quantity) {
        addProduct(beneficiary, storageProduct, quantity, LocalDateTime.now());
    }

    public ID addProduct(Beneficiary beneficiary, StorageProduct storageProduct, int quantity, LocalDateTime date) {
//        if (!canAddProduct(beneficiary, storageProduct.getStorage(), storageProduct.getProduct(), quantity)) {
//            throw new IllegalArgumentException("Limit reached");
//        }

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

        foodProductRepository.findByProductId(storageProduct.getProduct().getId()).ifPresent(foodProduct -> {
            beneficiaryRepository.updateSolde(beneficiary.getId(), beneficiary.getSolde() - foodProduct.getPrice() * quantity);
        });

        return id;
    }

    public boolean canAddProduct(Beneficiary beneficiary, Storage storage, Product product, int quantity) {
        if (product.getLimit() == null) return true;

        List<BeneficiaryProduct> products = new ArrayList<>(beneficiaryProductRepository.findAll(beneficiary.getId(), product.getId()));
        products.add(new BeneficiaryProduct(null, beneficiary, product, storage, LocalDateTime.now(), quantity));

        return !product.getLimit().isLimitReached(products);
    }
}
