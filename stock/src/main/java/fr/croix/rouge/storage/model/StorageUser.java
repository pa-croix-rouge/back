package fr.croix.rouge.storage.model;

import fr.croixrouge.domain.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StorageUser extends User {

    private final List<StorageUserProduct> products;

    public StorageUser(String userId, String username, String password, List<String> authorities) {
        super(userId, username, password, authorities);
        products = new ArrayList<>();
    }

    public void addProduct(FoodProduct product, int quantity) {
        products.add(new StorageUserProduct(product, LocalDate.now(), quantity));
    }

    public boolean canAddProduct(FoodProduct product, int quantity) {
        return !product.limit.isLimitReached(products.stream().filter(p -> p.product().equals(product)).toList());
    }

}
