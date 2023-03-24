package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.Route;
import fr.croixrouge.service.AuthenticationService;
import fr.croixrouge.service.ResourceService;
import fr.croixrouge.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;
    private final AuthenticationService authenticationService;
    private final RoleService roleService;

    public ResourceController(ResourceService resourceService, AuthenticationService authenticationService, RoleService roleService) {
        this.resourceService = resourceService;
        this.authenticationService = authenticationService;
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getResources(HttpServletRequest request) {
        String userId = authenticationService.getUserIdFromJwtToken(request);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (!roleService.isUserIdAuthorizedToAccessRoute(userId, Route.RESOURCE.getPath())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<String> resources = resourceService.getResources();
        return ResponseEntity.ok(resources);
    }
}
