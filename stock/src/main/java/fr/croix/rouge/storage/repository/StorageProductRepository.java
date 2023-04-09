package fr.croix.rouge.storage.repository;

import fr.croix.rouge.storage.model.Storage;
import fr.croix.rouge.storage.model.StorageProduct;
import fr.croix.rouge.storage.model.product.Product;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDRepository;

import java.util.Optional;

public interface StorageProductRepository extends CRUDRepository<ID, StorageProduct> {

    Optional<StorageProduct> findById(Storage storage, Product product);

}
