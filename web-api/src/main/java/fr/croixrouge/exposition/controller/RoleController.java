package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Role;
import fr.croixrouge.exposition.dto.core.RoleCreationRequest;
import fr.croixrouge.exposition.dto.core.RoleResponse;
import fr.croixrouge.service.RoleService;
import fr.croixrouge.service.UserService;
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
        return new RoleResponse(model.getName(), model.getDescription(), model.getAuthorizations(), List.of());
    }

    @GetMapping("/localunit/{id}")
    public ResponseEntity<List<RoleResponse>> getRolesFromLocalUnitId(@PathVariable ID id) {
        List<RoleResponse> roleResponse = service.getRoleByLocalUnitId(id).stream().map(RoleResponse::fromRole).collect(Collectors.toList());
        if (roleResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(roleResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateRole(@PathVariable ID id, @RequestBody RoleCreationRequest roleCreationRequest) {
        service.updateRole(id, roleCreationRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{roleId}/user/{userId}")
    public ResponseEntity<Void> addRoleToUser(@PathVariable ID roleId, @PathVariable ID userId) {
        service.addRole(roleId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{roleId}/user/{userId}")
    public ResponseEntity<Void> removeRoleToUser(@PathVariable ID roleId, @PathVariable ID userId) {
        service.removeRole(roleId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<List<RoleResponse>> getUserRole( @PathVariable ID userId) {
        return ResponseEntity.ok(service.getUserRole( userId).stream().map(RoleResponse::fromRole).toList());
    }
}
