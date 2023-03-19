package fr.croixrouge.domain.repository;

import fr.croixrouge.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByUsername(String username);

    void save(User user);

}
