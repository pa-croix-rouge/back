package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Operations;
import fr.croixrouge.domain.model.Resources;
import fr.croixrouge.domain.model.Role;
import fr.croixrouge.domain.repository.RoleRepository;
import fr.croixrouge.exposition.dto.core.RoleCreationRequest;
import fr.croixrouge.model.UserSecurity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService extends CRUDService<ID, Role, RoleRepository> {

    private final UserService userService;

    public RoleService(RoleRepository roleRepository, UserService userService) {
        super(roleRepository);
        this.userService = userService;
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

    @Override
    public void delete(Role object) {
        userService.removeRoleFromAllUsers(object);
        super.delete(object);
    }

    public void updateRole(ID id, RoleCreationRequest roleCreationRequest) {
        Role role = findById(id);

        Role newRole = new Role(id,
                roleCreationRequest.getName() == null ? role.getName() : roleCreationRequest.getName(),
                roleCreationRequest.getDescription() == null ? role.getDescription() : roleCreationRequest.getDescription(),
                roleCreationRequest.getAuthorizations() == null ? role.getAuthorizations() : roleCreationRequest.getAuthorizations(),
                role.getLocalUnit(),
                role.getUserIds() );

        save(newRole);
    }
}
