package fr.croixrouge.storage.model.product;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.quantifier.Quantifier;

public class ClothProduct extends Product {

    private final ClothSize size;


    public ClothProduct(ID id, String name, Quantifier quantity, ProductLimit limit, ClothSize size) {
        super(id, name, quantity, limit);
        this.size = size;
    }

    public ClothSize getSize() {
        return size;
    }

}
