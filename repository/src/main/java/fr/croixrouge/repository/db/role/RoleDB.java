package fr.croixrouge.repository.db.role;


import fr.croixrouge.repository.db.localunit.LocalUnitDB;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Table(name = "role",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "local_unit_db_localunit_id" }) })
@Entity
public class RoleDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleID;

    @Column(name = "description")
    private String description;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "local_unit_db_localunit_id")
    private LocalUnitDB localUnitDB;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "role_role_resourcedbs",
            joinColumns = @JoinColumn(name = "roledb_role_id"),
            inverseJoinColumns = @JoinColumn(name = "role_resourcedbs_id"))
    private Set<RoleResourceDB> roleResourceDBs = new LinkedHashSet<>();

    public RoleDB(Long roleID, String name, String description, LocalUnitDB localUnitDB, Set<RoleResourceDB> roleResourceDBs) {
        this.roleID = roleID;
        this.description = description;
        this.name = name;
        this.localUnitDB = localUnitDB;
        this.roleResourceDBs = roleResourceDBs;
    }

    public RoleDB() {
    }

    public LocalUnitDB getLocalUnitDB() {
        return localUnitDB;
    }

    public Set<RoleResourceDB> getRoleResourceDBs() {
        return roleResourceDBs;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRoleID() {
        return roleID;
    }
}
