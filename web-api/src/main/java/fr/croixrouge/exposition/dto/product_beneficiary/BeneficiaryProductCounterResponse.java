package fr.croixrouge.exposition.dto.product_beneficiary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.croixrouge.exposition.dto.product.ClothProductResponse;
import fr.croixrouge.exposition.dto.product.FoodProductResponse;
import fr.croixrouge.storage.model.product.ClothProduct;
import fr.croixrouge.storage.model.product.FoodProduct;

import java.util.Map;

public class BeneficiaryProductCounterResponse {


    @JsonSerialize(keyUsing = FoodProductResponseSerializer.class)
    public Map<FoodProductResponse, Integer> foodProducts;

    @JsonSerialize(keyUsing = ClothProductResponseSerializer.class)
    public Map<ClothProductResponse, Integer> clothProducts;


    public BeneficiaryProductCounterResponse(Map<FoodProduct, Integer> foodProducts, Map<ClothProduct, Integer> clothProducts) {

        this.foodProducts = foodProducts.entrySet().stream().collect(
                java.util.stream.Collectors.toMap(
                        entry -> FoodProductResponse.fromFoodProduct(entry.getKey()),
                        Map.Entry::getValue
                )
        );

        this.clothProducts = clothProducts.entrySet().stream().collect(
                java.util.stream.Collectors.toMap(
                        entry -> ClothProductResponse.fromClothProduct(entry.getKey()),
                        Map.Entry::getValue
                )
        );

    }

    public BeneficiaryProductCounterResponse() {
    }

    public Map<FoodProductResponse, Integer> getFoodProducts() {
        return foodProducts;
    }

    public Map<ClothProductResponse, Integer> getClothProducts() {
        return clothProducts;
    }
}
