package fr.croixrouge.repository.db.user;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.domain.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class InDBUserRepository implements UserRepository {

    private final UserDBRepository userDBRepository;

    public InDBUserRepository(UserDBRepository userDBRepository) {
        this.userDBRepository = userDBRepository;
    }

    private User toUser(UserDB userDB) {
        return new User(
                new ID(userDB.getUserID()),
                userDB.getUsername(),
                userDB.getPassword(),
                List.of()
        );
    }

    private UserDB toUserDB(User user) {
        return new UserDB(
                user.getId(),
                user.getUsername(),
                user.getPassword()
        );
    }

    @Override
    public Optional<User> findById(ID id) {
        return userDBRepository.findById(id).map(this::toUser);
    }

    @Override
    public ID save(User user) {
        UserDB userDB = userDBRepository.save(toUserDB(user));
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
}
