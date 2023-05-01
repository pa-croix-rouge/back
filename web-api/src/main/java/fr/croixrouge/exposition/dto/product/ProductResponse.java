package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.storage.model.product.Product;

public class ProductResponse {

    protected final String id;

    protected final String name;
    protected final QuantifierDTO quantity;

    public ProductResponse(Product product) {
        this.id = product.getId().value();
        this.name = product.getName();
        this.quantity = QuantifierDTO.fromQuantifier(product.getQuantity());
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public QuantifierDTO getQuantity() {
        return quantity;
    }
}
