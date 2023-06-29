package fr.croixrouge.storage.model;

import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.product.Product;

import java.util.Objects;

public class StorageProduct extends Entity<ID> {

    private final Storage storage;

    private final Product product;

    private final int quantity;

    public StorageProduct(ID id, Storage storage, Product product, int quantity) {
        super(id);
        this.storage = storage;
        this.product = product;
        this.quantity = quantity;
    }

    public StorageProduct(Storage storage, Product product, int quantity) {
        super (null);
        this.storage = storage;
        this.product = product;
        this.quantity = quantity;
    }

    public Storage getStorage() {
        return storage;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorageProduct that = (StorageProduct) o;
        return quantity == that.quantity && Objects.equals(storage, that.storage) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storage, product, quantity);
    }

    @Override
    public String toString() {
        return "StorageProduct{" +
                "storage=" + storage +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
