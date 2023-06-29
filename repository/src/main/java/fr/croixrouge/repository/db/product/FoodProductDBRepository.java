package fr.croixrouge.repository.db.product;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FoodProductDBRepository extends CrudRepository<FoodProductDB, Long> {

    @Query("SELECT fp FROM FoodProductDB fp WHERE fp.productDB.id = ?1")
    Optional<FoodProductDB> findByProductId(Long productId);
}
