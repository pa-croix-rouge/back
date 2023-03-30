package fr.croixrouge.repository;

import fr.croixrouge.domain.model.User;
import fr.croixrouge.domain.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final ConcurrentHashMap<String, User> users;
    private final PasswordEncoder passwordEncoder;

    public InMemoryUserRepository(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.users = new ConcurrentHashMap<>();
        initializeDefaultUser();
        initializeLocalUnitManager();
    }

    private void initializeDefaultUser() {
        String defaultUserId = "1";
        String defaultUsername = "defaultUser";
        String defaultPassword = passwordEncoder.encode("defaultPassword");
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
        User defaultUser = new User(defaultUserId, defaultUsername, defaultPassword, authorities);
        users.put(defaultUserId, defaultUser);
    }

    private void initializeLocalUnitManager() {
        String localUnitManagerUserId = "2";
        String localUnitManagerUsername = "LUManager";
        String localUnitManagerPassword = passwordEncoder.encode("LUPassword");
        Collection<GrantedAuthority> authorities = Collections.EMPTY_SET;
        User localUnitManager = new User(localUnitManagerUserId, localUnitManagerUsername, localUnitManagerPassword, authorities);
        users.put(localUnitManagerUserId, localUnitManager);
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
