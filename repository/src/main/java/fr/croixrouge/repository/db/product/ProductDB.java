package fr.croixrouge.repository.db.product;

import fr.croixrouge.repository.db.product_limit.ProductLimitDB;
import jakarta.persistence.*;

@Table(name = "product")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class ProductDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @Column(name = "unit", nullable = false)
    private String unit;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_limit_db_id")
    private ProductLimitDB productLimitDB;
    public ProductDB() {
    }

    public ProductDB(Long id, String name, Double quantity, String unit, ProductLimitDB productLimitDB) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.productLimitDB = productLimitDB;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ProductLimitDB getProductLimitDB() {
        return productLimitDB;
    }

    public void setProductLimitDB(ProductLimitDB productLimitDB) {
        this.productLimitDB = productLimitDB;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }
}
