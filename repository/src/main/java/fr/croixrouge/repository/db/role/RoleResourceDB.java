package fr.croixrouge.repository.db.role;

import fr.croixrouge.domain.model.Operations;
import fr.croixrouge.domain.model.Resources;
import jakarta.persistence.*;

@Table(name = "role-resource")
@Entity
public class RoleResourceDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_db_role_id", nullable = false)
    private RoleDB roleDB;

    @Enumerated
    @Column(name = "operations")
    private Operations operations;

    @Enumerated
    @Column(name = "resources")
    private Resources resources;

    public RoleDB getRoleDB() {
        return roleDB;
    }

    public void setRoleDB(RoleDB roleDB) {
        this.roleDB = roleDB;
    }

    public RoleResourceDB(Long id, Operations operations, Resources resources) {
        this.id = id;
        this.operations = operations;
        this.resources = resources;
    }

    public RoleResourceDB() {
    }

    public Operations getOperations() {
        return operations;
    }

    public void setOperations(Operations operations) {
        this.operations = operations;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public Long getId() {
        return id;
    }
}
