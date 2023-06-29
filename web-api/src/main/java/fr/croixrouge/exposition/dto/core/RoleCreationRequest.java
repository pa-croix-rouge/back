package fr.croixrouge.exposition.dto.core;

import fr.croixrouge.domain.model.*;
import fr.croixrouge.exposition.dto.CreationDTO;
import org.springframework.cglib.core.Local;

import java.util.Map;
import java.util.Set;

public class RoleCreationRequest extends CreationDTO<Role> {

    private final String name;
    private final String description;
    private final Map<Resources, Set<Operations>> authorizations;

    private final Long localUnitID;

    public RoleCreationRequest(String name, String description, Map<Resources, Set<Operations>> authorizations, Long localUnitID) {
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

    public Map<Resources, Set<Operations>> getAuthorizations() {
        return authorizations;
    }

    public Long getLocalUnitID() {
        return localUnitID;
    }

    @Override
    public Role toModel() {
        return new Role(null, name, description, authorizations, null, null);
    }
    public Role toModel(LocalUnit localUnit) {
        return new Role(null, name, description, authorizations, localUnit, null);
    }
}
