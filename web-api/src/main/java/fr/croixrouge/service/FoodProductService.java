package fr.croixrouge.service;

import fr.croix.rouge.storage.model.product.FoodProduct;
import fr.croix.rouge.storage.model.product.ProductLimit;
import fr.croix.rouge.storage.model.qauntifier.LiquidQuantifier;
import fr.croix.rouge.storage.model.qauntifier.LiquidUnit;
import fr.croixrouge.domain.model.ID;

public class FoodProductService {

    FoodProductService() {
        FoodProduct foodProduct = new FoodProduct(new ID("1"), "name", new LiquidQuantifier(1, LiquidUnit.LITER), ProductLimit.NO_LIMIT);
    }

}
