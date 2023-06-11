package fr.croixrouge.repository.db.product_limit;


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

    @Column(name = "quantity", nullable = true)
    private Double quantity;

    @Column(name = "unit", nullable = true)
    private String unit;

    public ProductLimitDB() {
    }

    public ProductLimitDB(Long id, Duration duration, Double quantity, String unit) {
        this.id = id;
        this.duration = duration;
        this.quantity = quantity;
        this.unit = unit;
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
}
