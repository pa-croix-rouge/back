package fr.croixrouge.repository.db.role;

import fr.croixrouge.domain.model.Operations;
import fr.croixrouge.domain.model.Resources;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Table(name = "role-resource")
@Entity
public class RoleResourceDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated
    @Column(name = "operations")
    private Operations operations;

    @Enumerated
    @Column(name = "resources")
    private Resources resources;


    public RoleResourceDB(Resources resources, Operations operations) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RoleResourceDB that = (RoleResourceDB) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
