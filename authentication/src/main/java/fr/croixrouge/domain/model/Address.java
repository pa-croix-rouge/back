package fr.croixrouge.domain.model;

import java.util.Objects;

public class Address {
    private final Department department;
    private final String postalCode;
    private final String city;
    private final String streetNumberAndName;

    public Address(Department department, String postalCode, String city, String streetNumberAndName) {
        this.department = department;
        this.postalCode = postalCode;
        this.city = city;
        this.streetNumberAndName = streetNumberAndName;
    }

    public Department getDepartment() {
        return department;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getStreetNumberAndName() {
        return streetNumberAndName;
    }

    @Override
    public String toString() {
        return "Address{" +
                "department=" + department +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", streetNumberAndName='" + streetNumberAndName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return department == address.department && Objects.equals(postalCode, address.postalCode) && Objects.equals(city, address.city) && Objects.equals(streetNumberAndName, address.streetNumberAndName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(department, postalCode, city, streetNumberAndName);
    }
}
