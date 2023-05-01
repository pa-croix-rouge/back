package fr.croixrouge.storage.repository.memory;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.InMemoryCRUDRepository;
import fr.croixrouge.domain.repository.TimeStampIDGenerator;
import fr.croixrouge.storage.model.product.FoodProduct;
import fr.croixrouge.storage.repository.FoodProductRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryFoodProductRepository extends InMemoryCRUDRepository<ID, FoodProduct> implements FoodProductRepository {

    public InMemoryFoodProductRepository(List<FoodProduct> objects) {
        super(objects, new TimeStampIDGenerator());
    }

    public InMemoryFoodProductRepository() {
        this(new ArrayList<FoodProduct>());
    }
}
