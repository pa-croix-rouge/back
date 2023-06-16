package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.storage.model.product.FoodConservation;
import fr.croixrouge.storage.model.product.FoodProduct;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class FoodProductResponse extends ProductResponse {

    private Long id;

    private String foodConservation;

    private String expirationDate;

    private String optimalConsumptionDate;

    private Long price;

    public FoodProductResponse() {
    }

    public FoodProductResponse(FoodProduct product) {
        super(product.getProduct());
        this.id = product.getId().value();
        this.foodConservation = product.getFoodConservation().getLabel();
        this.expirationDate = product.getExpirationDate().withZoneSameInstant(ZoneId.of("Europe/Paris")).toString();
        this.optimalConsumptionDate = product.getOptimalConsumptionDate().withZoneSameInstant(ZoneId.of("Europe/Paris")).toString();
        this.price = (long) product.getPrice();
    }

    public FoodProductResponse(Long id, Long productId, String name, QuantifierDTO quantifierDTO, FoodConservation foodConservation, ZonedDateTime expirationDate, ZonedDateTime optimalConsumptionDate, Long price) {
        super(productId, name, quantifierDTO);
        this.id = id;
        this.foodConservation = foodConservation.getLabel();
        this.expirationDate = expirationDate.withZoneSameInstant(ZoneId.of("Europe/Paris")).toString();
        this.optimalConsumptionDate = optimalConsumptionDate.withZoneSameInstant(ZoneId.of("Europe/Paris")).toString();
        this.price = price;
    }

    public static FoodProductResponse fromFoodProduct(FoodProduct product) {
        return new FoodProductResponse(product);
    }

    public Long getId() {
        return id;
    }

    public String getFoodConservation() {
        return foodConservation;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getOptimalConsumptionDate() {
        return optimalConsumptionDate;
    }

    public Long getPrice() {
        return price;
    }
}
