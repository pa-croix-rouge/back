package fr.croixrouge.domain.model;

import java.util.List;

public class User extends Entity<ID> {

    protected final String username;
    protected final String password;
    protected final LocalUnit localUnit;
    protected final List<Role> roles;

    public User(ID userId, String username, String password, LocalUnit localUnit, List<Role> roles) {
        super(userId);
        this.username = username;
        this.password = password;
        this.localUnit = localUnit;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public LocalUnit getLocalUnit() {
        return localUnit;
    }

    public List<Role> getRoles() {
        return roles;
    }
}
