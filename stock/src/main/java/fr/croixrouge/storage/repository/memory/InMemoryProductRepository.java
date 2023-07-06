package fr.croixrouge.storage.repository.memory;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.InMemoryCRUDRepository;
import fr.croixrouge.domain.repository.TimeStampIDGenerator;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryProductRepository extends InMemoryCRUDRepository<ID, Product> implements ProductRepository {

    public InMemoryProductRepository(List<Product> objects) {
        super(objects, new TimeStampIDGenerator());
    }

    public InMemoryProductRepository() {
        super(new ArrayList<>(), new TimeStampIDGenerator());
    }

    @Override
    public List<Product> findAllWithProductLimit(ID productLimitId) {
        return null;
    }

    @Override
    public Optional<Product> findByIdAndLocalUnitId(ID id, ID localUnitId) {
        return Optional.empty();
    }

    @Override
    public List<Product> findAllByLocalUnitId(ID localUnitId) {
        return null;
    }

    @Override
    public boolean isDeleted(ID id) {
        return false;
    }
}
