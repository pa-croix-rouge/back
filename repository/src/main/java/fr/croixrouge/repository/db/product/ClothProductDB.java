package fr.croixrouge.repository.db.product;

import fr.croixrouge.storage.model.product.ClothGender;
import fr.croixrouge.storage.model.product.ClothSize;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Table(name = "cloth-product")
@Entity
@SQLDelete(sql = "UPDATE product SET deleted = CURRENT_TIME WHERE id=?")
@Where(clause = "deleted IS NULL")
public class ClothProductDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated
    @Column(name = "size")
    private ClothSize size;

    @OneToOne(optional = false)
    private ProductDB productDB;

    @Enumerated
    @Column(name = "gender")
    private ClothGender gender;

    @Column(name = "deleted")
    private LocalDateTime deletionDate;

    public LocalDateTime getDeletionDate() {
        return deletionDate;
    }

    public void setDeletionDate(LocalDateTime deletionDate) {
        this.deletionDate = deletionDate;
    }

    public ClothProductDB() {
    }

    public ClothProductDB(Long id, ProductDB productDB, ClothSize size, ClothGender gender) {
        this.id = id;
        this.productDB = productDB;
        this.size = size;
        this.gender = gender;
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

    public ClothGender getGender() {
        return gender;
    }

    public void setGender(ClothGender gender) {
        this.gender = gender;
    }
}
