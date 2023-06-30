package fr.croixrouge.storage.model.product;

import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.quantifier.NumberedUnit;
import fr.croixrouge.storage.model.quantifier.Quantifier;

import java.util.Objects;

public class ClothProduct extends Entity<ID> {

    private final Product product;
    private final ClothSize size;
    private final ClothGender gender;

    public ClothProduct(ID id, ID productId, String name, double quantity, ProductLimit limit, ClothSize size, ClothGender clothGender) {
        super(id);
        this.product = new Product(productId, name, new Quantifier(quantity, NumberedUnit.NUMBER), limit);
        this.size = size;
        this.gender = clothGender;
    }

    public ClothProduct(ID id, Product product, ClothSize size, ClothGender clothGender) {
        this(id, product.getId(), product.name, product.quantity.getQuantity(), product.limit, size, clothGender);
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
