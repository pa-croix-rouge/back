package fr.croixrouge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.croixrouge.Application;
import fr.croixrouge.exposition.dto.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;

    @BeforeEach
    public void setUp() throws Exception {
        jwtToken = this.loginUserToGetJWTToken("LUManager", "LUPassword");
    }

    private String loginUserToGetJWTToken(String username, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest(username, password);

        String result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(result).get("jwtToken").asText();
    }



    @Test
    @DisplayName("Test that the resources endpoint returns the list of resources when the JWT token is valid.")
    public void resourcesAccessTest() throws Exception {
        mockMvc.perform(get("/resources")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(content().string("[\"Resource 1\",\"Resource 2\",\"Resource 3\"]"));
    }

    @Test
    @DisplayName("Test that the resources endpoint returns a 403 when the JWT token is invalid.")
    public void resourcesAccessDeniedWrongJWTTest() throws Exception {
        mockMvc.perform(get("/resources")
                        .header("Authorization", "Bearer wrongToken"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Test that the resources endpoint returns a 403 when the user is not allowed to access the resource.")
    public void resourcesAccessDeniedForUserTest() throws Exception {
        jwtToken = this.loginUserToGetJWTToken("defaultUser", "defaultPassword");

        mockMvc.perform(get("/resources")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isForbidden());
    }
}
