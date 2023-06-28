package fr.croixrouge.storage.repository.memory;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.InMemoryCRUDRepository;
import fr.croixrouge.domain.repository.TimeStampIDGenerator;
import fr.croixrouge.storage.model.product.FoodProduct;
import fr.croixrouge.storage.repository.FoodProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryFoodProductRepository extends InMemoryCRUDRepository<ID, FoodProduct> implements FoodProductRepository {

    public InMemoryFoodProductRepository(List<FoodProduct> objects) {
        super(objects, new TimeStampIDGenerator());
    }

    public InMemoryFoodProductRepository() {
        this(new ArrayList<FoodProduct>());
    }

    @Override
    public Optional<FoodProduct> findByProductId(ID productId) {
        return findAll().stream().filter(foodProduct -> foodProduct.getProduct().getId().equals(productId)).findFirst();
    }

    @Override
    public Optional<FoodProduct> findByLocalUnitIdAndId(ID localUnitId, ID id) {
        return Optional.empty();
    }

    @Override
    public List<FoodProduct> findAllByLocalUnitId(ID localUnitId) {
        return List.of();
    }

    @Override
    public List<FoodProduct> findAllSoonExpiredByLocalUnitId(ID localUnitId) {
        return List.of();
    }
}
