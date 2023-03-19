package fr.croixrouge.presentation.controller;

import fr.croixrouge.presentation.dto.RoleRequest;
import fr.croixrouge.presentation.dto.RoleResponse;
import fr.croixrouge.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<RoleResponse>> getRolesFromLocalUnitId(@RequestBody RoleRequest roleRequest) {
        List<RoleResponse> roleResponse = roleService.getRoleByLocalUnitId(roleRequest.getLocalUnitId()).stream().map(RoleResponse::fromRole).collect(Collectors.toList());
        if (roleResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(roleResponse);
    }
}
