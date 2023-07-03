package fr.croixrouge.domain.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Role extends Entity<ID> {

    public static final String COMMON_BENEFICIARY_ROLE_NAME = "Bénéficiaire";
    public static final String COMMON_VOLUNTEER_ROLE_NAME = "Volontaire";
    public static final String COMMON_MANAGER_ROLE_NAME = "Manager";

    private final String name;
    private final String description;
    private final Map<Resources, Set<Operations>> authorizations;

    private final LocalUnit localUnit;

    private final List<ID> userIds;

    public Role(ID id, String name, String description, Map<Resources, Set<Operations>> authorizations, LocalUnit localUnit, List<ID> userIds) {
        super(id);
        this.name = name;
        this.description = description;
        this.authorizations = authorizations;
        this.localUnit = localUnit;
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

    public Map<Resources, Set<Operations>> getAuthorizations() {
        return authorizations;
    }

    public LocalUnit getLocalUnit() {
        return localUnit;
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
                && Objects.equals(localUnit, role.localUnit)
                && Objects.equals(userIds, role.userIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, authorizations, localUnit, userIds);
    }


}
