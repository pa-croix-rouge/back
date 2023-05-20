package fr.croixrouge.exposition.dto.core;

import fr.croixrouge.domain.model.Address;
import fr.croixrouge.domain.model.Department;

public class AddressDTO {

    private final String departmentCode;
    private final String postalCode;
    private final String city;
    private final String streetNumberAndName;


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


}
