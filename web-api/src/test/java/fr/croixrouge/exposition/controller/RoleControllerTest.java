package fr.croixrouge.exposition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import fr.croixrouge.config.InDBMockRepositoryConfig;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.domain.model.Operations;
import fr.croixrouge.domain.model.Resources;
import fr.croixrouge.exposition.dto.core.LoginRequest;
import fr.croixrouge.exposition.dto.core.RoleCreationRequest;
import fr.croixrouge.exposition.dto.core.RoleResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({InDBMockRepositoryConfig.class, MockRepositoryConfig.class})
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken, jwtTokenForAutTest;

    @BeforeEach
    public void setUp() throws Exception {
        LoginRequest loginRequest = new LoginRequest("defaultUser", "defaultPassword");

        String result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        jwtToken = objectMapper.readTree(result).get("jwtToken").asText();

        loginRequest = new LoginRequest("userForAuthTest", "userForAuthTestPassword");

        result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        jwtTokenForAutTest = objectMapper.readTree(result).get("jwtToken").asText();

    }

    @Test
    @DisplayName("Test that the role endpoint returns a list of roles when given a correct local unit id")
    public void roleLocalUnitIdSuccessTest() throws Exception {
        long roleId = 1L;

        HashMap<Resources, Set<Operations>> roleResources = new HashMap<>();
        for (var ressource : Resources.values()) {
            roleResources.put(ressource, Set.of(Operations.values()));
        }

        RoleResponse roleResponse = new RoleResponse(
                "Val d'Orge default role",
                "Default role for Val d'Orge",
                roleResources,
                new ArrayList<>(List.of(2L))
        );

        mockMvc.perform(get("/role/localunit/" + roleId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(roleResponse.getName()))
                .andExpect(jsonPath("$[0].description").value(roleResponse.getDescription()))
              //  .andExpect(jsonPath("$[0].authorizations").value(roleResponse.getAuthorizations()))
//                .andExpect(jsonPath("$[0].userIds").exists())
//                .andExpect(jsonPath("$[0].userIds").isArray())
//                .andExpect(jsonPath("$[0].userIds").isNotEmpty())
//                .andExpect(jsonPath("$[0].userIds").value(containsInAnyOrder(2)))
        ;
    }

    @Test
    @DisplayName("Test that the role endpoint returns a 404 when given an incorrect local unit id")
    public void roleLocalUnitIdFailedTest() throws Exception {
        String roleId = "2";

        mockMvc.perform(get("/role/localunit/" + roleId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test that the role create endpoint returns a 403 when you user is not allowed to")
    public void getRoleForbidden() throws Exception {
        HashMap<Resources, Set<Operations>> roleResources = new HashMap<>();
        for (var ressource : Resources.values()) {
            roleResources.put(ressource, new HashSet<>( Set.of(Operations.values())));
        }
        roleResources.get(Resources.ROLE).remove(Operations.READ);

        RoleCreationRequest roleCreationRequest = new RoleCreationRequest(
                null,
                null,
                roleResources,
                1L
        );

        mockMvc.perform(put("/role/3")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleCreationRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/role/2")
                        .header("Authorization", "Bearer " + jwtTokenForAutTest))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Test that the role post endpoint returns a 200 when given correct data")
    public void createNewRole() throws Exception {
        RoleCreationRequest roleCreationRequest = new RoleCreationRequest(
                "Test role",
                "Test role description",
                Map.of(Resources.RESOURCE, Set.of(Operations.READ)),
                1L
        );

        var res = mockMvc.perform(post("/role")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleCreationRequest)))
                .andExpect(status().isOk());

        String id = JsonPath.read(res.andReturn().getResponse().getContentAsString(), "$.value").toString();

        mockMvc.perform(get("/role/" + id)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(roleCreationRequest.getName()))
                .andExpect(jsonPath("$.description").value(roleCreationRequest.getDescription()))
                .andExpect(jsonPath("$.authorizations").value("{"+Resources.RESOURCE.name()+"=["+Operations.READ.name()+"]}") )     ;
    }

    @Test
    @DisplayName("Test that the role create endpoint returns a 403 when you user is not allowed to")
    public void createRoleForbidden() throws Exception {
        HashMap<Resources, Set<Operations>> roleResources = new HashMap<>();
        for (var ressource : Resources.values()) {
            roleResources.put(ressource, new HashSet<>( Set.of(Operations.values())));
        }
        roleResources.get(Resources.ROLE).remove(Operations.CREATE);

        RoleCreationRequest roleCreationRequest = new RoleCreationRequest(
                null,
                null,
                roleResources,
                1L
        );

        mockMvc.perform(put("/role/3")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleCreationRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/role")
                        .header("Authorization", "Bearer " + jwtTokenForAutTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleCreationRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Test that the role put endpoint returns a 200 when given correct data")
    public void updateRole() throws Exception {
        String id = "3";
        RoleCreationRequest roleCreationRequest = new RoleCreationRequest(
                "Test role 2",
                "Test role description 2",
                Map.of(Resources.RESOURCE, Set.of(Operations.READ, Operations.CREATE), Resources.EVENT, Set.of(Operations.READ)),
                1L
        );

        mockMvc.perform(put("/role/" + id)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleCreationRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/role/" + id)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(roleCreationRequest.getName()))
                .andExpect(jsonPath("$.description").value(roleCreationRequest.getDescription()));
        //TODO : fix authorizations order in map
            //    .andExpect(jsonPath("$.authorizations").value("{"+Resources.RESOURCE.name()+"=["+Operations.READ.name()+", "+Operations.CREATE.name()+"], "+Resources.EVENT+"=["+Operations.READ.name()+"]}") )     ;
    }

    @Test
    @DisplayName("Test that the role put endpoint returns a 403 when you user is not allowed to")
    public void updateRoleForbidden() throws Exception {
        HashMap<Resources, Set<Operations>> roleResources = new HashMap<>();
        for (var ressource : Resources.values()) {
            roleResources.put(ressource, new HashSet<>( Set.of(Operations.values())));
        }
        roleResources.get(Resources.ROLE).remove(Operations.UPDATE);

        RoleCreationRequest roleCreationRequest = new RoleCreationRequest(
                null,
                null,
                roleResources,
                1L
        );

        mockMvc.perform(put("/role/3")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleCreationRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/role/2")
                        .header("Authorization", "Bearer " + jwtTokenForAutTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleCreationRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Test that the role delete endpoint returns a 200 when given correct data")
    public void deleteRole() throws Exception {
        mockMvc.perform(delete("/role/4")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Test that the role delete endpoint returns a 403 when you user is not allowed to delete any role")
    public void deleteRoleForbidden() throws Exception {

        HashMap<Resources, Set<Operations>> roleResources = new HashMap<>();
        for (var ressource : Resources.values()) {
            roleResources.put(ressource, new HashSet<>( Set.of(Operations.values())));
        }
        roleResources.get(Resources.ROLE).remove(Operations.DELETE);

        RoleCreationRequest roleCreationRequest = new RoleCreationRequest(
                null,
                null,
                roleResources,
                1L
        );

        mockMvc.perform(put("/role/3")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleCreationRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/role/2")
                        .header("Authorization", "Bearer " + jwtTokenForAutTest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }


}
