package fr.croixrouge.presentation.dto;

import java.util.List;

public class RoleResponse {
    private String name;
    private String description;
    private String routeName;
    private List<String> userIds;

    public RoleResponse(String name, String description, String routeName, List<String> userIds) {
        this.name = name;
        this.description = description;
        this.routeName = routeName;
        this.userIds = userIds;
    }

    public static RoleResponse fromRole(fr.croixrouge.domain.model.Role role) {
        return new RoleResponse(
                role.getName(),
                role.getDescription(),
                role.getRoute().getPath(),
                role.getUserIds()
        );
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRouteName() {
        return routeName;
    }

    public List<String> getUserIds() {
        return userIds;
    }
}
