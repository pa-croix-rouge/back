package fr.croixrouge.repository.db.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserDBRepository extends CrudRepository<UserDB, Long> {
    @Query("select u from UserDB u where u.username = :username")
    Optional<UserDB> findByUsername(@Param("username") String username);

    @Query("select u from UserDB u where u.localUnitDB.localUnitID = ?1")
    List<UserDB> findByLocalUnitDB_LocalUnitID(Long localUnitID);

    @Query("select u from UserDB u inner join u.roleDBs roleDBs where roleDBs.roleID = ?1")
    List<UserDB> findByRoleDBs_RoleID(Long roleID);

    @Query("select u from UserDB u where u.tokenToValidateEmail = :token")
    Optional<UserDB> findByToken(@Param("token") String token);

}
