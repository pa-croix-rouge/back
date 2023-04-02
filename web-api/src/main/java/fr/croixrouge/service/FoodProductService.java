package fr.croixrouge.service;

import fr.croix.rouge.storage.model.FoodProduct;
import fr.croix.rouge.storage.model.LiquidQuantifier;
import fr.croix.rouge.storage.model.LiquidUnit;

public class FoodProductService {

    FoodProductService() {
        FoodProduct foodProduct = new FoodProduct(new FoodProduct.ID("1"), "name", new LiquidQuantifier(1, LiquidUnit.LITER));
    }

}
