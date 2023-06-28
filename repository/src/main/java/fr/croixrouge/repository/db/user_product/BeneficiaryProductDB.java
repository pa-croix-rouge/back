package fr.croixrouge.repository.db.user_product;

import fr.croixrouge.repository.db.beneficiary.BeneficiaryDB;
import fr.croixrouge.repository.db.product.ProductDB;
import fr.croixrouge.repository.db.storage.StorageDB;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table(name = "beneficiary-product")
@Entity
public class BeneficiaryProductDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "beneficiary_db_id", nullable = false, unique = true)
    private BeneficiaryDB beneficiaryDB;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_db_id", nullable = false)
    private ProductDB productDB;

    @ManyToOne(optional = false)
    @JoinColumn(name = "storage_db_id", nullable = false)
    private StorageDB storageDB;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "number", nullable = false)
    private Integer number;


    public BeneficiaryProductDB() {
    }

    public BeneficiaryProductDB(Long id, BeneficiaryDB beneficiaryDB, ProductDB productDB, StorageDB storageDB, LocalDateTime date, Integer number) {
        this.id = id;
        this.beneficiaryDB = beneficiaryDB;
        this.productDB = productDB;
        this.storageDB = storageDB;
        this.date = date;
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public StorageDB getStorageDB() {
        return storageDB;
    }

    public void setStorageDB(StorageDB storageDB) {
        this.storageDB = storageDB;
    }

    public ProductDB getProductDB() {
        return productDB;
    }

    public void setProductDB(ProductDB productDB) {
        this.productDB = productDB;
    }

    public BeneficiaryDB getBeneficiaryDB() {
        return beneficiaryDB;
    }

    public void setBeneficiaryDB(BeneficiaryDB beneficiaryDB) {
        this.beneficiaryDB = beneficiaryDB;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
