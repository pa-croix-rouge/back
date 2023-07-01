package fr.croixrouge.storage.model.product;

import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;

import java.time.ZonedDateTime;
import java.util.Objects;

public class FoodProduct extends Entity<ID> {

    private final Product product;

    private final FoodConservation foodConservation;

    private final ZonedDateTime expirationDate;

    private final ZonedDateTime optimalConsumptionDate;

    private final double price;

    public FoodProduct(ID id, Product product, FoodConservation foodConservation, ZonedDateTime expirationDate, ZonedDateTime optimalConsumptionDate, double price) {
        super(id);
        this.product = product;
        this.foodConservation = foodConservation;
        this.expirationDate = expirationDate;
        this.optimalConsumptionDate = optimalConsumptionDate;
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public FoodConservation getFoodConservation() {
        return foodConservation;
    }

    public ZonedDateTime getExpirationDate() {
        return expirationDate;
    }

    public ZonedDateTime getOptimalConsumptionDate() {
        return optimalConsumptionDate;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodProduct that = (FoodProduct) o;
        return Double.compare(that.price, price) == 0 && Objects.equals(product, that.product) && foodConservation == that.foodConservation && Objects.equals(expirationDate, that.expirationDate) && Objects.equals(optimalConsumptionDate, that.optimalConsumptionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, foodConservation, expirationDate, optimalConsumptionDate, price);
    }

    @Override
    public String toString() {
        return "FoodProduct{" +
                "product=" + product +
                ", foodConservation=" + foodConservation +
                ", expirationDate=" + expirationDate +
                ", optimalConsumptionDate=" + optimalConsumptionDate +
                ", price=" + price +
                '}';
    }
}
