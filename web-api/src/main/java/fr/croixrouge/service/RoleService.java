package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Operations;
import fr.croixrouge.domain.model.Resources;
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

    public List<Role> getRoleByLocalUnitId(ID localUnitId) {
        return roleRepository.findAllByLocalUnitId(localUnitId);
    }

    public boolean isUserIdAuthorizedToAccessRoute(ID userId, Resources route, Operations operation) {
        List<Role> roles = roleRepository.findAllByUserId(userId);
        for (Role role : roles) {
            if (role.canAccessResource(route, operation)) {
                return true;
            }
        }
        return false;
    }
}
