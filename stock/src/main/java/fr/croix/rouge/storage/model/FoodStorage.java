package fr.croix.rouge.storage.model;

import fr.croix.rouge.storage.model.product.FoodProduct;
import fr.croixrouge.domain.model.Address;
import fr.croixrouge.domain.model.LocalUnit;

import java.util.Map;

public class FoodStorage {

    private final LocalUnit localUnit;
    private final Address address;

    private Map<FoodProduct, Integer> foodProducts;

    public FoodStorage(LocalUnit localUnit, Address address) {
        this.localUnit = localUnit;
        this.address = address;
    }

    public void addFoodProduct(FoodProduct foodProduct, int quantity) {
        foodProducts.put(foodProduct, quantity);
    }

    public void removeFoodProduct(FoodProduct foodProduct, int quantity) {
        foodProducts.put(foodProduct, foodProducts.get(foodProduct) - quantity);
    }

}
