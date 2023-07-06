package fr.croixrouge.repository.db.beneficiary;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.util.Objects;

@Table(name = "family_member")
@Entity
public class FamilyMemberDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @ManyToOne
    @JoinColumn(name = "beneficiary_db_id")
    private BeneficiaryDB beneficiaryDB;

    public BeneficiaryDB getBeneficiaryDB() {
        return beneficiaryDB;
    }

    public void setBeneficiaryDB(BeneficiaryDB beneficiaryDB) {
        this.beneficiaryDB = beneficiaryDB;
    }

    public FamilyMemberDB(Long id, String firstname, String lastname, LocalDate birthdate, BeneficiaryDB beneficiaryDB) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.beneficiaryDB = beneficiaryDB;
    }

    public FamilyMemberDB() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FamilyMemberDB that = (FamilyMemberDB) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
