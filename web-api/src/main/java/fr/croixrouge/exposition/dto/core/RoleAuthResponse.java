package fr.croixrouge.exposition.dto.core;

import fr.croixrouge.domain.model.Operations;
import fr.croixrouge.domain.model.Resources;

import java.util.List;

public class RoleAuthResponse {

    public List<Resources> resources = List.of( Resources.values());
    public List<Operations> operations = List.of( Operations.values());

}
