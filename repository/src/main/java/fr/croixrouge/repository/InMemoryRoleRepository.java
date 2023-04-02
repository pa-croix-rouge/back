package fr.croixrouge.repository;

import fr.croixrouge.domain.model.Role;
import fr.croixrouge.domain.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


public class InMemoryRoleRepository implements RoleRepository {

    private final ConcurrentHashMap<String, Role> roles;

    public InMemoryRoleRepository(ConcurrentHashMap<String, Role> roles) {
        this.roles = roles;
    }

    public InMemoryRoleRepository() {
        this.roles = new ConcurrentHashMap<>();
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
