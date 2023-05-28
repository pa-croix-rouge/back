package fr.croixrouge.exposition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.exposition.dto.core.LoginRequest;
import fr.croixrouge.exposition.dto.core.VolunteerCreationRequest;
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
        LoginRequest loginRequest = new LoginRequest("LUManager", "LUPassword");

        String result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        jwtToken = objectMapper.readTree(result).get("jwtToken").asText();
    }

    @Test
    @DisplayName("Test that the volunteer details endpoint returns volunteer's informations when given the correct id")
    public void volunteerIdSuccessTest() throws Exception {
        String volunteerId = "1";

        VolunteerResponse volunteerResponse = new VolunteerResponse(
                "LUManager",
                "volunteerFirstName",
                "volunteerLastName",
                "+33 6 00 00 00 00",
                true,
                1L
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

    @Test
    @DisplayName("Test that the volunteer details endpoint returns volunteer's informations when given the correct id")
    public void volunteerFromTokenSuccessTest() throws Exception {
        VolunteerResponse volunteerResponse = new VolunteerResponse(
                "LUManager",
                "volunteerFirstName",
                "volunteerLastName",
                "+33 6 00 00 00 00",
                true,
                1L
        );

        mockMvc.perform(get("/volunteer/token")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(volunteerResponse.getUsername()))
                .andExpect(jsonPath("$.firstName").value(volunteerResponse.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(volunteerResponse.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(volunteerResponse.getPhoneNumber()))
                .andExpect(jsonPath("$.isValidated").value(volunteerResponse.getIsValidated()));
    }

    @Test
    @DisplayName("Test that the volunteer details endpoint returns a 404 when given incorrect id")
    public void volunteerIdFailingTest() throws Exception {
        String volunteerId = "-1";

        mockMvc.perform(get("/volunteer/" + volunteerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test that the volunteer endpoint register a new volunteer when given the correct parameters")
    public void volunteerCreateSuccessTest() throws Exception {
        VolunteerCreationRequest volunteerCreationRequest = new VolunteerCreationRequest(
                "newvolunteer@croix-rouge.fr",
                "secretPassword",
                "John",
                "Doe",
                "+33 6 00 11 00 11",
                "91240-000"
        );

        String result = mockMvc.perform(post("/volunteer")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(volunteerCreationRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String volunteerId = objectMapper.readTree(result).get("value").asText();

        VolunteerResponse volunteerResponse = new VolunteerResponse(
                "newvolunteer@croix-rouge.fr",
                "John",
                "Doe",
                "+33 6 00 11 00 11",
                false,
                1L
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

    @Test
    @DisplayName("Test that the volunteer validate endpoint validate the volunteer account of an existing account")
    public void volunteerValidateAccountSuccessTest() throws Exception {
        String volunteerId = "2";

        mockMvc.perform(get("/volunteer/" + volunteerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isValidated").value(false));

        mockMvc.perform(post("/volunteer/validate/" + volunteerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/volunteer/" + volunteerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isValidated").value(true));
    }

    @Test
    @DisplayName("Test that the volunteer validate endpoint returns a 404 when given incorrect account")
    public void volunteerValidateAccountFailedTest() throws Exception {
        String volunteerId = "-1";

        mockMvc.perform(post("/volunteer/validate/" + volunteerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test that the volunteer invalidate endpoint invalidate the volunteer account of an existing account")
    public void volunteerInvalidateAccountSuccessTest() throws Exception {
        String volunteerId = "2";

        mockMvc.perform(get("/volunteer/" + volunteerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isValidated").value(true));

        mockMvc.perform(post("/volunteer/invalidate/" + volunteerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/volunteer/" + volunteerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isValidated").value(false));
    }

    @Test
    @DisplayName("Test that the volunteer invalidate endpoint returns a 404 when given incorrect account")
    public void volunteerInvalidateAccountFailedTest() throws Exception {
        String volunteerId = "-1";

        mockMvc.perform(post("/volunteer/validate/" + volunteerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }
}
