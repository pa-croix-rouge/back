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
                ID.of(foodProductDB.getId()),
                inDBProductRepository.findById(new ID(foodProductDB.getProductDB().getId())).orElseThrow(),
                foodProductDB.getFoodConservation(),
                foodProductDB.getExpirationDate(),
                foodProductDB.getOptimalConsumptionDate(),
                foodProductDB.getPrice()
        );
    }

    public FoodProductDB toFoodProductDB(FoodProduct foodProduct) {
        return new FoodProductDB(
                foodProduct.getId() == null ? null : foodProduct.getId().value(),
                inDBProductRepository.toProductDB(foodProduct.getProduct()),
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
        return new ID(foodProductDBRepository.save(toFoodProductDB(object)).getId());
    }

    @Override
    public void delete(FoodProduct object) {
        foodProductDBRepository.delete(toFoodProductDB(object));
    }

    @Override
    public List<FoodProduct> findAll() {
        return StreamSupport.stream(foodProductDBRepository.findAll().spliterator(), false)
                .map(this::toFoodProduct)
                .toList();
    }
}
