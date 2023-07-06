package fr.croixrouge.repository.db.product;

import fr.croixrouge.repository.db.localunit.LocalUnitDB;
import fr.croixrouge.repository.db.product_limit.ProductLimitDB;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;

@Table(name = "product")
@Entity
@SQLDelete(sql = "UPDATE product SET deleted = CURRENT_TIME WHERE id=?")
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_limit_db_id")
    private ProductLimitDB productLimitDB;

    @Column(name = "deleted")
    private LocalDateTime deletionDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "local_unit_db_localunit_id", nullable = false)
    private LocalUnitDB localUnitDB;

    public ProductDB() {
    }

    public ProductDB(Long id, String name, Double quantity, String unit, ProductLimitDB productLimitDB, LocalUnitDB localUnitDB) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.productLimitDB = productLimitDB;
        this.localUnitDB = localUnitDB;
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

    public LocalUnitDB getLocalUnitDB() {
        return localUnitDB;
    }

    public LocalDateTime getDeletionDate() {
        return deletionDate;
    }

}
