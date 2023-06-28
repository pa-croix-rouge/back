package fr.croixrouge.storage.model;

import fr.croixrouge.domain.model.Beneficiary;
import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.product.Product;

import java.time.LocalDateTime;
import java.util.Objects;

public final class BeneficiaryProduct extends Entity<ID> {

    private final Beneficiary beneficiary;
    private final Product product;
    private final Storage storage;
    private final LocalDateTime date;
    private final int quantity;

    public BeneficiaryProduct(ID id, Beneficiary beneficiary, Product product, Storage storage, LocalDateTime date, int quantity) {
        super(id);
        this.beneficiary = beneficiary;
        this.product = product;
        this.storage = storage;
        this.date = date;
        this.quantity = quantity;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public Product product() {
        return product;
    }

    public LocalDateTime date() {
        return date;
    }

    public int quantity() {
        return quantity;
    }

    public Storage getStorage() {
        return storage;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BeneficiaryProduct) obj;
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
