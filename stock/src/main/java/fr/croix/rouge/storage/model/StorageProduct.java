package fr.croix.rouge.storage.model;

import fr.croix.rouge.storage.model.product.Product;
import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;

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
}
