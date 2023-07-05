package fr.croixrouge.config;

import fr.croixrouge.domain.model.Operations;
import fr.croixrouge.domain.model.Resources;
import fr.croixrouge.domain.model.Role;
import fr.croixrouge.domain.repository.RoleRepository;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
public class RoleConfig {

    public RoleConfig(RoleRepository roleRepository) {
        HashMap<Resources, Set<Operations>> roleResources;

        if (roleRepository.findCommonRole(Role.COMMON_MANAGER_ROLE_NAME).isEmpty()) {
            roleResources = new HashMap<>();
            for (var ressource : Resources.values()) {
                roleResources.put(ressource, Set.of(Operations.values()));
            }

            roleRepository.save(new Role(
                    null,
                    Role.COMMON_MANAGER_ROLE_NAME,
                    "Manger d'unité local",
                    roleResources,
                    null,
                    List.of()
            ));
        }

        if (roleRepository.findCommonRole(Role.COMMON_VOLUNTEER_ROLE_NAME).isEmpty()) {
            roleResources = new HashMap<>();
            for (var ressource : Resources.values()) {
                roleResources.put(ressource, Set.of(Operations.READ));
            }

            roleRepository.save(new Role(
                    null,
                    Role.COMMON_VOLUNTEER_ROLE_NAME,
                    "Volontaire d'unité local",
                    roleResources,
                    null,
                    List.of()
            ));
        }

        if (roleRepository.findCommonRole(Role.COMMON_BENEFICIARY_ROLE_NAME).isEmpty()) {
            roleRepository.save(new Role(
                    null,
                    Role.COMMON_BENEFICIARY_ROLE_NAME,
                    "Bénéficiaire d'unité local",
                    Map.of(Resources.EVENT, Set.of(Operations.READ, Operations.CREATE, Operations.DELETE),
                            Resources.BENEFICIARY, Set.of(Operations.READ, Operations.CREATE, Operations.UPDATE, Operations.DELETE),
                            Resources.LOCAL_UNIT, Set.of(Operations.READ)
                    ),
                    null,
                    List.of()
            ));
        }
    }
}
