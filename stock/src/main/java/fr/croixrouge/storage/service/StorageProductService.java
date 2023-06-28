package fr.croixrouge.storage.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.repository.StorageProductRepository;

import java.util.List;

public class StorageProductService {

    protected final StorageProductRepository storageProductRepository;

    public StorageProductService(StorageProductRepository storageProductRepository) {
        this.storageProductRepository = storageProductRepository;
    }

    public StorageProduct findByProduct(Product product) {
        return storageProductRepository.findByProduct(product).orElse(null);
    }

    public ID save(StorageProduct storageProduct) {
        return storageProductRepository.save(storageProduct);
    }

    public void addProduct(Storage storage, Product product, int quantity) {
        storageProductRepository.findById(storage, product)
                .ifPresentOrElse(storageProduct -> storageProductRepository.save(new StorageProduct(storageProduct.getId(), storage, product, storageProduct.getQuantity() + quantity)),
                        () -> storageProductRepository.save(new StorageProduct(storage, product, quantity)));
    }

    public void addProduct(ID storageProductID, int quantity) {
        var storageProduct = storageProductRepository.findById(storageProductID).orElseThrow();
        storageProductRepository.save(new StorageProduct(storageProduct.getId(), storageProduct.getStorage(), storageProduct.getProduct(), storageProduct.getQuantity() + quantity));
    }

    public void removeProduct(ID storageProductID, int quantity) {
        addProduct(storageProductID, -quantity);
    }

    public int getProductQuantity(Storage storage, Product product) {
        return storageProductRepository.findById(storage, product)
                .map(StorageProduct::getQuantity)
                .orElse(0);
    }

    public List<StorageProduct> getProductsByStorage(Storage storage) {
        return storageProductRepository.findAllByStorage(storage);
    }

    public List<StorageProduct> getStorageProductsByLocalUnit(ID localUnitId) {
        return storageProductRepository.findAllByLocalUnit(localUnitId);
    }

    public StorageProduct getProductsByLocalUnitAndProductID(ID localUnitId, ID productId) {
        return storageProductRepository.findAllByLocalUnitAndProductId(localUnitId, productId).orElseThrow();
    }

    public void removeProduct(Storage storage, Product product, int quantity) {
        addProduct(storage, product, -quantity);
    }

    public void delete(StorageProduct storageProduct) {
        storageProductRepository.delete(storageProduct);
    }
}
