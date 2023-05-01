package fr.croixrouge.model;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserSecurity implements UserDetails {
    private final ID userId;
    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public UserSecurity(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = user.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public ID getUserId() {
        return userId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", authorities=" + authorities +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSecurity userSecurity = (UserSecurity) o;
        return Objects.equals(userId, userSecurity.userId) && Objects.equals(username, userSecurity.username) && Objects.equals(password, userSecurity.password) && Objects.equals(authorities, userSecurity.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, authorities);
    }
}
