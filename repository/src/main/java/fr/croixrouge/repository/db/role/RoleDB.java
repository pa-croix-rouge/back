package fr.croixrouge.repository.db.role;


import jakarta.persistence.*;

@Table(name = "role")
@Entity
public class RoleDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleID;

    @Column(name = "description")
    private String description;

    @Column(name = "name")
    private String name;

    public RoleDB(Long roleID, String description, String name) {
        this.roleID = roleID;
        this.description = description;
        this.name = name;
    }

    public RoleDB() {
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
