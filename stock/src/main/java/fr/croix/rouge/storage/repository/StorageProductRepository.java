package fr.croix.rouge.storage.repository;

import fr.croix.rouge.storage.model.Storage;
import fr.croix.rouge.storage.model.StorageProduct;
import fr.croix.rouge.storage.model.product.Product;

import java.util.Optional;

public interface StorageProductRepository {

    Optional<StorageProduct> findById(Storage storage, Product id);

    void save(Storage storage, StorageProduct storageProduct);

    void delete(Storage storage, StorageProduct storageProduct);
}
