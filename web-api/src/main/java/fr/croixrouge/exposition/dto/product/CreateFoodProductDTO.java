package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.exposition.dto.CreationDTO;
import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.storage.model.product.FoodConservation;
import fr.croixrouge.storage.model.product.FoodProduct;

import java.time.LocalDateTime;

public class CreateFoodProductDTO extends CreationDTO<FoodProduct> {

    private final String name;
    private final QuantifierDTO quantity;

    private final FoodConservation foodConservation;

    private final LocalDateTime expirationDate;

    private final LocalDateTime optimalConsumptionDate;

    private final double price;

    public CreateFoodProductDTO(String name, QuantifierDTO quantity, FoodConservation foodConservation, LocalDateTime expirationDate, LocalDateTime optimalConsumptionDate, double price) {
        this.name = name;
        this.quantity = quantity;
        this.foodConservation = foodConservation;
        this.expirationDate = expirationDate;
        this.optimalConsumptionDate = optimalConsumptionDate;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public QuantifierDTO getQuantity() {
        return quantity;
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

    public double getPrice() {
        return price;
    }

    @Override
    public FoodProduct toModel() {
        return new FoodProduct(null, name, quantity.toQuantifier(), null, foodConservation, expirationDate, optimalConsumptionDate, price);
    }
}
