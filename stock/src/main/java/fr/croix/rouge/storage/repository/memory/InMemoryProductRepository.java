package fr.croix.rouge.storage.repository.memory;

import fr.croix.rouge.storage.model.product.Product;
import fr.croix.rouge.storage.repository.ProductRepository;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDInMemoryRepository;
import fr.croixrouge.domain.repository.TimeStampIDGenerator;

import java.util.ArrayList;
import java.util.List;

public class InMemoryProductRepository extends CRUDInMemoryRepository<ID, Product> implements ProductRepository {

    public InMemoryProductRepository(List<Product> objects) {
        super(objects, new TimeStampIDGenerator());
    }

    public InMemoryProductRepository() {
        super(new ArrayList<>(), new TimeStampIDGenerator());
    }

    @Override
    public void delete(Product product) {

    }
}
