package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.core.RoleRequest;
import fr.croixrouge.exposition.dto.core.RoleResponse;
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
        List<RoleResponse> roleResponse = roleService.getRoleByLocalUnitId(new ID(roleRequest.getLocalUnitId())).stream().map(RoleResponse::fromRole).collect(Collectors.toList());
        if (roleResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(roleResponse);
    }
}
