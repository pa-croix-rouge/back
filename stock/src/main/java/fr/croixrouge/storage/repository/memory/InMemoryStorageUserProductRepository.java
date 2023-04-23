package fr.croixrouge.storage.repository.memory;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDInMemoryRepository;
import fr.croixrouge.domain.repository.TimeStampIDGenerator;
import fr.croixrouge.storage.model.StorageUserProduct;
import fr.croixrouge.storage.repository.StorageUserProductRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryStorageUserProductRepository extends CRUDInMemoryRepository<ID, StorageUserProduct> implements StorageUserProductRepository {
    public InMemoryStorageUserProductRepository(List<StorageUserProduct> objects) {
        super(objects, new TimeStampIDGenerator());
    }

    public InMemoryStorageUserProductRepository() {
        this(new ArrayList<>());
    }

    @Override
    public List<StorageUserProduct> findAll(String userId, ID productId) {
        return new ArrayList<>(objects.stream().filter(p -> p.product().getId().equals(productId) && p.user().getUserId().equals(userId)).toList());
    }
}
