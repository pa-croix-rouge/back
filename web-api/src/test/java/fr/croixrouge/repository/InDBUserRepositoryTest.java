package fr.croixrouge.repository;


import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.domain.repository.UserRepository;
import fr.croixrouge.repository.db.user.InDBUserRepository;
import fr.croixrouge.repository.db.user.UserDBRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class InDBUserRepositoryTest {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public InDBUserRepositoryTest(UserDBRepository userDBRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;

        userRepository = new InDBUserRepository(userDBRepository);
    }

    @Test
    @DisplayName("Test that the login endpoint returns a JWT token when the credentials are correct.")
    public void saveUser() throws Exception {
        ID defaultUserId = new ID(1L);
        String defaultUsername = "defaultUser";
        String defaultPassword = passwordEncoder.encode("defaultPassword");
        User defaultUser = new User(defaultUserId, defaultUsername, defaultPassword, List.of());

        userRepository.save(defaultUser);
    }

}
