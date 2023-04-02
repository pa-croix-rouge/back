package fr.croix.rouge.storage.model;

import fr.croixrouge.domain.model.ID;

public class FoodProduct extends Product {


    public FoodProduct(ID id, String name, Quantifier quantity, ProductLimit limit) {
        super(id, name, quantity, limit);
    }
}
