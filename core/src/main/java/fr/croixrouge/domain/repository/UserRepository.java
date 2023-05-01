package fr.croixrouge.domain.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(ID userId);

    Optional<User> findByUsername(String username);

    void save(User user);

}
