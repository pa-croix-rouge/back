package fr.croixrouge.storage.repository.memory;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDInMemoryRepository;
import fr.croixrouge.domain.repository.TimeStampIDGenerator;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryProductRepository extends CRUDInMemoryRepository<ID, Product> implements ProductRepository {

    public InMemoryProductRepository(List<Product> objects) {
        super(objects, new TimeStampIDGenerator());
    }

    public InMemoryProductRepository() {
        super(new ArrayList<>(), new TimeStampIDGenerator());
    }

}
