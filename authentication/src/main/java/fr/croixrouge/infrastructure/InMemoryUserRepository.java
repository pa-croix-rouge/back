package fr.croixrouge.infrastructure;

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
    }

    private void initializeDefaultUser() {
        String defaultUsername = "defaultUser";
        String defaultPassword = passwordEncoder.encode("defaultPassword");
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        User defaultUser = new User(defaultUsername, defaultPassword, authorities);
        users.put(defaultUsername, defaultUser);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    @Override
    public void save(User user) {
        users.put(user.getUsername(), user);
    }
}
