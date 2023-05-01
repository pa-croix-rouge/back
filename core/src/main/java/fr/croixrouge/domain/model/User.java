package fr.croixrouge.domain.model;

import java.util.List;

public class User extends Entity<ID> {

    private final String username;
    private final String password;

    private final List<String> authorities;

    public User(ID userId, String username, String password, List<String> authorities) {
        super(userId);
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getAuthorities() {
        return authorities;
    }
}
