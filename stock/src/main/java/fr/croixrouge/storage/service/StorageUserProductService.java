package fr.croixrouge.storage.service;

import fr.croixrouge.domain.model.User;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.StorageUserProduct;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.repository.StorageUserProductRepository;

import java.time.LocalDate;
import java.util.List;

public class StorageUserProductService {


    private final StorageUserProductRepository storageUserProductRepository;

    private final StorageProductService storageProductService;

    public StorageUserProductService(StorageUserProductRepository storageUserProductRepository, StorageProductService storageProductService) {
        this.storageUserProductRepository = storageUserProductRepository;
        this.storageProductService = storageProductService;
    }

    public void addProduct(User user, Product product, int quantity) {
        addProduct(user, product, quantity, LocalDate.now());
    }

    public void addProduct(User user, Product product, int quantity, LocalDate date) {
        storageUserProductRepository.save(new StorageUserProduct(null, user, product, date, quantity));
    }

    public boolean canAddProduct(User user, Storage storage, Product product, int quantity) {
        List<StorageUserProduct> products = storageUserProductRepository.findAll(user.getUserId(), storage.getId());
        products.add(new StorageUserProduct(null, user, product, LocalDate.now(), quantity));
        return !product.getLimit().isLimitReached(products);

    }
}
