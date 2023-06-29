package fr.croixrouge.repository.db.storage;

import fr.croixrouge.repository.db.localunit.LocalUnitDB;
import jakarta.persistence.*;

@Table(name = "storage")
@Entity
public class StorageDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "department")
    private String department;

    @Column(name = "street_number_and_name")
    private String streetNumberAndName;

    @Column(name = "city")
    private String city;

    @Column(name = "postal_code")
    private String postalCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "local_unit_db_localunit_id", nullable = false)
    private LocalUnitDB localUnitDB;

    public StorageDB() {
    }

    public StorageDB(Long id, String name, String department, String streetNumberAndName, String city, String postalCode, LocalUnitDB localUnitDB) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.streetNumberAndName = streetNumberAndName;
        this.city = city;
        this.postalCode = postalCode;
        this.localUnitDB = localUnitDB;
    }

    public LocalUnitDB getLocalUnitDB() {
        return localUnitDB;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getStreetNumberAndName() {
        return streetNumberAndName;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }
}
