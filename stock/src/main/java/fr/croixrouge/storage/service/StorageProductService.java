package fr.croixrouge.storage.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.repository.ProductRepository;
import fr.croixrouge.storage.repository.StorageProductRepository;
import fr.croixrouge.storage.repository.StorageRepository;

public class StorageProductService {

    private final StorageRepository storageRepository;
    private final ProductRepository productRepository;

    private final StorageProductRepository storageProductRepository;

    public StorageProductService(StorageRepository storageRepository, ProductRepository productRepository, StorageProductRepository storageProductRepository) {
        this.storageRepository = storageRepository;
        this.productRepository = productRepository;
        this.storageProductRepository = storageProductRepository;
    }

    public void addProduct(ID storageID, ID productID, int quantity) {
        Product product = productRepository.findById(productID).orElseThrow();
        Storage storage = storageRepository.findById(storageID).orElseThrow();

        storageProductRepository.findById(storage, product)
                .ifPresentOrElse(storageProduct -> storageProductRepository.save(new StorageProduct(storageProduct.getId(), storage, product, storageProduct.getQuantity() + quantity)),
                        () -> storageProductRepository.save(new StorageProduct(storage, product, quantity)));
    }

    public int getProductQuantity(Storage storage, Product product) {
        return storageProductRepository.findById(storage, product)
                .map(StorageProduct::getQuantity)
                .orElse(0);
    }

    public void removeProduct(Storage storage, Product product, int quantity) {
        storageProductRepository.findById(storage, product)
                .ifPresentOrElse(storageProduct -> storageProductRepository.save(new StorageProduct(storageProduct.getId(), storage, product, storageProduct.getQuantity() - quantity)),
                        () -> storageProductRepository.save(new StorageProduct(storage, product, -quantity)));
    }
}
