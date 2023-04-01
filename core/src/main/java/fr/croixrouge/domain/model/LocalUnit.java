package fr.croixrouge.domain.model;

import java.util.Objects;

public class LocalUnit {
    private final String localUnitId;
    private final String name;
    private final Address address;
    private final String managerId;

    public LocalUnit(String localUnitId, String name, Address address, String managerId) {
        this.localUnitId = localUnitId;
        this.name = name;
        this.address = address;
        this.managerId = managerId;
    }

    public String getLocalUnitId() {
        return localUnitId;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public String getManagerId() {
        return managerId;
    }

    @Override
    public String toString() {
        return "LocalUnit{" +
                "localUnitId='" + localUnitId + '\'' +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", managerId='" + managerId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalUnit localUnit = (LocalUnit) o;
        return Objects.equals(localUnitId, localUnit.localUnitId) && Objects.equals(name, localUnit.name) && Objects.equals(address, localUnit.address) && Objects.equals(managerId, localUnit.managerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(localUnitId, name, address, managerId);
    }
}