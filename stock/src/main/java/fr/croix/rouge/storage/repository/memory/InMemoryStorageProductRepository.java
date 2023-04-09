package fr.croix.rouge.storage.repository.memory;

import fr.croix.rouge.storage.model.Storage;
import fr.croix.rouge.storage.model.StorageProduct;
import fr.croix.rouge.storage.model.product.Product;
import fr.croix.rouge.storage.repository.StorageProductRepository;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDInMemoryRepository;
import fr.croixrouge.domain.repository.TimeStampIDGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryStorageProductRepository extends CRUDInMemoryRepository<ID, StorageProduct> implements StorageProductRepository {
    public InMemoryStorageProductRepository(List<StorageProduct> objects) {
        super(objects, new TimeStampIDGenerator());
    }

    public InMemoryStorageProductRepository() {
        super (new ArrayList<>(), new TimeStampIDGenerator());
    }

    @Override
    public Optional<StorageProduct> findById(Storage storage, Product product) {
        return objects.stream().filter(object -> object.getStorage().equals(storage) && object.getProduct().equals(product)).findFirst();
    }
}
