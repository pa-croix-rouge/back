package fr.croixrouge.storage.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDRepository;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.Product;

import java.util.List;
import java.util.Optional;

public interface StorageProductRepository extends CRUDRepository<ID, StorageProduct> {

    Optional<StorageProduct> findById(Storage storage, Product product);

    Optional<StorageProduct> findByProduct(Product product);

    List<StorageProduct> findAllByStorage(Storage storage);

    List<StorageProduct> findAllByLocalUnit(ID localUnitId);
}
