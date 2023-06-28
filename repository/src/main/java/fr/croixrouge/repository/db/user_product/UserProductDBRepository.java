package fr.croixrouge.repository.db.user_product;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserProductDBRepository extends CrudRepository<BeneficiaryProductDB, Long> {

    @Query("select b from BeneficiaryProductDB b where b.beneficiaryDB.id = ?1 and b.productDB.id = ?2")
    List<BeneficiaryProductDB> findByBeneficiaryDB_IdAndProductDB_Id(Long id, Long id1);

}
