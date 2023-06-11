package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.storage.model.product.Product;

public class ProductResponse {

    protected Long productId;

    protected String name;
    protected QuantifierDTO quantity;

    public ProductResponse() {
    }

    public ProductResponse(Product product) {
        this.productId = product.getId().value();
        this.name = product.getName();
        this.quantity = QuantifierDTO.fromQuantifier(product.getQuantity());
    }

    public String getName() {
        return name;
    }

    public Long getProductId() {
        return productId;
    }

    public QuantifierDTO getQuantity() {
        return quantity;
    }
}
