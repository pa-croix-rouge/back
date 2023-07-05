package fr.croixrouge.repository.db.user;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Role;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.domain.repository.UserRepository;
import fr.croixrouge.repository.db.localunit.InDBLocalUnitRepository;
import fr.croixrouge.repository.db.role.InDBRoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class InDBUserRepository implements UserRepository {

    private final UserDBRepository userDBRepository;

    private final InDBRoleRepository roleDBRepository;

    private final InDBLocalUnitRepository localUnitDBRepository;

    public InDBUserRepository(UserDBRepository userDBRepository, InDBRoleRepository roleDBRepository, InDBLocalUnitRepository localUnitDBRepository) {
        this.userDBRepository = userDBRepository;
        this.roleDBRepository = roleDBRepository;
        this.localUnitDBRepository = localUnitDBRepository;
    }

    public User toUser(UserDB userDB) {
        return new User(
                new ID(userDB.getUserID()),
                userDB.getUsername(),
                userDB.getPassword(),
                localUnitDBRepository.toLocalUnit(userDB.getLocalUnitDB()),
                userDB.getRoleDBs().stream().map(roleDBRepository::toRole).toList()
        );
    }

    public UserDB toUserDB(User user) {
        return new UserDB(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                localUnitDBRepository.toLocalUnitDB(user.getLocalUnit()),
                user.getRoles().stream().map(roleDBRepository::toRoleDB).collect(Collectors.toSet())
        );

    }

    @Override
    public Optional<User> findById(ID id) {
        return userDBRepository.findById(id.value()).map(this::toUser);
    }

    @Override
    public ID save(User user) {
        UserDB userDB = userDBRepository.save(toUserDB(user));
        user.setId(new ID(userDB.getUserID()));
        return new ID(userDB.getUserID());
    }

    @Override
    public void delete(User user) {
        userDBRepository.delete(toUserDB(user));
    }

    @Override
    public List<User> findAll() {
        return StreamSupport.stream(userDBRepository.findAll().spliterator(), false).map(this::toUser).toList();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDBRepository.findByUsername(username).map(this::toUser);
    }

    @Override
    public List<User> findAllByRole(Role role) {
        return userDBRepository.findByRoleDBs_RoleID(role.getId().value()).stream().map(this::toUser).toList();
    }
}
