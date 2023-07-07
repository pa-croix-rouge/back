package fr.croixrouge.storage.model.product;

import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.quantifier.Quantifier;

public class Product extends Entity<ID> {

    protected final String name;
    protected final Quantifier quantity;

    protected final ProductLimit limit;

    protected final ID localUnitId;

    public Product(ID id, String name, Quantifier quantity, ProductLimit limit, ID localUnitId) {
        super(id);
        this.name = name;
        this.quantity = quantity;
        this.limit = limit;
        this.localUnitId = localUnitId;
    }

    public Product(Product product) {
        this(product.id, product.name, product.quantity, product.limit, product.getLocalUnitId());
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

    public ID getLocalUnitId() {
        return localUnitId;
    }
}
