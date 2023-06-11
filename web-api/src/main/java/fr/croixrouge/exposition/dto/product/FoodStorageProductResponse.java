package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.FoodConservation;
import fr.croixrouge.storage.model.product.FoodProduct;

import java.time.LocalDateTime;

public class FoodStorageProductResponse extends StorageProductResponse {
    private Long id;

    private FoodConservation foodConservation;

    private LocalDateTime expirationDate;

    private LocalDateTime optimalConsumptionDate;

    public FoodStorageProductResponse() {
    }

    public FoodStorageProductResponse(FoodProduct product, StorageProduct storageProduct) {
        super(product.getProduct(), storageProduct);
        this.id = product.getId().value();
        this.foodConservation = product.getFoodConservation();
        this.expirationDate = product.getExpirationDate();
        this.optimalConsumptionDate = product.getOptimalConsumptionDate();
    }

    public FoodStorageProductResponse(Long id, Long storageProductId, Long productId, Long storageId, String productName, int quantity, String quantifierQuantity, String quantifierName, FoodConservation foodConservation, LocalDateTime expirationDate, LocalDateTime optimalConsumptionDate) {
        super(productId, storageProductId, storageId, productName, quantity, quantifierQuantity, quantifierName);
        this.id = id;
        this.foodConservation = foodConservation;
        this.expirationDate = expirationDate;
        this.optimalConsumptionDate = optimalConsumptionDate;
    }

    public static FoodStorageProductResponse fromFoodProduct(FoodProduct product, StorageProduct storageProduct) {
        return new FoodStorageProductResponse(product, storageProduct);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FoodConservation getFoodConservation() {
        return foodConservation;
    }

    public void setFoodConservation(FoodConservation foodConservation) {
        this.foodConservation = foodConservation;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDateTime getOptimalConsumptionDate() {
        return optimalConsumptionDate;
    }

    public void setOptimalConsumptionDate(LocalDateTime optimalConsumptionDate) {
        this.optimalConsumptionDate = optimalConsumptionDate;
    }
}
