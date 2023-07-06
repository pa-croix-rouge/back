package fr.croixrouge.service;

import fr.croixrouge.domain.model.User;
import org.springframework.stereotype.Service;

@Service
public class ValidateAccountService {

    final private UserService userService;

    ValidateAccountService(UserService userService) {
        this.userService = userService;
    }

    public boolean validateEmail(String token) {
        var user = userService.findByToken(token);
        if (user == null) {
            return false;
        }
        var updatedUser = new User(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getLocalUnit(),
                user.getRoles(),
                true,
                null
        );
        userService.update(updatedUser);
        return true;
    }
}
