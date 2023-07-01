package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.storage.model.product.Product;

public class ProductResponse {

    public Long productId;

    public String name;

    public QuantifierDTO quantity;

    public ProductResponse() {
    }

    public ProductResponse(Product product) {
        this.productId = product.getId().value();
        this.name = product.getName();
        this.quantity = QuantifierDTO.fromQuantifier(product.getQuantity());
    }

    public ProductResponse(Long productId, String name, QuantifierDTO quantity) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QuantifierDTO getQuantity() {
        return quantity;
    }

    public void setQuantity(QuantifierDTO quantity) {
        this.quantity = quantity;
    }
}
