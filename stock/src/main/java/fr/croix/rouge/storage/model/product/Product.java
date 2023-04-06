package fr.croix.rouge.storage.model.product;

import fr.croix.rouge.storage.model.qauntifier.Quantifier;
import fr.croixrouge.domain.model.ID;

public class Product {

    protected final ID id;

    protected final String name;
    protected final Quantifier Quantity;

    protected final ProductLimit limit;

    public Product(ID id, String name, Quantifier quantity, ProductLimit limit) {
        this.id = id;
        this.name = name;
        Quantity = quantity;
        this.limit = limit;
    }

    public ID getId() {
        return id;
    }

    public ProductLimit getLimit() {
        return limit;
    }

    public Quantifier getQuantity() {
        return Quantity;
    }
}
