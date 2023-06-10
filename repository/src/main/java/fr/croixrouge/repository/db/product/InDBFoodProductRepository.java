package fr.croixrouge.repository.db.product;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.product.FoodProduct;
import fr.croixrouge.storage.repository.FoodProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class InDBFoodProductRepository implements FoodProductRepository {

    private final FoodProductDBRepository foodProductDBRepository;

    private final InDBProductRepository inDBProductRepository;

    public InDBFoodProductRepository(FoodProductDBRepository foodProductDBRepository, InDBProductRepository inDBProductRepository) {
        this.foodProductDBRepository = foodProductDBRepository;
        this.inDBProductRepository = inDBProductRepository;
    }

    public FoodProduct toFoodProduct(FoodProductDB foodProductDB) {
        return new FoodProduct(
                inDBProductRepository.findById(new ID(foodProductDB.getId())).orElseThrow(),
                foodProductDB.getFoodConservation(),
                foodProductDB.getExpirationDate(),
                foodProductDB.getOptimalConsumptionDate(),
                foodProductDB.getPrice()
        );
    }

    public FoodProductDB toFoodProductDB(FoodProduct foodProduct) {
        return new FoodProductDB(
                inDBProductRepository.toProductDB(inDBProductRepository.findById(foodProduct.getId()).orElseThrow()),
                (float)foodProduct.getPrice(),
                foodProduct.getFoodConservation(),
                foodProduct.getExpirationDate(),
                foodProduct.getOptimalConsumptionDate()
        );
    }

    @Override
    public Optional<FoodProduct> findById(ID id) {
        return foodProductDBRepository.findById(id.value()).map(this::toFoodProduct);
    }

    @Override
    public ID save(FoodProduct object) {
        var id = inDBProductRepository.save(object);
        object.setId(id);
        foodProductDBRepository.save(toFoodProductDB(object));
        return id;
    }

    @Override
    public void delete(FoodProduct object) {
        foodProductDBRepository.delete(toFoodProductDB(object));
        inDBProductRepository.delete(object);
    }

    @Override
    public List<FoodProduct> findAll() {
        return StreamSupport.stream(foodProductDBRepository.findAll().spliterator(), false)
                .map(this::toFoodProduct)
                .toList();
    }
}
