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
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

        String result = mockMvc.perform(post("/login/volunteer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        jwtToken = objectMapper.readTree(result).get("jwtToken").asText();

        loginRequest = new LoginRequest("userForAuthTest", "userForAuthTestPassword");

        result = mockMvc.perform(post("/login/volunteer")
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
                null,
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
        HashMap<Resources, Set<String>> roleResources = new HashMap<>();
        for (var ressource : Resources.values()) {
            roleResources.put(ressource, new HashSet<>(Arrays.stream(Operations.values()).map(Operations::getName).toList()));
        }
        roleResources.get(Resources.ROLE).remove(Operations.READ.getName());

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
                Map.of(Resources.RESOURCE, Set.of(Operations.READ.getName())),
                1L
        );

        final Map<Resources, Set<Operations>> auths = new HashMap<>();
        for (Resources resource : roleCreationRequest.getAuthorizations().keySet()) {
            auths.put(resource, roleCreationRequest.getAuthorizations().get(resource).stream().map(Operations::fromName).collect(Collectors.toSet()));
        }

        RoleResponse roleResponse = new RoleResponse(
                null,
                roleCreationRequest.getName(),
                roleCreationRequest.getDescription(),
                auths,
                List.of()
        );

        var res = mockMvc.perform(post("/role")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleCreationRequest)))
                .andExpect(status().isOk());

        String id = JsonPath.read(res.andReturn().getResponse().getContentAsString(), "$.value").toString();

        var resGet = mockMvc.perform(get("/role/" + id)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var role = objectMapper.reader().readValue(resGet, RoleResponse.class);
        Assertions.assertEquals(roleResponse, role);
    }

    @Test
    @DisplayName("Test that the role create endpoint returns a 403 when you user is not allowed to")
    public void createRoleForbidden() throws Exception {
        HashMap<Resources, Set<String>> roleResources = new HashMap<>();
        for (var ressource : Resources.values()) {
            roleResources.put(ressource, new HashSet<>(Arrays.stream(Operations.values()).map(Operations::getName).toList()));
        }
        roleResources.get(Resources.ROLE).remove(Operations.CREATE.getName());

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
                Map.of(Resources.RESOURCE, Set.of(Operations.READ.getName(), Operations.CREATE.getName()), Resources.EVENT, Set.of(Operations.READ.getName())),
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
        HashMap<Resources, Set<String>> roleResources = new HashMap<>();
        for (var ressource : Resources.values()) {
            roleResources.put(ressource, new HashSet<>(Arrays.stream(Operations.values()).map(Operations::getName).toList()));
        }
        roleResources.get(Resources.ROLE).remove(Operations.UPDATE.getName());

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
    @Order(3)
    @DisplayName("Test that the role delete endpoint returns a 403 when user is not allowed to delete any role")
    public void deleteRoleForbidden() throws Exception {
        HashMap<Resources, Set<String>> roleResources = new HashMap<>();
        for (var ressource : Resources.values()) {
            roleResources.put(ressource, new HashSet<>(Arrays.stream(Operations.values()).map(Operations::getName).toList()));
        }
        roleResources.get(Resources.ROLE).remove(Operations.DELETE.getName());

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


    @Test
    @Order(1)
    @DisplayName("Test that the role assign endpoint returns a 200")
    public void addRoleToUser() throws Exception {

        String userID = "2";
        mockMvc.perform(post("/role/2/user/" + userID)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        var res = mockMvc.perform(get("/role/user/" + userID)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn().getResponse().getContentAsString();

        var roles = List.of(objectMapper.reader().readValue(res, RoleResponse[].class));
        Assertions.assertEquals(2, roles.stream()
                .filter(role -> role.getName().equals("roleForAuthTest") || role.getName().equals("default role"))
                .count());
    }

    @Test
    @Order(2)
    @DisplayName("Test that the role unassign endpoint returns a 200")
    public void deleteRoleToUser() throws Exception {

        String userID = "2";
        mockMvc.perform(delete("/role/2/user/" + userID)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/role/user/" + userID)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("roleForAuthTest"));
    }



}
