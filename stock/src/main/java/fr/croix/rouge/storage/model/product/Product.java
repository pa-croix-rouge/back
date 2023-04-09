package fr.croix.rouge.storage.model.product;

import fr.croix.rouge.storage.model.qauntifier.Quantifier;
import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;

public class Product extends Entity<ID> {

    protected final String name;
    protected final Quantifier Quantity;

    protected final ProductLimit limit;

    public Product(ID id, String name, Quantifier quantity, ProductLimit limit) {
        super(id);
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
