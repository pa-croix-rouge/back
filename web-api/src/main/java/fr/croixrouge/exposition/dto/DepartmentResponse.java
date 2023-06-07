package fr.croixrouge.exposition.dto;

import fr.croixrouge.domain.model.Department;

public class DepartmentResponse {
    private final String name;
    private final String code;

    public DepartmentResponse(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public static DepartmentResponse fromDepartment(Department department) {
        return new DepartmentResponse(department.getName(), department.getCode());
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
