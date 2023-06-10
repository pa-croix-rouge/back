package fr.croixrouge.repository.db.product;

import fr.croixrouge.storage.model.product.ClothSize;
import jakarta.persistence.*;

@Table(name = "cloth-product")
@Entity
public class ClothProductDB extends ProductDB {

    @Enumerated
    @Column(name = "size")
    private ClothSize size;

    public ClothProductDB() {
    }

    public ClothProductDB(ProductDB productDB, ClothSize size) {
        super(productDB.getId(), productDB.getName(), productDB.getQuantity(), productDB.getUnit(), productDB.getProductLimitDB());
        this.size = size;
    }

    public ClothSize getSize() {
        return size;
    }

    public void setSize(ClothSize size) {
        this.size = size;
    }
}
