package fr.croix.rouge.storage.model;

import fr.croix.rouge.storage.model.product.Product;

import java.time.LocalDate;
import java.util.List;

public class StorageUser {

    private final String userId;
    private final List<StorageUserProduct> products;

    public StorageUser(String userId, List<StorageUserProduct> products) {
        this.userId = userId;
        this.products = products;
    }


    public void addProduct(Product product, int quantity) {
        products.add(new StorageUserProduct(product, LocalDate.now(), quantity));
    }

    public boolean canAddProduct(Product product, int quantity) {
        return !product.getLimit().isLimitReached(products.stream().filter(p -> p.product().equals(product)).toList());
    }

}
