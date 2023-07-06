package fr.croixrouge.repository.db.user;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.repository.db.localunit.LocalUnitDB;
import fr.croixrouge.repository.db.role.RoleDB;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Table(name = "user")
@Entity
public class UserDB {
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roledbs",
            joinColumns = @JoinColumn(name = "userdb_user_id"),
            inverseJoinColumns = @JoinColumn(name = "roledbs_role_id"))
    private Set<RoleDB> roleDBs = new LinkedHashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userID;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email_validated", nullable = false)
    private boolean emailValidated;

    @ManyToOne(optional = false)
    @JoinColumn(name = "local_unit_db_localunit_id", nullable = false)
    private LocalUnitDB localUnitDB;

    public Set<RoleDB> getRoleDBs() {
        return roleDBs;
    }

    public UserDB(ID id, String username, String password, LocalUnitDB localUnitDB, Set<RoleDB> roleDBs, boolean emailValidated) {
        if (id != null) {
            this.userID = id.value();
        } else {
            this.userID = null;
        }
        this.username = username;
        this.password = password;
        this.localUnitDB = localUnitDB;
        this.roleDBs = roleDBs;
        this.emailValidated = emailValidated;
    }

    public UserDB() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalUnitDB getLocalUnitDB() {
        return localUnitDB;
    }

    public Long getUserID() {
        return userID;
    }

    public boolean getEmailValidated() {
        return emailValidated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserDB userDB = (UserDB) o;
        return getUserID() != null && Objects.equals(getUserID(), userDB.getUserID());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
