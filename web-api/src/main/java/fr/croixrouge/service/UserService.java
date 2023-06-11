package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Role;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends CRUDService<ID, User, UserRepository> {

    public UserService(UserRepository userRepository) {
        super(userRepository);
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username).orElse(null);
    }


    public void removeRoleFromAllUsers(Role role) {
        var users = repository.findAll();

        for (User user : users) {
            save(user.removeRole(role));
        }
    }

    public void removeRole(ID id, Role role) {
        User user = findById(id);
        save(user.removeRole(role));
    }

    public void addRole(ID id, Role role) {
        User user = findById(id);
        save(user.addRole(role));
    }

}
