package fr.croixrouge;

import fr.croixrouge.domain.model.User;
import fr.croixrouge.domain.repository.UserRepository;
import fr.croixrouge.repository.InMemoryUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
public class ApplicationTest {

    @Test
    @DisplayName("Test that the application context loads successfully.")
    void contextLoads() {
        // This test will pass if the application context loads successfully.
    }

    @Bean
    public UserRepository userRepository(PasswordEncoder passwordEncoder){
        ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
        String defaultUserId = "1";
        String defaultUsername = "defaultUser";
        String defaultPassword = passwordEncoder.encode( "defaultPassword");
        User defaultUser = new User(defaultUserId, defaultUsername, defaultPassword, List.of("ROLE_ADMIN"));
        users.put(defaultUserId, defaultUser);

        String localUnitManagerUserId = "2";
        String localUnitManagerUsername = "LUManager";
        String localUnitManagerPassword = passwordEncoder.encode("LUPassword");
        User localUnitManager = new User(localUnitManagerUserId, localUnitManagerUsername, localUnitManagerPassword, List.of());
        users.put(localUnitManagerUserId, localUnitManager);

        return new InMemoryUserRepository(users);
    }
}
