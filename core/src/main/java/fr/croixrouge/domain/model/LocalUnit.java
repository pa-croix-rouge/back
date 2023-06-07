package fr.croixrouge.domain.model;

import java.util.Objects;

public class LocalUnit extends Entity<ID> {
    private final String name;
    private final Address address;
    private final User manager;
    private final String code;

    public LocalUnit(ID localUnitId, String name, Address address, User manager, String code) {
        super(localUnitId);
        this.name = name;
        this.address = address;
        this.manager = manager;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

//    public User getManager() {
//        return manager;
//    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalUnit localUnit = (LocalUnit) o;
        return Objects.equals(name, localUnit.name) && Objects.equals(address, localUnit.address) && Objects.equals(manager, localUnit.manager) && Objects.equals(code, localUnit.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, manager, code);
    }

    @Override
    public String toString() {
        return "LocalUnit{" +
                "name='" + name + '\'' +
                ", address=" + address +
                ", manager=" + manager +
                ", code='" + code + '\'' +
                '}';
    }
}
