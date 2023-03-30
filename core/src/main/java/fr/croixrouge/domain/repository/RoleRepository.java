package fr.croixrouge.domain.repository;

import fr.croixrouge.domain.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {

    Optional<Role> findById(String roleId);
    List<Role> findAllByLocalUnitId(String localUnitId);
    List<Role> findAllByUserId(String userId);
    void save(Role role);
}
