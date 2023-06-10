package fr.croixrouge.repository.db.product;

import fr.croixrouge.storage.model.product.ClothSize;
import jakarta.persistence.*;

@Table(name = "cloth-product")
@Entity
public class ClothProductDB {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated
    @Column(name = "size")
    private ClothSize size;

    public ClothProductDB() {
    }

    public ClothProductDB(Long id, ClothSize size) {
        this.id = id;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClothSize getSize() {
        return size;
    }

    public void setSize(ClothSize size) {
        this.size = size;
    }
}
