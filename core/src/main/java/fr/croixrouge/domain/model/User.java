package fr.croixrouge.domain.model;

import java.util.List;

public class User extends Entity<ID> {

    protected final String username;
    protected final String password;

    protected final List<Role> roles;

    public User(ID userId, String username, String password, List<Role> roles) {
        super(userId);
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Role> getRoles() {
        return roles;
    }
}
