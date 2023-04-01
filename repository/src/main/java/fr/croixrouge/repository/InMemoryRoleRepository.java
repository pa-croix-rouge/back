package fr.croixrouge.repository;

import fr.croixrouge.domain.model.Operations;
import fr.croixrouge.domain.model.Resources;
import fr.croixrouge.domain.model.Role;
import fr.croixrouge.domain.repository.RoleRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class InMemoryRoleRepository implements RoleRepository {

    private final ConcurrentHashMap<String, Role> roles;

    public InMemoryRoleRepository() {
        this.roles = new ConcurrentHashMap<>();
        initializeDefaultRoleForValDOrge();
    }

    private void initializeDefaultRoleForValDOrge() {
        String roleId = "1";
        String roleName = "Val d'Orge default role";
        String roleDescription = "Default role for Val d'Orge";
        Map<Resources, List<Operations>> resources = Map.of(Resources.RESOURCE, List.of(Operations.READ));

        String localUnitId = "1";
        List<String> userIds = Collections.singletonList("2");
        Role role = new Role(roleId, roleName, roleDescription, resources, localUnitId, userIds);
        roles.put(roleId, role);
    }

    @Override
    public Optional<Role> findById(String roleId) {
        return Optional.ofNullable(roles.get(roleId));
    }

    @Override
    public List<Role> findAllByLocalUnitId(String localUnitId) {
        List<Role> rolesByLocalUnitId = new ArrayList<>();
        this.roles.values().stream()
                .filter(role -> role.getLocalUnitId().equals(localUnitId))
                .forEach(rolesByLocalUnitId::add);
        return rolesByLocalUnitId;
    }

    @Override
    public List<Role> findAllByUserId(String userId) {
        List<Role> rolesByUserId = new ArrayList<>();
        this.roles.values().stream()
                .filter(role -> role.getUserIds().contains(userId))
                .forEach(rolesByUserId::add);
        return rolesByUserId;
    }

    @Override
    public void save(Role role) {
        roles.put(role.getId(), role);
    }
}
