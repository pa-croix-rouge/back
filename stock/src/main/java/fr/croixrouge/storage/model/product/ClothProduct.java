package fr.croixrouge.storage.model.product;

import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;

import java.util.Objects;

public class ClothProduct extends Entity<ID> {

    private final Product product;
    private final ClothSize size;
    private final ClothGender gender;

    public ClothProduct(ID id, Product product, ClothSize size, ClothGender clothGender) {
        super(id);
        this.product = product;
        this.size = size;
        this.gender = clothGender;
    }

    public Product getProduct() {
        return product;
    }

    public ClothSize getSize() {
        return size;
    }

    public ClothGender getGender() {
        return gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClothProduct that = (ClothProduct) o;
        return Objects.equals(product, that.product) && size == that.size && gender == that.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, size, gender);
    }

    @Override
    public String toString() {
        return "ClothProduct{" +
                "product=" + product +
                ", size=" + size +
                ", gender=" + gender +
                '}';
    }
}
