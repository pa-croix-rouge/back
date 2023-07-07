package fr.croixrouge.repository.db.product_limit;


import fr.croixrouge.repository.db.localunit.LocalUnitDB;
import jakarta.persistence.*;

import java.time.Duration;

@Table(name = "product-limit")
@Entity
public class ProductLimitDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "duration", nullable = true)
    private Duration duration;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "quantity", nullable = true)
    private Double quantity;

    @Column(name = "unit", nullable = true)
    private String unit;

    @ManyToOne(optional = false)
    @JoinColumn(name = "local_unit_db_localunit_id", nullable = false)
    private LocalUnitDB localUnitDB;


    public ProductLimitDB() {
    }

    public ProductLimitDB(Long id, String name, Duration duration, Double quantity, String unit, LocalUnitDB localUnitDB) {
        this.id = id;
        this.duration = duration;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.localUnitDB = localUnitDB;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalUnitDB getLocalUnitDB() {
        return localUnitDB;
    }
}
