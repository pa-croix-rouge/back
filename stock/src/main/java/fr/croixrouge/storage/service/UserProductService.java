package fr.croixrouge.storage.service;

import fr.croixrouge.domain.model.User;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.UserProduct;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.repository.UserProductRepository;

import java.time.LocalDateTime;
import java.util.List;

public class UserProductService {

    private final UserProductRepository userProductRepository;

    private final StorageProductService storageProductService;

    public UserProductService(UserProductRepository userProductRepository, StorageProductService storageProductService) {
        this.userProductRepository = userProductRepository;
        this.storageProductService = storageProductService;
    }

    public void addProduct(User user, Storage storage, Product product, int quantity) {
        addProduct(user, storage, product, quantity, LocalDateTime.now());
    }

    public void addProduct(User user, Storage storage, Product product, int quantity, LocalDateTime date) {
        userProductRepository.save(new UserProduct(null, user, product, storage, date, quantity));
        storageProductService.removeProduct(storage, product, quantity);
    }

    public boolean canAddProduct(User user, Storage storage, Product product, int quantity) {
        List<UserProduct> products = userProductRepository.findAll(user.getId(), storage.getId());
        products.add(new UserProduct(null, user, product, storage, LocalDateTime.now(), quantity));
        return !product.getLimit().isLimitReached(products);

    }
}
