package fr.croixrouge.service;

import fr.croixrouge.domain.repository.UserRepository;
import fr.croixrouge.model.UserSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityService {

    private final UserRepository userRepository;

    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserSecurity loadUserByUsername(String userName) {
        return userRepository.findByUsername(userName).map(UserSecurity::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userName));
    }
}
