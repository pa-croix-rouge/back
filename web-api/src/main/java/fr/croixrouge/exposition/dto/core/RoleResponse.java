package fr.croixrouge.exposition.dto.core;

import fr.croixrouge.domain.model.Operations;
import fr.croixrouge.domain.model.Resources;
import fr.croixrouge.domain.model.Role;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class RoleResponse {

    private Long id;
    private String name;
    private String description;
    private Map<Resources, Set<Operations>> authorizations;
    private List<Long> userIDs;

    public RoleResponse() {
    }

    public RoleResponse(Long id, String name, String description, Map<Resources, Set<Operations>> authorizations, List<Long> userIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.authorizations = authorizations;
        this.userIDs = userIds;
    }

    public static RoleResponse fromRole(Role role) {
        return new RoleResponse(
                role.getId().value(),
                role.getName(),
                role.getDescription(),
                role.getAuthorizations(),
                List.of()// role.getUserIds().stream().map(ID::value).toList()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<Resources, Set<Operations>> getAuthorizations() {
        return authorizations;
    }

    public List<Long> getUserIDs() {
        return userIDs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleResponse that = (RoleResponse) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(authorizations, that.authorizations) && Objects.equals(userIDs, that.userIDs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, authorizations, userIDs);
    }
}
