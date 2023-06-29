package fr.croixrouge.exposition.dto.core;

import fr.croixrouge.domain.model.Operations;
import fr.croixrouge.domain.model.Resources;

import java.util.Arrays;
import java.util.List;

public class RoleAuthResponse {
    public List<Resources> resources = List.of(Resources.values());
    public List<String> operations = Arrays.stream(Operations.values()).map(Operations::getName).toList();
}
