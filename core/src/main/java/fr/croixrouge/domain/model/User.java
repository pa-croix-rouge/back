package fr.croixrouge.domain.model;

import java.util.List;

public class User {
    private final String userId;

    private final String username;
    private final String password;

    private final List<String> authorities;

    public User(String userId, String username, String password, List<String> authorities) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public String getUserId() {
        return userId;
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
