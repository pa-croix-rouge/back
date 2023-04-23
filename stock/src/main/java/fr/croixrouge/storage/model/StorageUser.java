package fr.croixrouge.storage.model;

import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.product.Product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StorageUser extends Entity<ID> {
    private final List<StorageUserProduct> products;

    public StorageUser(ID userId, List<StorageUserProduct> products) {
        super(userId);
        this.products = products;
    }


    public void addProduct(Product product, int quantity) {
        products.add(new StorageUserProduct(product, LocalDate.now(), quantity));
    }

    public boolean canAddProduct(Product product, int quantity) {
        List<StorageUserProduct> filtered = new ArrayList<>(products.stream().filter(p -> p.product().equals(product)).toList());
        filtered.add(new StorageUserProduct(product, LocalDate.now(), quantity));
        return !product.getLimit().isLimitReached(filtered);
    }

}
