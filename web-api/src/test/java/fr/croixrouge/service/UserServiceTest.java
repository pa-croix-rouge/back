package fr.croixrouge.service;

import fr.croixrouge.config.InDBMockRepositoryConfig;
import fr.croixrouge.domain.model.ID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@AutoConfigureMockMvc
@Import(InDBMockRepositoryConfig.class)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void should_get_user_by_id() {
        assertDoesNotThrow(() -> {
            userService.findById(new ID(1L));
        });
    }

    @Test
    void should_get_user_by_name() {
        assertDoesNotThrow(() -> {
            userService.findByUsername("defaultUser");
        });
    }

}