package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Role;
import fr.croixrouge.exposition.dto.core.RoleCreationRequest;
import fr.croixrouge.exposition.dto.core.RoleResponse;
import fr.croixrouge.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/role")
public class RoleController extends CRUDController<ID, Role, RoleService, RoleResponse, RoleCreationRequest> {

    public RoleController(RoleService roleService) {
        super(roleService);
    }

    @Override
    public RoleResponse toDTO(Role model) {
        return null;
    }

    @GetMapping("/localunit/{id}")
    public ResponseEntity<List<RoleResponse>> getRolesFromLocalUnitId(@PathVariable ID id) {
        List<RoleResponse> roleResponse = service.getRoleByLocalUnitId(id).stream().map(RoleResponse::fromRole).collect(Collectors.toList());
        if (roleResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(roleResponse);
    }
}
