package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Role;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends CRUDService<ID, User, UserRepository> {


    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.passwordEncoder = passwordEncoder;
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username).orElse(null);
    }


    @Override
    public ID save(User object) {
        return super.save(object.setPassword(passwordEncoder.encode(object.getPassword())));
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
