package fr.croixrouge.storage.model;

import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.storage.model.product.Product;

import java.time.LocalDate;
import java.util.Objects;

public final class StorageUserProduct extends Entity<ID> {

    private final User user;
    private final Product product;
    private final LocalDate date;
    private final int quantity;

    public StorageUserProduct(ID id, User user, Product product, LocalDate date, int quantity) {
        super(id);
        this.user = user;
        this.product = product;
        this.date = date;
        this.quantity = quantity;
    }

    public User user() {
        return user;
    }

    public Product product() {
        return product;
    }

    public LocalDate date() {
        return date;
    }

    public int quantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (StorageUserProduct) obj;
        return Objects.equals(this.product, that.product) &&
                Objects.equals(this.date, that.date) &&
                this.quantity == that.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, date, quantity);
    }

    @Override
    public String toString() {
        return "StorageUserProduct[" +
                "product=" + product + ", " +
                "date=" + date + ", " +
                "quantity=" + quantity + ']';
    }


}
