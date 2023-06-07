package fr.croixrouge.domain.model;

import java.util.Objects;

public class LocalUnit extends Entity<ID> {
    private final String name;
    private final Address address;
    private final String managerUsername;
    private final String code;

    public LocalUnit(ID id, String name, Address address, String managerUsername, String code) {
        super(id);
        this.name = name;
        this.address = address;
        this.managerUsername = managerUsername;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public String getManagerUsername() {
        return managerUsername;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalUnit localUnit = (LocalUnit) o;
        return Objects.equals(name, localUnit.name) && Objects.equals(address, localUnit.address) && Objects.equals(managerUsername, localUnit.managerUsername) && Objects.equals(code, localUnit.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, managerUsername, code);
    }

    @Override
    public String toString() {
        return "LocalUnit{" +
                "name='" + name + '\'' +
                ", address=" + address +
                ", managerUsername='" + managerUsername + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
