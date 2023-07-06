package fr.croixrouge.service;

import fr.croixrouge.domain.model.*;
import fr.croixrouge.domain.repository.RoleRepository;
import fr.croixrouge.exposition.dto.core.RoleCreationRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleService extends CRUDService<ID, Role, RoleRepository> {

    private final UserService userService;

    private final LocalUnitService localUnitService;

    public RoleService(RoleRepository roleRepository, UserService userService, LocalUnitService localUnitService) {
        super(roleRepository);
        this.userService = userService;
        this.localUnitService = localUnitService;
    }

    public List<Role> getRoleByLocalUnitId(ID localUnitId) {
        localUnitService.findById(localUnitId);
        return repository.findAllByLocalUnitId(localUnitId);
    }

    public Role getCommonRole(String name) {
        return repository.findCommonRole(name).orElseThrow();
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
        final Map<Resources, Set<Operations>> auths = new HashMap<>();
        if (roleCreationRequest.getAuthorizations() != null) {
            for (String resource : roleCreationRequest.getAuthorizations().keySet()) {
                auths.put(Resources.fromName(resource), roleCreationRequest.getAuthorizations().get(resource).stream().map(Operations::fromName).collect(Collectors.toSet()));
            }
        }

        Role newRole = new Role(id,
                roleCreationRequest.getName() == null ? role.getName() : roleCreationRequest.getName(),
                roleCreationRequest.getDescription() == null ? role.getDescription() : roleCreationRequest.getDescription(),
                roleCreationRequest.getAuthorizations() == null ? role.getAuthorizations() : auths,
                role.getLocalUnit(),
                role.getUserIds() );

        save(newRole);
    }

    public void removeRole(ID roleId,ID userId) {
        userService.removeRole(userId, findById(roleId) );
    }

    public void addRole(ID roleId,ID userId) {
        userService.addRole(userId, findById(roleId) );
    }

    public List<Role> getUserRole(ID userId) {
        return userService.findById(userId).getRoles();
    }

    public List<User> getAllByRole(ID roleID) {
        return userService.findByRole(findById(roleID));
    }

    public Map<Resources, Set<Operations>> getUserAuthorizations(ID roleId) {
        List<Map<Resources, Set<Operations>>> authList = getUserRole(roleId).stream().map(Role::getAuthorizations).toList();
        Map<Resources, Set<Operations>> auths = new HashMap<>();
        for (Map<Resources, Set<Operations>> auth : authList) {
            for (Resources resource : auth.keySet()) {
                if (auths.containsKey(resource)) {
                    auths.get(resource).addAll(auth.get(resource));
                } else {
                    auths.put(resource, auth.get(resource));
                }
            }
        }
        return auths;
    }
}
