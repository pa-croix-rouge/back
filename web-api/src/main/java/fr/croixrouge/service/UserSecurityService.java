package fr.croixrouge.service;

import fr.croixrouge.domain.repository.UserRepository;
import fr.croixrouge.model.UserSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;


    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserSecurity loadUserByUsername(String userName) {
        UserSecurity user = userRepository.findByUsername(userName).map(UserSecurity::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userName));
        return user;
    }
}
