package fr.croixrouge.repository.db.role;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Role;
import fr.croixrouge.domain.repository.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class InDBRoleRepository implements RoleRepository {

    private final RoleDBRepository roleDBRepository;

    private final RoleResourceDBRepository roleResourceDBRepository;

    public InDBRoleRepository(RoleDBRepository roleDBRepository, RoleResourceDBRepository roleResourceDBRepository) {
        this.roleDBRepository = roleDBRepository;
        this.roleResourceDBRepository = roleResourceDBRepository;
    }

    private Role toRole(RoleDB roleDB) {
        return new Role(
                new ID(roleDB.getRoleID()),
                roleDB.getName(),
                roleDB.getDescription(),
                null,
                null,
                null
        );
    }

    private RoleDB toRoleDB(Role role) {
        return new RoleDB(
                role.getId().value(),
                role.getName(),
                role.getDescription()
        );
    }

    @Override
    public Optional<Role> findById(ID id) {
        return roleDBRepository.findById(id.value()).map(this::toRole);
    }

    @Override
    public ID save(Role object) {
        RoleDB roleDB = roleDBRepository.save(toRoleDB(object));
        return new ID(roleDB.getRoleID());
    }

    @Override
    public void delete(Role object) {
        roleDBRepository.delete(toRoleDB(object));
    }

    @Override
    public List<Role> findAll() {
        return StreamSupport.stream(roleDBRepository.findAll().spliterator(), false).map(this::toRole).toList();
    }

    @Override
    public List<Role> findAllByLocalUnitId(ID localUnitId) {
        return null;
    }

    @Override
    public List<Role> findAllByUserId(ID userId) {
        return null;
    }
}
