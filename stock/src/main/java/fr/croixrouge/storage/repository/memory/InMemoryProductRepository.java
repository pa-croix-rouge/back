package fr.croixrouge.storage.repository.memory;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.InMemoryCRUDRepository;
import fr.croixrouge.domain.repository.TimeStampIDGenerator;
import fr.croixrouge.storage.model.product.FoodProduct;
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
    public Optional<FoodProduct> findByIdFood(ID id) {
        return findById(id).map( product -> {
            if (product instanceof FoodProduct) {
                return (FoodProduct) product;
            }
            return null;
        });
    }
}
