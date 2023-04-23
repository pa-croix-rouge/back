package fr.croixrouge.config;

import fr.croixrouge.domain.repository.LocalUnitRepository;
import fr.croixrouge.domain.repository.RoleRepository;
import fr.croixrouge.domain.repository.UserRepository;
import fr.croixrouge.repository.InMemoryLocalUnitRepository;
import fr.croixrouge.repository.InMemoryRoleRepository;
import fr.croixrouge.repository.InMemoryUserRepository;
import fr.croixrouge.storage.repository.ProductRepository;
import fr.croixrouge.storage.repository.StorageRepository;
import fr.croixrouge.storage.repository.memory.InMemoryProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryStorageRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public UserRepository userRepository(){
        return new InMemoryUserRepository();
    }

    @Bean
    public RoleRepository roleRepository() {
        return new InMemoryRoleRepository();
    }

    @Bean
    public LocalUnitRepository localUnitRepository() {
        return new InMemoryLocalUnitRepository();
    }

    @Bean
    public ProductRepository productRepository() {
        return new InMemoryProductRepository();
    }

    @Bean
    public StorageRepository storageRepository() {
        return new InMemoryStorageRepository();
    }

}
