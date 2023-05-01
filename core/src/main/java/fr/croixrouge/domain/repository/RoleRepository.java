package fr.croixrouge.domain.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Role;

import java.util.List;

public interface RoleRepository extends CRUDRepository<ID, Role> {

    List<Role> findAllByLocalUnitId(ID localUnitId);
    List<Role> findAllByUserId(ID userId);
}
