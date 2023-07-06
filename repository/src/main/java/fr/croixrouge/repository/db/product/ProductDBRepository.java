package fr.croixrouge.repository.db.product;

import fr.croixrouge.repository.db.product_limit.ProductLimitDB;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductDBRepository extends CrudRepository<ProductDB, Long> {
    @Query("select p from ProductDB p where p.productLimitDB.id = ?1")
    List<ProductDB> findByProductLimitDB_Id(Long id);

}
