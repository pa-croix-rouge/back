package fr.croixrouge.domain.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {

    Optional<Role> findById(ID roleId);
    List<Role> findAllByLocalUnitId(ID localUnitId);
    List<Role> findAllByUserId(ID userId);
    void save(Role role);
}
