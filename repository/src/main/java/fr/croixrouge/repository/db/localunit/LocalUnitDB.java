package fr.croixrouge.repository.db.localunit;

import jakarta.persistence.*;

@Table(name = "localunit")
@Entity
public class LocalUnitDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "localunit_id")
    private String localUnitID;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "name", nullable = false)
    private String name;

    public LocalUnitDB(String localUnitID, String address, String name) {
        this.localUnitID = localUnitID;
        this.address = address;
        this.name = name;
    }

    public LocalUnitDB() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalUnitID() {
        return localUnitID;
    }
}
