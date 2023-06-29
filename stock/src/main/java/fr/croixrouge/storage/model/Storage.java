package fr.croixrouge.storage.model;

import fr.croixrouge.domain.model.Address;
import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.LocalUnit;

public class Storage extends Entity<ID> {

    private final String name;
    private final LocalUnit localUnit;
    private final Address address;


    public Storage(ID id, String name, LocalUnit localUnit, Address address) {
        super(id);
        this.name = name;
        this.localUnit = localUnit;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public LocalUnit getLocalUnit() {
        return localUnit;
    }

    public Address getAddress() {
        return address;
    }

}
