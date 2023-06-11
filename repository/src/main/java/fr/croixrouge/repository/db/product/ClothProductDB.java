package fr.croixrouge.repository.db.product;

import fr.croixrouge.storage.model.product.ClothSize;
import jakarta.persistence.*;

@Table(name = "cloth-product")
@Entity
public class ClothProductDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated
    @Column(name = "size")
    private ClothSize size;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private ProductDB productDB;

    public ClothProductDB() {
    }

    public ClothProductDB(Long id, ProductDB productDB, ClothSize size) {
        this.id = id;
        this.productDB = productDB;
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

    public ProductDB getProductDB() {
        return productDB;
    }

    public void setProductDB(ProductDB productDB) {
        this.productDB = productDB;
    }
}
