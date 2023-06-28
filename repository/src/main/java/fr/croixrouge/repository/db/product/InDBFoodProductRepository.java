package fr.croixrouge.repository.db.product;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.FoodProduct;
import fr.croixrouge.storage.repository.FoodProductRepository;
import fr.croixrouge.storage.repository.StorageProductRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class InDBFoodProductRepository implements FoodProductRepository {

    private final FoodProductDBRepository foodProductDBRepository;

    private final InDBProductRepository inDBProductRepository;

    private final StorageProductRepository storageProductRepository;

    public InDBFoodProductRepository(FoodProductDBRepository foodProductDBRepository, InDBProductRepository inDBProductRepository, StorageProductRepository storageProductRepository) {
        this.foodProductDBRepository = foodProductDBRepository;
        this.inDBProductRepository = inDBProductRepository;
        this.storageProductRepository = storageProductRepository;
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
    public Optional<FoodProduct> findByLocalUnitIdAndId(ID localUnitId, ID id) {
        FoodProduct foodProduct = foodProductDBRepository.findById(id.value()).map(this::toFoodProduct).orElse(null);
        if (foodProduct == null) {
            return Optional.empty();
        }
        StorageProduct storageProduct = storageProductRepository.findByProduct(foodProduct.getProduct()).orElse(null);
        if (storageProduct == null) {
            return Optional.empty();
        }
        if (storageProduct.getStorage().getLocalUnit().getId().equals(localUnitId)) {
            return Optional.of(foodProduct);
        }
        return Optional.empty();
    }

    @Override
    public List<FoodProduct> findAll() {
        return StreamSupport.stream(foodProductDBRepository.findAll().spliterator(), false)
                .map(this::toFoodProduct)
                .toList();
    }

    @Override
    public List<FoodProduct> findAllByLocalUnitId(ID localUnitId) {
        List<FoodProduct> foodProducts = StreamSupport.stream(foodProductDBRepository.findAll().spliterator(), false)
                .map(this::toFoodProduct)
                .toList();

        List<StorageProduct> storageProducts = foodProducts.stream().map(FoodProduct::getProduct)
                .map(storageProductRepository::findByProduct)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(storageProduct -> storageProduct.getStorage().getLocalUnit().getId().equals(localUnitId))
                .toList();

        List<FoodProduct> localFoodProducts = new ArrayList<>();
        for (FoodProduct foodProduct : foodProducts) {
            for (StorageProduct storageProduct : storageProducts) {
                if (foodProduct.getProduct().getId().equals(storageProduct.getProduct().getId())) {
                    localFoodProducts.add(foodProduct);
                }
            }
        }
        return localFoodProducts;
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
    public Optional<FoodProduct> findByProductId(ID productId) {
        return foodProductDBRepository.findByProductId(productId.value()).map(this::toFoodProduct);
    }

    @Override
    public List<FoodProduct> findAllSoonExpiredByLocalUnitId(ID localUnitId) {
        return this.findAllByLocalUnitId(localUnitId).stream()
                .filter(foodProduct -> foodProduct.getExpirationDate().minusDays(7).isBefore(ZonedDateTime.now()))
                .toList();
    }
}
