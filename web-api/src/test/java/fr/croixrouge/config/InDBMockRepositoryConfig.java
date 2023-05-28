package fr.croixrouge.config;

import fr.croixrouge.domain.model.*;
import fr.croixrouge.domain.repository.UserRepository;
import fr.croixrouge.repository.db.user.InDBUserRepository;
import fr.croixrouge.repository.db.user.UserDBRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@TestConfiguration
public class InDBMockRepositoryConfig {
    private final PasswordEncoder passwordEncoder;

    private final User managerUser;
    private final Address address = new Address(Department.getDepartmentFromPostalCode("91"), "91240", "St Michel sur Orge", "76 rue des Liers");
    private final LocalUnit localUnit;

    public InDBMockRepositoryConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;

        managerUser = new User(new ID(2L), "LUManager", passwordEncoder.encode("LUPassword"), List.of("ROLE_ADMIN"));

        localUnit = new LocalUnit(new ID(1L),
                "Unite Local du Val d'Orge",
                address,
                managerUser,
                address.getPostalCode() + "-000");
    }

    @Bean
    @Primary
    public UserRepository userTestRepository(UserDBRepository userDBRepository) {
        ID defaultUserId = new ID(1L);
        String defaultUsername = "defaultUser";
        String defaultPassword = passwordEncoder.encode("defaultPassword");
        User defaultUser = new User(defaultUserId, defaultUsername, defaultPassword, List.of());

        InDBUserRepository inDBUserRepository = new InDBUserRepository(userDBRepository);

        inDBUserRepository.save(defaultUser);
        inDBUserRepository.save(managerUser);

        return inDBUserRepository;
    }
}
