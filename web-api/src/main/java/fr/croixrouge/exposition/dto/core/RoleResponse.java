package fr.croixrouge.exposition.dto.core;

import fr.croixrouge.domain.model.Operations;
import fr.croixrouge.domain.model.Resources;
import fr.croixrouge.domain.model.Role;

import java.util.List;
import java.util.Map;

public class RoleResponse {
    private String name;
    private String description;
    private Map<Resources, List<Operations>> authorizations;
    private List<Long> userIds;

    public RoleResponse() {
    }

    public RoleResponse(String name, String description, Map<Resources, List<Operations>> authorizations, List<Long> userIds) {
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
                List.of()// role.getUserIds().stream().map(ID::value).toList()
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

    public List<Long> getUserIds() {
        return userIds;
    }
}
