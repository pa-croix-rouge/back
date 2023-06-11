package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.storage.model.product.FoodConservation;
import fr.croixrouge.storage.model.product.FoodProduct;

import java.time.LocalDateTime;

public class FoodProductResponse extends ProductResponse {

    private Long id;

    private FoodConservation foodConservation;

    private LocalDateTime expirationDate;

    private LocalDateTime optimalConsumptionDate;

    public FoodProductResponse() {
    }

    public FoodProductResponse(FoodProduct product) {
        super(product.getProduct());
        this.id = product.getId().value();
        this.foodConservation = product.getFoodConservation();
        this.expirationDate = product.getExpirationDate();
        this.optimalConsumptionDate = product.getOptimalConsumptionDate();
    }

    public FoodProductResponse(Long id, Long productId, String name, QuantifierDTO quantifierDTO, FoodConservation foodConservation, LocalDateTime expirationDate, LocalDateTime optimalConsumptionDate) {
        super(productId, name, quantifierDTO);
        this.id = id;
        this.foodConservation = foodConservation;
        this.expirationDate = expirationDate;
        this.optimalConsumptionDate = optimalConsumptionDate;
    }

    public static FoodProductResponse fromFoodProduct(FoodProduct product) {
        return new FoodProductResponse(product);
    }

    public Long getId() {
        return id;
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
