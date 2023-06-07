package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.Department;
import fr.croixrouge.exposition.dto.DepartmentResponse;
import fr.croixrouge.exposition.error.ErrorHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController extends ErrorHandler {

    @GetMapping("/department")
    public ResponseEntity<List<DepartmentResponse>> getDepartments() {
        return ResponseEntity.ok(Arrays.stream(Department.values()).map(DepartmentResponse::fromDepartment).toList());
    }
}
