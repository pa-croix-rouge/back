package fr.croixrouge.storage.repository.memory;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.InMemoryCRUDRepository;
import fr.croixrouge.domain.repository.TimeStampIDGenerator;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.repository.StorageProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryStorageProductRepository extends InMemoryCRUDRepository<ID, StorageProduct> implements StorageProductRepository {
    public InMemoryStorageProductRepository(List<StorageProduct> objects) {
        super(objects, new TimeStampIDGenerator());
    }

    public InMemoryStorageProductRepository() {
        super(new ArrayList<>(), new TimeStampIDGenerator());
    }

    @Override
    public Optional<StorageProduct> findById(Storage storage, Product product) {
        return objects.stream().filter(object -> object.getStorage().equals(storage) && object.getProduct().equals(product)).findFirst();
    }

    @Override
    public Optional<StorageProduct> findByProduct(Product product) {
        return objects.stream().filter(object -> object.getProduct().equals(product)).findFirst();
    }

    @Override
    public List<StorageProduct> findAllByStorage(Storage storage) {
        return objects.stream().filter(object -> object.getStorage().equals(storage)).toList();
    }

    @Override
    public List<StorageProduct> findAllByLocalUnit(ID localUnitId) {
        return objects.stream().filter(object -> object.getStorage().getLocalUnit().getId().equals(localUnitId)).toList();
    }

    @Override
    public Optional<StorageProduct> findAllByLocalUnitAndProductId(ID localUnitId, ID productId) {
        return null;
    }

    @Override
    public Optional<StorageProduct> findByProductId(ID productId) {
        return Optional.empty();
    }
}
