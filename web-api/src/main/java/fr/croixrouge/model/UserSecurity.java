package fr.croixrouge.model;

import fr.croixrouge.domain.model.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class UserSecurity extends User implements UserDetails {

    public Map<Resources, Set<Operations>> allAuthorizations;

    public UserSecurity(ID userId, String username, String password, LocalUnit localUnit, List<Role> roles) {
        super(userId, username, password, localUnit, roles);
        allAuthorizations = roles.stream().flatMap(r -> r.getAuthorizations().entrySet().stream()).collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll);
    }

    public UserSecurity(User user) {
        this(user.getId(), user.getUsername(), user.getPassword(), user.getLocalUnit(), user.getRoles());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            for (Map.Entry<Resources, Set<Operations>> entry : role.getAuthorizations().entrySet()) {
                Resources resource = entry.getKey();
                for (Operations operation : entry.getValue()) {
                    authorities.add(new SimpleGrantedAuthority(resource.name() + "_" + operation.name()));
                }
            }
        }
        return authorities;
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
                "userId='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", authorities=" + getAuthorities() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSecurity userSecurity = (UserSecurity) o;
        return Objects.equals(id, userSecurity.id) && Objects.equals(username, userSecurity.username) && Objects.equals(password, userSecurity.password) && Objects.equals(roles, userSecurity.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, roles);
    }
}
