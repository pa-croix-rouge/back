package fr.croixrouge.storage.model.product;

import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.quantifier.Quantifier;

public class Product extends Entity<ID> {

    protected final String name;
    protected final Quantifier quantity;

    protected final ProductLimit limit;

    public Product(ID id, String name, Quantifier quantity, ProductLimit limit) {
        super(id);
        this.name = name;
        this.quantity = quantity;
        this.limit = limit;
    }

    public Product(Product product) {
        this(product.id, product.name, product.quantity, product.limit);
    }

    public ID getId() {
        return id;
    }

    public ProductLimit getLimit() {
        return limit;
    }

    public Quantifier getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }
}
