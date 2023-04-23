package fr.croixrouge.config;

import fr.croixrouge.domain.model.*;
import fr.croixrouge.domain.repository.LocalUnitRepository;
import fr.croixrouge.domain.repository.RoleRepository;
import fr.croixrouge.domain.repository.UserRepository;
import fr.croixrouge.repository.InMemoryLocalUnitRepository;
import fr.croixrouge.repository.InMemoryRoleRepository;
import fr.croixrouge.repository.InMemoryUserRepository;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.model.quantifier.VolumeQuantifier;
import fr.croixrouge.storage.model.quantifier.VolumeUnit;
import fr.croixrouge.storage.model.quantifier.WeightQuantifier;
import fr.croixrouge.storage.model.quantifier.WeightUnit;
import fr.croixrouge.storage.repository.ProductRepository;
import fr.croixrouge.storage.repository.StorageRepository;
import fr.croixrouge.storage.repository.memory.InMemoryProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryStorageRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@TestConfiguration
public class MockRepositoryConfig {

    private final PasswordEncoder passwordEncoder;

    private final User mangerUser;
    private final Address address = new Address(Department.getDepartmentFromPostalCode("91"), "91240", "St Michel sur Orge", "76 rue des Liers");
    private final LocalUnit localUnit;

    public MockRepositoryConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        mangerUser = new User("2", "LUManager", passwordEncoder.encode("LUPassword"), List.of("ROLE_ADMIN"));

        localUnit = new LocalUnit("1",
                "Unite Local du Val d'Orge",
                address,
                mangerUser);
    }

    @Bean
    @Primary
    public UserRepository userTestRepository() {
        ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
        String defaultUserId = "1";
        String defaultUsername = "defaultUser";
        String defaultPassword = passwordEncoder.encode("defaultPassword");
        User defaultUser = new User(defaultUserId, defaultUsername, defaultPassword, List.of());
        users.put(defaultUserId, defaultUser);

        users.put(mangerUser.getUserId(), mangerUser);

        return new InMemoryUserRepository(users);
    }

    @Bean
    @Primary
    public LocalUnitRepository localTestUnitRepository() {
        ConcurrentHashMap<String, LocalUnit> localUnits = new ConcurrentHashMap<>();
        localUnits.put("1", localUnit);
        return new InMemoryLocalUnitRepository(localUnits);
    }

    @Bean
    @Primary
    public RoleRepository roleTestRepository() {
        ConcurrentHashMap<String, Role> roles = new ConcurrentHashMap<>();
        String roleId = "1";
        String roleName = "Val d'Orge default role";
        String roleDescription = "Default role for Val d'Orge";
        Map<Resources, List<Operations>> resources = Map.of(Resources.RESOURCE, List.of(Operations.READ));

        String localUnitId = "1";
        List<String> userIds = Collections.singletonList("2");
        Role role = new Role(roleId, roleName, roleDescription, resources, localUnitId, userIds);
        roles.put(roleId, role);

        return new InMemoryRoleRepository(roles);
    }

    @Bean
    @Primary
    public ProductRepository productTestRepository() {
        List<Product> products = new ArrayList<>();

        products.add(new Product(new ID("1"), "Product 1", new WeightQuantifier(1, WeightUnit.KILOGRAM), null));
        products.add(new Product(new ID("2"), "Product 2", new VolumeQuantifier(1, VolumeUnit.LITER), null));

        return new InMemoryProductRepository(products);
    }

    @Bean
    @Primary
    public StorageRepository storageTestRepository() {
        List<Storage> storages = new ArrayList<>();

        storages.add(new Storage(new ID("1"), localUnit, address, null, null));
        storages.add(new Storage(new ID("2"), localUnit, address, null, null));

        return new InMemoryStorageRepository(storages);
    }

}
