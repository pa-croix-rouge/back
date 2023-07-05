package fr.croixrouge.domain.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends CRUDRepository<ID, Role> {

    List<Role> findAllByLocalUnitId(ID localUnitId);

    Optional<Role> findCommonRole( String name);

    List<Role> findAllByUserId(ID userId);
}
