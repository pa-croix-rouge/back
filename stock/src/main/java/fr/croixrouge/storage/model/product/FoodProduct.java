package fr.croixrouge.storage.model.product;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.quantifier.Quantifier;

public class FoodProduct extends Product {


    public FoodProduct(ID id, String name, Quantifier quantity, ProductLimit limit) {
        super(id, name, quantity, limit);
    }
}
