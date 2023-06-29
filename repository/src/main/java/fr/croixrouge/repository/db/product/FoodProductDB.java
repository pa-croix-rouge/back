package fr.croixrouge.repository.db.product;

import fr.croixrouge.storage.model.product.FoodConservation;
import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Table(name = "food-product")
@Entity
public class FoodProductDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(optional = false)
    private ProductDB productDB;

    @Column(name = "price")
    private Float price;

    @Enumerated
    @Column(name = "food_conservation")
    private FoodConservation foodConservation;

    @Column(name = "expiration_date", nullable = false)
    private ZonedDateTime expirationDate;

    @Column(name = "optimal_consumption_date")
    private ZonedDateTime optimalConsumptionDate;

    public FoodProductDB() {
    }

    public FoodProductDB(Long id, ProductDB productDB, Float price, FoodConservation foodConservation, ZonedDateTime expirationDate, ZonedDateTime optimalConsumptionDate) {
        this.id = id;
        this.productDB = productDB;
        this.price = price;
        this.foodConservation = foodConservation;
        this.expirationDate = expirationDate;
        this.optimalConsumptionDate = optimalConsumptionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public ProductDB getProductDB() {
        return productDB;
    }

    public void setProductDB(ProductDB productDB) {
        this.productDB = productDB;
    }

    public FoodConservation getFoodConservation() {
        return foodConservation;
    }

    public void setFoodConservation(FoodConservation foodConservation) {
        this.foodConservation = foodConservation;
    }

    public ZonedDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public ZonedDateTime getOptimalConsumptionDate() {
        return optimalConsumptionDate;
    }

    public void setOptimalConsumptionDate(ZonedDateTime optimalConsumptionDate) {
        this.optimalConsumptionDate = optimalConsumptionDate;
    }
}
