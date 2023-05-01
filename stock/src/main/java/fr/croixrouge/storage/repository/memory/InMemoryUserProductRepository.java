package fr.croixrouge.storage.repository.memory;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.InMemoryCRUDRepository;
import fr.croixrouge.domain.repository.TimeStampIDGenerator;
import fr.croixrouge.storage.model.UserProduct;
import fr.croixrouge.storage.repository.UserProductRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryUserProductRepository extends InMemoryCRUDRepository<ID, UserProduct> implements UserProductRepository {
    public InMemoryUserProductRepository(List<UserProduct> objects) {
        super(objects, new TimeStampIDGenerator());
    }

    public InMemoryUserProductRepository() {
        this(new ArrayList<>());
    }

    @Override
    public List<UserProduct> findAll(ID userId, ID productId) {
        return new ArrayList<>(objects.stream().filter(p -> p.product().getId().equals(productId) && p.user().getId().equals(userId)).toList());
    }
}
