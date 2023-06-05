package fr.croixrouge.repository.db.user_product;

import fr.croixrouge.repository.db.product.ProductDB;
import fr.croixrouge.repository.db.storage.StorageDB;
import fr.croixrouge.repository.db.user.UserDB;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table(name = "user-product")
@Entity
public class UserProductDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_db_user_id", nullable = false)
    private UserDB userDB;

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

    public UserProductDB() {
    }

    public UserProductDB(Long id, UserDB userDB, ProductDB productDB, StorageDB storageDB, LocalDateTime date, Integer number) {
        this.id = id;
        this.userDB = userDB;
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

    public UserDB getUserDB() {
        return userDB;
    }

    public void setUserDB(UserDB userDB) {
        this.userDB = userDB;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
