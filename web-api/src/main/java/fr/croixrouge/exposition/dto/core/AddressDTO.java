package fr.croixrouge.exposition.dto.core;

import fr.croixrouge.domain.model.Address;
import fr.croixrouge.domain.model.Department;

import java.util.Objects;

public class AddressDTO {

    private String departmentCode;
    private String postalCode;
    private String city;
    private String streetNumberAndName;

    public AddressDTO() {
    }

    public AddressDTO(Address address) {
        this.departmentCode = address.getDepartment().getCode();
        this.postalCode = address.getPostalCode();
        this.city = address.getCity();
        this.streetNumberAndName = address.getStreetNumberAndName();
    }

    public AddressDTO(String departmentCode, String postalCode, String city, String streetNumberAndName) {
        this.departmentCode = departmentCode;
        this.postalCode = postalCode;
        this.city = city;
        this.streetNumberAndName = streetNumberAndName;
    }

    public Address toModel() {
        return new Address(Department.getDepartmentFromPostalCode(this.departmentCode), this.postalCode, this.city, this.streetNumberAndName);
    }

    public String getDepartmentCode() {
        return departmentCode;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressDTO that = (AddressDTO) o;
        return Objects.equals(departmentCode, that.departmentCode) && Objects.equals(postalCode, that.postalCode) && Objects.equals(city, that.city) && Objects.equals(streetNumberAndName, that.streetNumberAndName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentCode, postalCode, city, streetNumberAndName);
    }
}
