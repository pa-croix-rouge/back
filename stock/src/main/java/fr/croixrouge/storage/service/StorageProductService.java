package fr.croixrouge.storage.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.repository.StorageProductRepository;

import java.util.List;

public class StorageProductService {

    private final StorageProductRepository storageProductRepository;

    public StorageProductService(StorageProductRepository storageProductRepository) {
        this.storageProductRepository = storageProductRepository;
    }

    public StorageProduct findByProduct(Product product) {
        return storageProductRepository.findByProduct(product).orElse(null);
    }

    public void addProduct(Storage storage, Product product, int quantity) {
        storageProductRepository.findById(storage, product)
                .ifPresentOrElse(storageProduct -> storageProductRepository.save(new StorageProduct(storageProduct.getId(), storage, product, storageProduct.getQuantity() + quantity)),
                        () -> storageProductRepository.save(new StorageProduct(storage, product, quantity)));
    }

    public int getProductQuantity(Storage storage, Product product) {
        return storageProductRepository.findById(storage, product)
                .map(StorageProduct::getQuantity)
                .orElse(0);
    }

    public List<StorageProduct> getProductsByStorage(Storage storage) {
        return storageProductRepository.findAllByStorage(storage);
    }

    public List<StorageProduct> getProductsByLocalUnit(ID localUnitId) {
        return storageProductRepository.findAllByLocalUnit(localUnitId);
    }

    public void removeProduct(Storage storage, Product product, int quantity) {
        addProduct(storage, product, -quantity);
    }

    public void delete(StorageProduct storageProduct) {
        storageProductRepository.delete(storageProduct);
    }
}
