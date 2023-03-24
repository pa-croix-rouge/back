package fr.croixrouge.exposition.dto;

import fr.croixrouge.domain.model.Operations;
import fr.croixrouge.domain.model.Resources;
import fr.croixrouge.domain.model.Role;

import java.util.List;
import java.util.Map;

public class RoleResponse {
    private String name;
    private String description;
    private Map<Resources, List<Operations>> authorizations;
    private List<String> userIds;

    public RoleResponse() {
    }

    public RoleResponse(String name, String description, Map<Resources, List<Operations>> authorizations, List<String> userIds) {
        this.name = name;
        this.description = description;
        this.authorizations = authorizations;
        this.userIds = userIds;
    }

    public static RoleResponse fromRole(Role role) {
        return new RoleResponse(
                role.getName(),
                role.getDescription(),
                role.getAuthorizations(),
                role.getUserIds()
        );
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthorizations() {
        return authorizations.toString();
    }

    public List<String> getUserIds() {
        return userIds;
    }
}
