package fr.croixrouge.repository.db.product;

import fr.croixrouge.storage.model.product.FoodConservation;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table(name = "food-product")
@Entity
public class FoodProductDB {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(name = "price")
    private Float price;

    @Enumerated
    @Column(name = "food_conservation")
    private FoodConservation foodConservation;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "optimal_consumption_date")
    private LocalDateTime optimalConsumptionDate;

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public FoodProductDB() {
    }

    public FoodProductDB(Long id, Float price, FoodConservation foodConservation, LocalDateTime expirationDate, LocalDateTime optimalConsumptionDate) {
        this.id = id;
        this.price = price;
        this.foodConservation = foodConservation;
        this.expirationDate = expirationDate;
        this.optimalConsumptionDate = optimalConsumptionDate;
    }

    public LocalDateTime getOptimalConsumptionDate() {
        return optimalConsumptionDate;
    }

    public void setOptimalConsumptionDate(LocalDateTime optimalConsumptionDate) {
        this.optimalConsumptionDate = optimalConsumptionDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public FoodConservation getFoodConservation() {
        return foodConservation;
    }

    public void setFoodConservation(FoodConservation foodConservation) {
        this.foodConservation = foodConservation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
