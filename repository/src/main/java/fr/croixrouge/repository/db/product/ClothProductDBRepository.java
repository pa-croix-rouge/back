package fr.croixrouge.repository.db.product;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClothProductDBRepository extends CrudRepository<ClothProductDB, Long> {

    @Query("SELECT cp FROM ClothProductDB cp WHERE cp.productDB.id = ?1")
    Optional<ClothProductDB> findByProductId(Long productId);
}
