package fr.croixrouge.config;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.domain.repository.LocalUnitRepository;
import fr.croixrouge.domain.repository.RoleRepository;
import fr.croixrouge.domain.repository.UserRepository;
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
@Profile("prod")
public class RepositoryConfig {

    @Bean
    @Primary
    public InDBLocalUnitRepository localTestInDBUnitRepository(LocalUnitDBRepository localUnitDBRepository) {
        return new InDBLocalUnitRepository(localUnitDBRepository);
    }

    @Bean
    @Primary
    public InDBRoleRepository roleTestRepository(RoleDBRepository roleDBRepository, RoleResourceDBRepository roleResourceDBRepository, InDBLocalUnitRepository localUnitDBRepository) {
        return new InDBRoleRepository(roleDBRepository, roleResourceDBRepository, localUnitDBRepository);
    }

    @Bean
    @Primary
    public InDBUserRepository userTestRepository(UserDBRepository userDBRepository, InDBRoleRepository roleDBRepository) {
        return new InDBUserRepository(userDBRepository, roleDBRepository);
    }

    @Bean
    @Primary
    public InDBVolunteerRepository volunteerTestRepository(VolunteerDBRepository volunteerDBRepository, InDBUserRepository inDBUserRepository, InDBLocalUnitRepository inDBLocalUnitRepository) {
        return new InDBVolunteerRepository(volunteerDBRepository, inDBUserRepository, inDBLocalUnitRepository);
    }

    @Bean
    @Primary
    public EventRepository eventTestRepository(EventDBRepository eventDBRepository, EventSessionDBRepository eventSessionDBRepository, EventTimeWindowDBRepository eventTimeWindowDBRepository, InDBUserRepository userDBRepository, InDBVolunteerRepository inDBVolunteerRepository, InDBLocalUnitRepository inDBLocalUnitRepository) {
        return new InDBEventRepository(eventDBRepository, eventSessionDBRepository, eventTimeWindowDBRepository, userDBRepository, inDBVolunteerRepository, inDBLocalUnitRepository);
    }

    @Bean
    public FoodProductRepository foodProductRepository() {
        return new InMemoryFoodProductRepository();
    }

    @Bean
    @Primary
    public InDBProductRepository productTestRepository(ProductDBRepository productDBRepository, FoodProductDBRepository foodProductDBRepository, ProductLimitDBRepository productLimitDBRepository) {
        return new InDBProductRepository(productDBRepository, foodProductDBRepository, productLimitDBRepository);
    }

    @Bean
    @Primary
    public InDBStorageRepository storageTestRepository(StorageDBRepository storageDBRepository, InDBLocalUnitRepository inDBLocalUnitRepository) {
        return new InDBStorageRepository(storageDBRepository, inDBLocalUnitRepository);
    }

    @Bean
    public UserProductRepository storageUserProductRepository(UserProductDBRepository userProductDBRepository, InDBUserRepository userRepository, InDBProductRepository productRepository, InDBStorageRepository storageRepository) {
        return new InDBUserProductRepository(userProductDBRepository, userRepository, productRepository, storageRepository);
    }

    @Bean
    public StorageProductRepository storageProductRepository(StorageProductDBRepository storageProductDBRepository, InDBProductRepository productRepository, InDBStorageRepository storageRepository) {
        return new InDBStorageProductRepository(storageProductDBRepository, productRepository, storageRepository);
    }

    @Bean
    public StorageProductService storageProductServiceCore(StorageProductRepository storageProductRepository) {
        return new StorageProductService(storageProductRepository);
    }
}
