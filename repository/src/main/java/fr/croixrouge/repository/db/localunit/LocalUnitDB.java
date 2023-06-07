package fr.croixrouge.repository.db.localunit;

import jakarta.persistence.*;

@Table(name = "localunit")
@Entity
public class LocalUnitDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "localunit_id")
    private Long localUnitID;

    @Column(name = "department")
    private String department;

    @Column(name = "street_number_and_name")
    private String streetNumberAndName;

    @Column(name = "city")
    private String city;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code")
    private String code;

    public LocalUnitDB() {
    }

    public LocalUnitDB(Long localUnitID, String name, String code, String department, String streetNumberAndName, String city, String postalCode) {
        this.localUnitID = localUnitID;
        this.department = department;
        this.code = code;
        this.streetNumberAndName = streetNumberAndName;
        this.city = city;
        this.postalCode = postalCode;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getStreetNumberAndName() {
        return streetNumberAndName;
    }

    public void setStreetNumberAndName(String streetNumberAndName) {
        this.streetNumberAndName = streetNumberAndName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLocalUnitID() {
        return localUnitID;
    }
}
