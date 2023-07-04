package fr.croixrouge.repository.db.volunteer;

import fr.croixrouge.repository.db.user.UserDB;
import jakarta.persistence.*;

@Table(name = "volunteer")
@Entity
public class VolunteerDB {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "phonenumber")
    private String phonenumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_db_user_id", nullable = false)
    private UserDB userDB;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "validated")
    private Boolean validated;

    public VolunteerDB(Long id, UserDB userDB, String firstname, String lastname, String phonenumber, Boolean validated) {
        this.id = id;
        this.firstname = firstname;
        this.phonenumber = phonenumber;
        this.userDB = userDB;
        this.lastname = lastname;
        this.validated = validated;
    }

    public VolunteerDB() {
    }

    public UserDB getUserDB() {
        return userDB;
    }

    public void setUserDB(UserDB userDB) {
        this.userDB = userDB;
    }

    public Boolean getValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Long getId() {
        return id;
    }
}
