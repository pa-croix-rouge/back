package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Operations;
import fr.croixrouge.domain.model.Resources;
import fr.croixrouge.domain.model.Role;
import fr.croixrouge.domain.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService extends CRUDService<ID, Role, RoleRepository> {

    public RoleService(RoleRepository roleRepository) {
        super(roleRepository);
    }

    public List<Role> getRoleByLocalUnitId(ID localUnitId) {
        return repository.findAllByLocalUnitId(localUnitId);
    }

    public boolean isUserIdAuthorizedToAccessRoute(ID userId, Resources route, Operations operation) {
        List<Role> roles = repository.findAllByUserId(userId);
        for (Role role : roles) {
            if (role.canAccessResource(route, operation)) {
                return true;
            }
        }
        return false;
    }
}
