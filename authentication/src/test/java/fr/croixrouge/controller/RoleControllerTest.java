package fr.croixrouge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.croixrouge.domain.model.Route;
import fr.croixrouge.presentation.dto.LoginRequest;
import fr.croixrouge.presentation.dto.RoleRequest;
import fr.croixrouge.presentation.dto.RoleResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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
        RoleRequest roleRequest = new RoleRequest("1");

        RoleResponse roleResponse = new RoleResponse(
                "Val d'Orge default role",
                "Default role for Val d'Orge",
                Route.RESOURCE.getPath(),
                new ArrayList<>(Collections.singletonList("2"))
        );

        mockMvc.perform(get("/role")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(roleResponse.getName()))
                .andExpect(jsonPath("$[0].description").value(roleResponse.getDescription()))
                .andExpect(jsonPath("$[0].routeName").value(roleResponse.getRouteName()))
                .andExpect(jsonPath("$[0].userIds").value(roleResponse.getUserIds()));
    }

    @Test
    @DisplayName("Test that the role endpoint returns a 404 when given an incorrect local unit id")
    public void roleLocalUnitIdFailedTest() throws Exception {
        RoleRequest roleRequest = new RoleRequest("2");

        mockMvc.perform(get("/role")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleRequest)))
                .andExpect(status().isNotFound());
    }
}
