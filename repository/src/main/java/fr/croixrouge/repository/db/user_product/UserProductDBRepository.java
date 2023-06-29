package fr.croixrouge.repository.db.user_product;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserProductDBRepository extends CrudRepository<BeneficiaryProductDB, Long> {

    @Query("select b from BeneficiaryProductDB b where b.beneficiaryDB.id = ?1 and b.productDB.id = ?2")
    List<BeneficiaryProductDB> findByBeneficiaryDB_IdAndProductDB_Id(Long id, Long id1);

    @Query("select b from BeneficiaryProductDB b where b.productDB.id = ?1 and b.storageDB.id = ?2 and b.date = ?3")
    Optional<BeneficiaryProductDB> findByProductDB_IdAndStorageDB_IdAndDate(Long id, Long id1, LocalDateTime date);


}
