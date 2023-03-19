package fr.croixrouge.service;

import fr.croixrouge.domain.model.Role;
import fr.croixrouge.domain.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getRoleByLocalUnitId(String localUnitId) {
        return roleRepository.findAllByLocalUnitId(localUnitId);
    }

    public boolean isUserIdAuthorizedToAccessRoute(String userId, String route) {
        List<Role> roles = roleRepository.findAllByUserId(userId);
        for (Role role : roles) {
            if (role.getRoute().getPath().equalsIgnoreCase(route)) {
                return true;
            }
        }
        return false;
    }
}
