package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends CRUDService<ID, User, UserRepository> {

    public UserService(UserRepository userRepository) {
        super(userRepository);
    }
}
