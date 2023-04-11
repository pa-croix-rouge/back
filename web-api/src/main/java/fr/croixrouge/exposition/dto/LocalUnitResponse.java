package fr.croixrouge.exposition.dto;

import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.domain.model.User;

public class LocalUnitResponse {

    private String name;
    private String department;
    private String postalCode;
    private String city;
    private String streetNumberAndName;
    private String managerName;

    public LocalUnitResponse(String name, String department, String postalCode, String city, String streetNumberAndName, String managerName) {
        this.name = name;
        this.department = department;
        this.postalCode = postalCode;
        this.city = city;
        this.streetNumberAndName = streetNumberAndName;
        this.managerName = managerName;
    }

    public static LocalUnitResponse fromLocalUnit(LocalUnit localUnit, User manager) {
        return new LocalUnitResponse(
                localUnit.getName(),
                localUnit.getAddress().getDepartment().getName(),
                localUnit.getAddress().getPostalCode(),
                localUnit.getAddress().getCity(),
                localUnit.getAddress().getStreetNumberAndName(),
                manager.getUsername()
        );
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
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

    public String getManagerName() {
        return managerName;
    }
}
