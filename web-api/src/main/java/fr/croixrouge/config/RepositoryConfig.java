package fr.croixrouge.config;

import fr.croixrouge.domain.repository.LocalUnitRepository;
import fr.croixrouge.domain.repository.RoleRepository;
import fr.croixrouge.domain.repository.UserRepository;
import fr.croixrouge.repository.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public UserRepository userRepository(){
        return new InMemoryUserRepository();
    }

    @Bean
    public RoleRepository roleRepository(){
        return new InMemoryRoleRepository();
    }

    @Bean
    public LocalUnitRepository localUnitRepository() {
        return new InMemoryLocalUnitRepository();
    }

    @Bean
    public EventRepository eventRepository() {
        return new InMemoryEventRepository();
    }

}
