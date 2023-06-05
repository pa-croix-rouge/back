package fr.croixrouge.repository.db.user_product;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserProductDBRepository extends CrudRepository<UserProductDB, Long> {
    @Query("select u from UserProductDB u where u.userDB.userID = ?1 and u.productDB.id = ?2")
    List<UserProductDB> findByUserDB_UserIDAndProductDB_Id(Long userID, Long id);



}
