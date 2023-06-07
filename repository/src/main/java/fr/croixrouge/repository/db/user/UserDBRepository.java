package fr.croixrouge.repository.db.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserDBRepository extends CrudRepository<UserDB, Long> {
    @Query("select u from UserDB u where u.username = :username")
    Optional<UserDB> findByUsername(@Param("username") String username);

}
