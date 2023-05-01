package fr.croixrouge.exposition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.exposition.dto.core.LoginRequest;
import fr.croixrouge.exposition.dto.core.VolunteerResponse;
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
public class VolunteerControllerTest {

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
    @DisplayName("Test that the volunteer details endpoint retunrs volunteer's informations when given the correct id")
    public void volunteerIdSuccessTest() throws Exception {
        String volunteerId = "1";

        VolunteerResponse volunteerResponse = new VolunteerResponse(
                "LUManager",
                "volunteerFirstName",
                "volunteerLastName",
                "+33 6 00 00 00 00",
                true
        );

        mockMvc.perform(get("/volunteer/" + volunteerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(volunteerResponse.getUsername()))
                .andExpect(jsonPath("$.firstName").value(volunteerResponse.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(volunteerResponse.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(volunteerResponse.getPhoneNumber()))
                .andExpect(jsonPath("$.isValidated").value(volunteerResponse.getIsValidated()));
    }
}
