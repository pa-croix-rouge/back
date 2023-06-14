package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.FoodConservation;
import fr.croixrouge.storage.model.product.FoodProduct;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class FoodStorageProductResponse extends StorageProductResponse {
    private Long id;

    private String foodConservation;

    private String expirationDate;

    private String optimalConsumptionDate;

    public FoodStorageProductResponse() {
    }

    public FoodStorageProductResponse(FoodProduct product, StorageProduct storageProduct) {
        super(product.getProduct(), storageProduct);
        this.id = product.getId().value();
        this.foodConservation = product.getFoodConservation().getLabel();
        this.expirationDate = product.getExpirationDate().withZoneSameInstant(ZoneId.of("Europe/Paris")).toString();
        this.optimalConsumptionDate = product.getOptimalConsumptionDate().withZoneSameInstant(ZoneId.of("Europe/Paris")).toString();
    }

    public FoodStorageProductResponse(Long id, Long storageProductId, Long productId, Long storageId, String productName, int quantity, String quantifierQuantity, String quantifierName, FoodConservation foodConservation, ZonedDateTime expirationDate, ZonedDateTime optimalConsumptionDate) {
        super(productId, storageProductId, storageId, productName, quantity, quantifierQuantity, quantifierName);
        this.id = id;
        this.foodConservation = foodConservation.getLabel();
        this.expirationDate = expirationDate.withZoneSameInstant(ZoneId.of("Europe/Paris")).toString();
        this.optimalConsumptionDate = optimalConsumptionDate.withZoneSameInstant(ZoneId.of("Europe/Paris")).toString();
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

    public String getFoodConservation() {
        return foodConservation;
    }

    public void setFoodConservation(FoodConservation foodConservation) {
        this.foodConservation = foodConservation.getLabel();
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate.withZoneSameInstant(ZoneId.of("Europe/Paris")).toString();
    }

    public String getOptimalConsumptionDate() {
        return optimalConsumptionDate;
    }

    public void setOptimalConsumptionDate(ZonedDateTime optimalConsumptionDate) {
        this.optimalConsumptionDate = optimalConsumptionDate.withZoneSameInstant(ZoneId.of("Europe/Paris")).toString();
    }
}
