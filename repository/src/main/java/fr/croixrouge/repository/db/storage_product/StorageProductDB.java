package fr.croixrouge.repository.db.storage_product;


import fr.croixrouge.repository.db.product.ProductDB;
import fr.croixrouge.repository.db.storage.StorageDB;
import jakarta.persistence.*;

@Table(name = "storage-product")
@Entity
public class StorageProductDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_db_id", nullable = false)
    private ProductDB productDB;

    @ManyToOne(optional = false)
    @JoinColumn(name = "storage_db_id", nullable = false)
    private StorageDB storageDB;

    @Column(name = "number", nullable = false)
    private Integer number;

    public StorageProductDB() {
    }

    public StorageProductDB(Long id, ProductDB productDB, StorageDB storageDB, Integer number) {
        this.id = id;
        this.productDB = productDB;
        this.storageDB = storageDB;
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
