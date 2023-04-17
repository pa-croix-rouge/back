package fr.croixrouge.config;

import fr.croixrouge.domain.model.User;
import fr.croixrouge.domain.repository.LocalUnitRepository;
import fr.croixrouge.domain.repository.RoleRepository;
import fr.croixrouge.domain.repository.UserRepository;
import fr.croixrouge.repository.InMemoryLocalUnitRepository;
import fr.croixrouge.repository.InMemoryRoleRepository;
import fr.croixrouge.repository.InMemoryUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RepositoryConfig {

    private final PasswordEncoder passwordEncoder;

    public RepositoryConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public UserRepository userRepository(){
        ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
        String defaultUserId = "1";
        String defaultUsername = "defaultUser";
        String defaultPassword = passwordEncoder.encode("defaultPassword");
        User defaultUser = new User(defaultUserId, defaultUsername, defaultPassword, List.of());
        users.put(defaultUserId, defaultUser);
        return new InMemoryUserRepository(users);
    }

    @Bean
    public RoleRepository roleRepository(){
        return new InMemoryRoleRepository();
    }

    @Bean
    public LocalUnitRepository localUnitRepository() {
        return new InMemoryLocalUnitRepository();
    }

}
