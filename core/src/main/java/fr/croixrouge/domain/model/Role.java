package fr.croixrouge.domain.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Role extends Entity<ID> {
    private final String name;
    private final String description;
    private final Map<Resources, List<Operations>> authorizations;

    private final ID localUnitId;
    private final List<ID> userIds;

    public Role(ID id, String name, String description, Map<Resources, List<Operations>> authorizations, ID localUnitId, List<ID> userIds) {
        super(id);
        this.name = name;
        this.description = description;
        this.authorizations = authorizations;
        this.localUnitId = localUnitId;
        this.userIds = userIds;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean canAccessResource(Resources resource, Operations operation) {
        return authorizations.get(resource).contains(operation);
    }

    public Map<Resources, List<Operations>> getAuthorizations() {
        return authorizations;
    }

    public ID getLocalUnitId() {
        return localUnitId;
    }

    public List<ID> getUserIds() {
        return userIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id)
            && Objects.equals(name, role.name)
            && Objects.equals(description, role.description)
            && Objects.equals(authorizations, role.authorizations)
            && Objects.equals(localUnitId, role.localUnitId)
            && Objects.equals(userIds, role.userIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, authorizations, localUnitId, userIds);
    }


}
