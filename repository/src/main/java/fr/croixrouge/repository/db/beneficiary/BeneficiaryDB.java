package fr.croixrouge.repository.db.beneficiary;


import fr.croixrouge.repository.db.user.UserDB;
import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Table(name = "beneficiary")
@Entity
public class BeneficiaryDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "phonenumber")
    private String phonenumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_db_user_id", nullable = false)
    private UserDB userDB;

    @Column(name = "validated")
    private Boolean validated;

    @Column(name = "birthdate")
    private ZonedDateTime birthdate;

    @Column(name = "socialworkernumber")
    private String socialWorkerNumber;

    public BeneficiaryDB(Long id, UserDB userDB, String firstname, String lastname, String phonenumber, Boolean validated, ZonedDateTime birthdate, String socialWorkerNumber) {
        this.id = id;
        this.firstname = firstname;
        this.phonenumber = phonenumber;
        this.userDB = userDB;
        this.lastname = lastname;
        this.validated = validated;
        this.birthdate = birthdate;
        this.socialWorkerNumber = socialWorkerNumber;
    }

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public UserDB getUserDB() {
        return userDB;
    }

    public Boolean getValidated() {
        return validated;
    }

    public ZonedDateTime getBirthdate() {
        return birthdate;
    }

    public String getSocialWorkerNumber() {
        return socialWorkerNumber;
    }

    //todo faire les family members
}