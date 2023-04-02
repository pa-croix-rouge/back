package fr.croixrouge.service;

import fr.croix.rouge.storage.model.FoodProduct;
import fr.croix.rouge.storage.model.LiquidQuantifier;
import fr.croix.rouge.storage.model.LiquidUnit;
import fr.croix.rouge.storage.model.ProductLimit;
import fr.croixrouge.domain.model.ID;

public class FoodProductService {

    FoodProductService() {
        FoodProduct foodProduct = new FoodProduct(new ID("1"), "name", new LiquidQuantifier(1, LiquidUnit.LITER), ProductLimit.NO_LIMIT);
    }

}
