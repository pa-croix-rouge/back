package fr.croixrouge.config;

import fr.croixrouge.domain.model.*;
import fr.croixrouge.domain.repository.LocalUnitRepository;
import fr.croixrouge.domain.repository.RoleRepository;
import fr.croixrouge.domain.repository.UserRepository;
import fr.croixrouge.domain.repository.VolunteerRepository;
import fr.croixrouge.repository.*;
import fr.croixrouge.storage.repository.ProductRepository;
import fr.croixrouge.storage.repository.StorageProductRepository;
import fr.croixrouge.storage.repository.StorageRepository;
import fr.croixrouge.storage.repository.UserProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryStorageProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryStorageRepository;
import fr.croixrouge.storage.repository.memory.InMemoryUserProductRepository;
import fr.croixrouge.storage.service.StorageProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RepositoryConfig {

    private final PasswordEncoder passwordEncoder;

    private final User managerUser;

    private final Address address = new Address(Department.getDepartmentFromPostalCode("91"), "91240", "St Michel sur Orge", "76 rue des Liers");

    private final LocalUnit localUnit;

    public RepositoryConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.managerUser = new User(new ID("2"), "LUManager", passwordEncoder.encode("LUPassword"), List.of("ROLE_ADMIN"));
        this.localUnit = new LocalUnit(new ID("1"), "Unite Local du Val d'Orge", address, managerUser, address.getPostalCode() + "-000");
    }

    @Bean
    public UserRepository userRepository(){
        ArrayList<User> users = new ArrayList<>();
        ID defaultUserId = new ID("1");
        String defaultUsername = "defaultUser";
        String defaultPassword = passwordEncoder.encode("defaultPassword");
        User defaultUser = new User(defaultUserId, defaultUsername, defaultPassword, List.of());
        users.add(defaultUser);
        users.add(managerUser);
        return new InMemoryUserRepository(users);
    }

    @Bean
    public RoleRepository roleRepository() {
        return new InMemoryRoleRepository();
    }

    @Bean
    public LocalUnitRepository localUnitRepository() {
        ArrayList<LocalUnit> localUnits = new ArrayList<>();
        localUnits.add(localUnit);
        return new InMemoryLocalUnitRepository(localUnits);
    }

    @Bean
    public ProductRepository productRepository() {
        return new InMemoryProductRepository();
    }

    @Bean
    public StorageRepository storageRepository() {
        return new InMemoryStorageRepository();
    }

    @Bean
    public UserProductRepository storageUserProductRepository() {
        return new InMemoryUserProductRepository();
    }

    @Bean
    public StorageProductRepository storageProductRepository() {
        return new InMemoryStorageProductRepository();
    }

    @Bean
    public StorageProductService storageProductServiceCore(StorageProductRepository storageProductRepository) {
        return new StorageProductService(storageProductRepository);
    }

    @Bean
    public EventRepository eventRepository() {
        return new InMemoryEventRepository();
    }

    @Bean
    public VolunteerRepository volunteerRepository() {
        ArrayList<Volunteer> volunteers = new ArrayList<>();
        Volunteer volunteer1 = new Volunteer(new ID("1"), managerUser, "volunteerFirstName", "volunteerLastName", "+33 6 00 00 00 00", true, localUnit.getId());
        volunteers.add(volunteer1);
        return new InMemoryVolunteerRepository(volunteers);
    }
}
