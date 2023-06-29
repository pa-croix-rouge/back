package fr.croixrouge.exposition.dto.core;

import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.domain.model.Operations;
import fr.croixrouge.domain.model.Resources;
import fr.croixrouge.domain.model.Role;
import fr.croixrouge.exposition.dto.CreationDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleCreationRequest extends CreationDTO<Role> {

    private final String name;
    private final String description;
    private final Map<Resources, Set<String>> authorizations;

    private final Long localUnitID;

    public RoleCreationRequest(String name, String description, Map<Resources, Set<String>> authorizations, Long localUnitID) {
        this.name = name;
        this.description = description;
        this.authorizations = authorizations;
        this.localUnitID = localUnitID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<Resources, Set<String>> getAuthorizations() {
        return authorizations;
    }

    public Long getLocalUnitID() {
        return localUnitID;
    }

    @Override
    public Role toModel() {
        final Map<Resources, Set<Operations>> auths = new HashMap<>();
        for (Resources resource : authorizations.keySet()) {
            auths.put(resource, authorizations.get(resource).stream().map(Operations::fromName).collect(Collectors.toSet()));
        }
        return new Role(null, name, description, auths, null, null);
    }

    public Role toModel(LocalUnit localUnit) {
        final Map<Resources, Set<Operations>> auths = new HashMap<>();
        for (Resources resource : authorizations.keySet()) {
            auths.put(resource, authorizations.get(resource).stream().map(Operations::fromName).collect(Collectors.toSet()));
        }
        return new Role(null, name, description, auths, localUnit, null);
    }
}
