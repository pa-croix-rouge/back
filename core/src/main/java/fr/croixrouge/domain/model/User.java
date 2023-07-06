package fr.croixrouge.domain.model;

import java.util.ArrayList;
import java.util.List;

public class User extends Entity<ID> {

    protected final String username;
    protected final String password;
    protected final LocalUnit localUnit;
    protected final List<Role> roles;
    protected final boolean emailValidated;
    protected final String tokenToValidateEmail;

    public User(ID userId, String username, String password, LocalUnit localUnit, List<Role> roles, boolean emailValidated, String tokenToValidateEmail) {
        super(userId);
        this.username = username;
        this.password = password;
        this.localUnit = localUnit;
        this.roles = roles;
        this.emailValidated = emailValidated;
        this.tokenToValidateEmail = tokenToValidateEmail;
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

    public boolean isEmailValidated() {
        return emailValidated;
    }

    public String getTokenToValidateEmail() {
        return tokenToValidateEmail;
    }

    public User removeRole(Role role) {
        return new User(id, username, password, localUnit, roles.stream().filter(r -> !r.equals(role)).toList(), emailValidated, tokenToValidateEmail);
    }

    public User addRole(Role role) {
        if (roles.contains(role)) {
            return this;
        }
        var newRoles = new ArrayList<>(roles);
        newRoles.add(role);
        return new User(id, username, password, localUnit, newRoles, emailValidated, tokenToValidateEmail);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", localUnit=" + localUnit +
                ", roles=" + roles +
                ", id=" + id +
                '}';
    }

    public User setPassword(String encode) {
        return new User(id, username, encode, localUnit, roles, emailValidated, tokenToValidateEmail);
    }
}
