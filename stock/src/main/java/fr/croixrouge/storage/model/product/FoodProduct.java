package fr.croixrouge.storage.model.product;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.quantifier.Quantifier;

import java.time.LocalDateTime;

public class FoodProduct extends Product {

    private final FoodConservation foodConservation;

    private final LocalDateTime expirationDate;

    private final LocalDateTime optimalConsumptionDate;

    private final float price;

    public FoodProduct(ID id, String name, Quantifier quantity, ProductLimit limit, FoodConservation foodConservation, LocalDateTime expirationDate, LocalDateTime optimalConsumptionDate, float price) {
        super(id, name, quantity, limit);
        this.foodConservation = foodConservation;
        this.expirationDate = expirationDate;
        this.optimalConsumptionDate = optimalConsumptionDate;
        this.price = price;
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

    public float getPrice() {
        return price;
    }
}
