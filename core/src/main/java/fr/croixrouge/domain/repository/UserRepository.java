package fr.croixrouge.domain.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.User;

import java.util.Optional;

public interface UserRepository extends CRUDRepository<ID, User> {

    Optional<User> findByUsername(String username);

}
