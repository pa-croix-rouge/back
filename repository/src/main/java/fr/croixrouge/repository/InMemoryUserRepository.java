package fr.croixrouge.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Role;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.domain.repository.InMemoryCRUDRepository;
import fr.croixrouge.domain.repository.TimeStampIDGenerator;
import fr.croixrouge.domain.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryUserRepository extends InMemoryCRUDRepository<ID, User> implements UserRepository {

    public InMemoryUserRepository(ArrayList<User> users) {
        super(users, new TimeStampIDGenerator());
    }

    public InMemoryUserRepository() {
        super(new ArrayList<>(), new TimeStampIDGenerator());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return objects.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public List<User> findAllByRole(Role role) {
        return null;
    }

    @Override
    public Optional<User> findByToken(String token) {
        return Optional.empty();
    }
}
