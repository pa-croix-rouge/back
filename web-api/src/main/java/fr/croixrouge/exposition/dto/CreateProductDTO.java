package fr.croixrouge.exposition.dto;

import fr.croixrouge.storage.model.product.Product;

public class CreateProductDTO extends CreationDTO<Product> {

    private String name;
    private QuantifierDTO quantity;

    public CreateProductDTO() {
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

    @Override
    public Product toModel() {
        return new Product(null, name, quantity.toQuantifier(), null);
    }

}
