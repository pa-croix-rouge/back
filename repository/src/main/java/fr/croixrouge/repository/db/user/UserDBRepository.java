package fr.croixrouge.repository.db.user;

import fr.croixrouge.domain.model.ID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserDBRepository extends CrudRepository<UserDB, ID> {
    @Query("select u from UserDB u where u.username = ?1")
    Optional<UserDB> findByUsername(String username);

}
