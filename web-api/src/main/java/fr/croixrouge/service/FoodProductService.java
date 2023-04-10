package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.product.FoodProduct;
import fr.croixrouge.storage.model.product.ProductLimit;
import fr.croixrouge.storage.model.quantifier.LiquidQuantifier;
import fr.croixrouge.storage.model.quantifier.LiquidUnit;

public class FoodProductService {

    FoodProductService() {
        FoodProduct foodProduct = new FoodProduct(new ID("1"), "name", new LiquidQuantifier(1, LiquidUnit.LITER), ProductLimit.NO_LIMIT);
    }

}
