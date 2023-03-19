package fr.croixrouge.domain.model;

import java.util.List;
import java.util.Objects;

public class Role {
    private final String id;
    private final String name;
    private final String description;
    private final Route route;
    private final String localUnitId;
    private final List<String> userIds;

    public Role(String id, String name, String description, Route route, String localUnitId, List<String> userIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.route = route;
        this.localUnitId = localUnitId;
        this.userIds = userIds;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Route getRoute() {
        return route;
    }

    public String getLocalUnitId() {
        return localUnitId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", route=" + route +
                ", localUnitId='" + localUnitId + '\'' +
                ", userIds=" + userIds +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) && Objects.equals(name, role.name) && Objects.equals(description, role.description) && route == role.route && Objects.equals(localUnitId, role.localUnitId) && Objects.equals(userIds, role.userIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, route, localUnitId, userIds);
    }
}
