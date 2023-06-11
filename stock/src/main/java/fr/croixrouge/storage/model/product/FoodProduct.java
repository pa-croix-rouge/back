package fr.croixrouge.storage.model.product;

import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.quantifier.Quantifier;

import java.time.LocalDateTime;

public class FoodProduct extends Entity<ID> {

    private final Product product;

    private final FoodConservation foodConservation;

    private final LocalDateTime expirationDate;

    private final LocalDateTime optimalConsumptionDate;

    private final double price;

    public FoodProduct(ID id, ID productId, String name, Quantifier quantity, ProductLimit limit, FoodConservation foodConservation, LocalDateTime expirationDate, LocalDateTime optimalConsumptionDate, double price) {
        super(id);
        this.product = new Product(productId, name, quantity, limit);
        this.foodConservation = foodConservation;
        this.expirationDate = expirationDate;
        this.optimalConsumptionDate = optimalConsumptionDate;
        this.price = price;
    }

    public FoodProduct(ID id, Product product, FoodConservation foodConservation, LocalDateTime expirationDate, LocalDateTime optimalConsumptionDate, double price) {
        this(id, product.getId(), product.name, product.quantity, product.limit, foodConservation, expirationDate, optimalConsumptionDate, price);
    }

    public Product getProduct() {
        return product;
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
}
