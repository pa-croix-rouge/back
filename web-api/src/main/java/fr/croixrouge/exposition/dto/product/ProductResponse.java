package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.storage.model.product.Product;

public class ProductResponse {

    protected Long id;

    protected String name;
    protected QuantifierDTO quantity;

    public ProductResponse() {
    }

    public ProductResponse(Product product) {
        this.id = product.getId().value();
        this.name = product.getName();
        this.quantity = QuantifierDTO.fromQuantifier(product.getQuantity());
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public QuantifierDTO getQuantity() {
        return quantity;
    }
}
