package fr.croixrouge.repository;

import fr.croixrouge.domain.model.User;
import fr.croixrouge.domain.repository.UserRepository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


public class InMemoryUserRepository implements UserRepository {

    private final ConcurrentHashMap<String, User> users;

    public InMemoryUserRepository(ConcurrentHashMap<String, User> users) {
        this.users = users;
    }

    public InMemoryUserRepository() {
        this.users = new ConcurrentHashMap<>();
    }

    @Override
    public Optional<User> findById(String userId) {
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
        users.put(user.getUserId(), user);
    }
}
