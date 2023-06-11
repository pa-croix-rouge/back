package fr.croixrouge.storage.model.product;

import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.quantifier.NumberedUnit;
import fr.croixrouge.storage.model.quantifier.Quantifier;

public class ClothProduct extends Entity<ID> {

    private final Product product;
    private final ClothSize size;

    public ClothProduct(ID id, ID productId, String name, double quantity, ClothSize size) {
        super(id);
        this.product = new Product(productId, name, new Quantifier(quantity, NumberedUnit.NUMBER), ProductLimit.NO_LIMIT);
        this.size = size;
    }

    public ClothProduct(ID id, Product product, ClothSize size) {
        this(id, product.getId(), product.name, product.quantity.getQuantity(), size);
    }

    public Product getProduct() {
        return product;
    }

    public ClothSize getSize() {
        return size;
    }
}
