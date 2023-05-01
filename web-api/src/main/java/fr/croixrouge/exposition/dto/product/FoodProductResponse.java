package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.storage.model.product.FoodConservation;
import fr.croixrouge.storage.model.product.FoodProduct;

import java.time.LocalDateTime;

public class FoodProductResponse extends ProductResponse {

    private final FoodConservation foodConservation;

    private final LocalDateTime expirationDate;

    private final LocalDateTime optimalConsumptionDate;

    public FoodProductResponse(FoodProduct product) {
        super(product);
        this.foodConservation = product.getFoodConservation();
        this.expirationDate = product.getExpirationDate();
        this.optimalConsumptionDate = product.getOptimalConsumptionDate();
    }

    public FoodConservation getFoodConservation() {
        return foodConservation;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public LocalDateTime getOptimalConsumptionDate() {
        return optimalConsumptionDate;
    }
}
