package fr.croixrouge.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Role;
import fr.croixrouge.domain.repository.InMemoryCRUDRepository;
import fr.croixrouge.domain.repository.RoleRepository;
import fr.croixrouge.domain.repository.TimeStampIDGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class InMemoryRoleRepository extends InMemoryCRUDRepository<ID, Role> implements RoleRepository {

    public InMemoryRoleRepository(List<Role> roles) {
        super(roles, new TimeStampIDGenerator());
    }

    public InMemoryRoleRepository() {
        super(new ArrayList<>(), new TimeStampIDGenerator());
    }

    @Override
    public List<Role> findAllByLocalUnitId(ID localUnitId) {
        List<Role> rolesByLocalUnitId = new ArrayList<>();
        this.objects.stream()
                .filter(role -> role.getLocalUnit().getId().equals(localUnitId))
                .forEach(rolesByLocalUnitId::add);
        return rolesByLocalUnitId;
    }

    @Override
    public Optional<Role> findCommonRole(String name) {
        return Optional.empty();
    }

    @Override
    public List<Role> findAllByUserId(ID userId) {
        List<Role> rolesByUserId = new ArrayList<>();
//        this.objects.stream()
//                .filter(role -> role.getUserIds().contains(userId))
//                .forEach(rolesByUserId::add);
        return rolesByUserId;
    }
}
