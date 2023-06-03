package fr.croixrouge.exposition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.croixrouge.config.InDBMockRepositoryConfig;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.domain.model.Operations;
import fr.croixrouge.domain.model.Resources;
import fr.croixrouge.exposition.dto.core.LoginRequest;
import fr.croixrouge.exposition.dto.core.RoleResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private String jwtToken;

    @BeforeEach
    public void setUp() throws Exception {
        LoginRequest loginRequest = new LoginRequest("defaultUser", "defaultPassword");

        String result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        jwtToken = objectMapper.readTree(result).get("jwtToken").asText();
    }

    @Test
    @DisplayName("Test that the role endpoint returns a list of roles when given a correct local unit id")
    public void roleLocalUnitIdSuccessTest() throws Exception {
        long roleId = 1L;

        RoleResponse roleResponse = new RoleResponse(
                "Val d'Orge default role",
                "Default role for Val d'Orge",
                Map.of( Resources.RESOURCE, List.of(Operations.READ)),
                new ArrayList<>(List.of(2L))
        );

        mockMvc.perform(get("/role/localunit/" + roleId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(roleResponse.getName()))
                .andExpect(jsonPath("$[0].description").value(roleResponse.getDescription()))
                .andExpect(jsonPath("$[0].authorizations").value(roleResponse.getAuthorizations()))
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
}
