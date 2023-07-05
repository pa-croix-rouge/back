package fr.croixrouge.repository.db.role;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Operations;
import fr.croixrouge.domain.model.Resources;
import fr.croixrouge.domain.model.Role;
import fr.croixrouge.domain.repository.RoleRepository;
import fr.croixrouge.repository.db.localunit.InDBLocalUnitRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class InDBRoleRepository implements RoleRepository {

    private final RoleDBRepository roleDBRepository;

    private final RoleResourceDBRepository roleResourceDBRepository;

    private final InDBLocalUnitRepository inDBLocalUnitRepository;

    public InDBRoleRepository(RoleDBRepository roleDBRepository, RoleResourceDBRepository roleResourceDBRepository, InDBLocalUnitRepository inDBLocalUnitRepository) {
        this.roleDBRepository = roleDBRepository;
        this.roleResourceDBRepository = roleResourceDBRepository;
        this.inDBLocalUnitRepository = inDBLocalUnitRepository;
    }

    public Role toRole(RoleDB roleDB) {

        Map<Resources, Set<Operations>> resourcesSetMap = new HashMap<>();
        for (var roleResourceDB : roleDB.getRoleResourceDBs()) {

            if (!resourcesSetMap.containsKey(roleResourceDB.getResources()))
                resourcesSetMap.put(roleResourceDB.getResources(), new HashSet<>());

            resourcesSetMap.get(roleResourceDB.getResources()).add(roleResourceDB.getOperations());
        }

        return new Role(
                new ID(roleDB.getRoleID()),
                roleDB.getName(),
                roleDB.getDescription(),
                resourcesSetMap,
                inDBLocalUnitRepository.toLocalUnit(roleDB.getLocalUnitDB()),
                null
        );
    }

    public RoleDB toRoleDB(Role role) {
        Set<RoleResourceDB> roleResourceDBs = new HashSet<>();
        for (var entry : role.getAuthorizations().entrySet()) {
            for (var operation : entry.getValue()) {
                roleResourceDBs.add(new RoleResourceDB(entry.getKey(), operation));
            }
        }

        return new RoleDB(
                role.getId() == null ? null : role.getId().value(),
                role.getName(),
                role.getDescription(),
                inDBLocalUnitRepository.toLocalUnitDB(role.getLocalUnit()),
                roleResourceDBs
        );

    }

    @Override
    public Optional<Role> findById(ID id) {
        return roleDBRepository.findById(id.value()).map(this::toRole);
    }

    @Override
    public ID save(Role object) {
        RoleDB roleDB = roleDBRepository.save(toRoleDB(object));
        object.setId(new ID(roleDB.getRoleID()));
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
        return Stream.of(roleDBRepository.findByLocalUnitDB_LocalUnitID(localUnitId.value())
                        , roleDBRepository.findByLocalUnitDB_LocalUnitID(null))
                .flatMap(Collection::stream)
                .map(this::toRole)
                .toList();
    }

    @Override
    public Optional<Role> findCommonRole(String name) {
        return roleDBRepository.findByLocalUnitDB_LocalUnitIDNullAndName(name).map(this::toRole);
    }

    @Override
    public List<Role> findAllByUserId(ID userId) {
        return null;
    }
}
