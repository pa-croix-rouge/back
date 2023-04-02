package fr.croixrouge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.exposition.dto.LocalUnitRequest;
import fr.croixrouge.exposition.dto.LocalUnitResponse;
import fr.croixrouge.exposition.dto.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(MockRepositoryConfig.class)
public class LocalUnitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private  ObjectMapper objectMapper;

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
    @DisplayName("Test that the localunit endpoint returns a local unit when given a correct postal code")
    public void localUnitPostalCodeSuccessTest() throws Exception {
        LocalUnitRequest localUnitRequest = new LocalUnitRequest("91240");

        LocalUnitResponse localUnitResponse = new LocalUnitResponse(
                "Unite Local du Val d'Orge",
                "Essonne",
                "91240",
                "St Michel sur Orge",
                "76 rue des Liers",
                "LUManager"
        );

        mockMvc.perform(get("/localunit")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(localUnitRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(localUnitResponse.getName()))
                .andExpect(jsonPath("department").value(localUnitResponse.getDepartment()))
                .andExpect(jsonPath("postalCode").value(localUnitResponse.getPostalCode()))
                .andExpect(jsonPath("city").value(localUnitResponse.getCity()))
                .andExpect(jsonPath("streetNumberAndName").value(localUnitResponse.getStreetNumberAndName()))
                .andExpect(jsonPath("managerName").value(localUnitResponse.getManagerName()));
    }

    @Test
    @DisplayName("Test that the localunit endpoint returns a 404 when given a wrong postal code")
    public void localUnitPostalCodeFailedTest() throws Exception {
        LocalUnitRequest localUnitRequest = new LocalUnitRequest("00100");

        mockMvc.perform(get("/localunit")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(localUnitRequest)))
                .andExpect(status().isNotFound());
    }
}
