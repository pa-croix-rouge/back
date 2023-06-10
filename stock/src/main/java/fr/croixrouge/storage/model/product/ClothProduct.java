package fr.croixrouge.storage.model.product;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.quantifier.NumberedUnit;
import fr.croixrouge.storage.model.quantifier.Quantifier;

public class ClothProduct extends Product {

    private final ClothSize size;

    public ClothProduct(ID id, String name, double quantity, ClothSize size) {
        super(id, name, new Quantifier(quantity, NumberedUnit.NUMBER), ProductLimit.NO_LIMIT);
        this.size = size;
    }

    public ClothProduct(Product product, ClothSize size) {
        this(product.getId(), product.name, product.quantity.getQuantity(), size);
    }

    public ClothSize getSize() {
        return size;
    }

}
