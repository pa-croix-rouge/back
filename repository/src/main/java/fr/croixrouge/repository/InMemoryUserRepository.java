package fr.croixrouge.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.domain.repository.UserRepository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


public class InMemoryUserRepository implements UserRepository {

    private final ConcurrentHashMap<ID, User> users;

    public InMemoryUserRepository(ConcurrentHashMap<ID, User> users) {
        this.users = users;
    }

    public InMemoryUserRepository() {
        this(new ConcurrentHashMap<>());
    }

    @Override
    public Optional<User> findById(ID userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }
}
