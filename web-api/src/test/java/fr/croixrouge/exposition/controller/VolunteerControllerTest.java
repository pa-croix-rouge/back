package fr.croixrouge.exposition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.croixrouge.config.InDBMockRepositoryConfig;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.exposition.dto.core.LoginRequest;
import fr.croixrouge.exposition.dto.core.VolunteerCreationRequest;
import fr.croixrouge.exposition.dto.core.VolunteerResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({InDBMockRepositoryConfig.class, MockRepositoryConfig.class})
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
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
    @Order(1)
    @DisplayName("Test that the volunteer details endpoint returns volunteer's informations when given the correct id")
    public void volunteerIdSuccessTest() throws Exception {
        Long volunteerId = 1L;

        VolunteerResponse volunteerResponse = new VolunteerResponse(
                volunteerId,
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
                .andExpect(jsonPath("$.id").value(volunteerResponse.getId()))
                .andExpect(jsonPath("$.username").value(volunteerResponse.getUsername()))
                .andExpect(jsonPath("$.firstName").value(volunteerResponse.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(volunteerResponse.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(volunteerResponse.getPhoneNumber()))
                .andExpect(jsonPath("$.isValidated").value(volunteerResponse.getIsValidated()));
    }

    @Test
    @Order(2)
    @DisplayName("Test that the volunteer endpoint returns a list of volunteers based on your local unit")
    public void volunteerListSuccessTest() throws Exception {
        VolunteerResponse volunteerResponse1 = new VolunteerResponse(
                1L,
                "LUManager",
                "volunteerFirstName",
                "volunteerLastName",
                "+33 6 00 00 00 00",
                true,
                1L
        );
        VolunteerResponse volunteerResponse2 = new VolunteerResponse(
                2L,
                "defaultUser",
                "newVolunteer",
                "newVolunteerName",
                "+33 6 00 11 22 33",
                true,
                1L
        );
        VolunteerResponse volunteerResponse3 = new VolunteerResponse(
                3L,
                "volunteerUser",
                "newVolunteer2",
                "newVolunteerName2",
                "+33 6 00 11 22 34",
                false,
                1L
        );

        mockMvc.perform(get("/volunteer")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(volunteerResponse1.getId()))
                .andExpect(jsonPath("$[0].username").value(volunteerResponse1.getUsername()))
                .andExpect(jsonPath("$[0].firstName").value(volunteerResponse1.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(volunteerResponse1.getLastName()))
                .andExpect(jsonPath("$[0].phoneNumber").value(volunteerResponse1.getPhoneNumber()))
                .andExpect(jsonPath("$[0].isValidated").value(volunteerResponse1.getIsValidated()))
                .andExpect(jsonPath("$[1].id").value(volunteerResponse2.getId()))
                .andExpect(jsonPath("$[1].username").value(volunteerResponse2.getUsername()))
                .andExpect(jsonPath("$[1].firstName").value(volunteerResponse2.getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(volunteerResponse2.getLastName()))
                .andExpect(jsonPath("$[1].phoneNumber").value(volunteerResponse2.getPhoneNumber()))
                .andExpect(jsonPath("$[1].isValidated").value(volunteerResponse2.getIsValidated()))
                .andExpect(jsonPath("$[2].id").value(volunteerResponse3.getId()))
                .andExpect(jsonPath("$[2].username").value(volunteerResponse3.getUsername()))
                .andExpect(jsonPath("$[2].firstName").value(volunteerResponse3.getFirstName()))
                .andExpect(jsonPath("$[2].lastName").value(volunteerResponse3.getLastName()))
                .andExpect(jsonPath("$[2].phoneNumber").value(volunteerResponse3.getPhoneNumber()))
                .andExpect(jsonPath("$[2].isValidated").value(volunteerResponse3.getIsValidated()));
    }

    @Test
    @Order(3)
    @DisplayName("Test that the volunteer endpoint returns an (almost) empty list of volunteers based on your local unit")
    public void volunteerEmptyListSuccessTest() throws Exception {
        LoginRequest loginRequest = new LoginRequest("SLUManager", "SLUPassword");

        String result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String southernJwtToken = objectMapper.readTree(result).get("jwtToken").asText();

        mockMvc.perform(get("/volunteer")
                        .header("Authorization", "Bearer " + southernJwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].username").value("SLUManager"))
                .andExpect(jsonPath("$[0].firstName").value("southernVolunteer"))
                .andExpect(jsonPath("$[0].lastName").value("southernVolunteerName"))
                .andExpect(jsonPath("$[0].phoneNumber").value("+33 6 83 83 83 83"))
                .andExpect(jsonPath("$[0].isValidated").value(true));
    }

    @Test
    @Order(4)
    @DisplayName("Test that the volunteer details endpoint returns volunteer's informations from his token")
    public void volunteerFromTokenSuccessTest() throws Exception {
        VolunteerResponse volunteerResponse = new VolunteerResponse(
                1L,
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
                .andExpect(jsonPath("$.id").value(volunteerResponse.getId()))
                .andExpect(jsonPath("$.username").value(volunteerResponse.getUsername()))
                .andExpect(jsonPath("$.firstName").value(volunteerResponse.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(volunteerResponse.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(volunteerResponse.getPhoneNumber()))
                .andExpect(jsonPath("$.isValidated").value(volunteerResponse.getIsValidated()));
    }

    @Test
    @Order(5)
    @DisplayName("Test that the volunteer details endpoint returns a 404 when given incorrect id")
    public void volunteerIdFailingTest() throws Exception {
        String volunteerId = "-1";

        mockMvc.perform(get("/volunteer/" + volunteerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
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

        String result = mockMvc.perform(post("/volunteer/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(volunteerCreationRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String volunteerId = objectMapper.readTree(result).get("value").asText();

        VolunteerResponse volunteerResponse = new VolunteerResponse(
                Long.parseLong(volunteerId),
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
                .andExpect(jsonPath("$.id").value(volunteerResponse.getId()))
                .andExpect(jsonPath("$.username").value(volunteerResponse.getUsername()))
                .andExpect(jsonPath("$.firstName").value(volunteerResponse.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(volunteerResponse.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(volunteerResponse.getPhoneNumber()))
                .andExpect(jsonPath("$.isValidated").value(volunteerResponse.getIsValidated()));
    }

    @Test
    @Order(7)
    @DisplayName("Test that the volunteer validate endpoint returns a 404 when given incorrect account")
    public void volunteerValidateAccountNotFoundTest() throws Exception {
        String volunteerId = "-1";

        mockMvc.perform(post("/volunteer/validate/" + volunteerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(8)
    @DisplayName("Test that the volunteer validate endpoint returns a 403 when non manager tries to validate an account")
    public void volunteerValidateAccountFailedTest() throws Exception {
        String volunteerId = "3";

        LoginRequest loginRequest = new LoginRequest("defaultUser", "defaultPassword");

        String result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        jwtToken = objectMapper.readTree(result).get("jwtToken").asText();

        mockMvc.perform(post("/volunteer/validate/" + volunteerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(9)
    @DisplayName("Test that the volunteer validate endpoint validate the volunteer account of an existing account")
    public void volunteerValidateAccountSuccessTest() throws Exception {
        String volunteerId = "3";

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
    @Order(10)
    @DisplayName("Test that the volunteer invalidate endpoint returns a 404 when given incorrect account")
    public void volunteerInvalidateAccountNotFoundTest() throws Exception {
        String volunteerId = "-1";

        mockMvc.perform(post("/volunteer/invalidate/" + volunteerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(11)
    @DisplayName("Test that the volunteer invalidate endpoint returns a 403 when non manager tries to invalidate an account")
    public void volunteerInvalidateAccountFailedTest() throws Exception {
        String volunteerId = "3";

        LoginRequest loginRequest = new LoginRequest("defaultUser", "defaultPassword");

        String result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        jwtToken = objectMapper.readTree(result).get("jwtToken").asText();

        mockMvc.perform(post("/volunteer/invalidate/" + volunteerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(12)
    @DisplayName("Test that the volunteer invalidate endpoint invalidate the volunteer account of an existing account")
    public void volunteerInvalidateAccountSuccessTest() throws Exception {
        String volunteerId = "3";

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
    @Order(13)
    @DisplayName("Test that the volunteer endpoint can't deletes another volunteer")
    public void volunteerDeleteFailSuccessTest() throws Exception {
        String volunteerId = "4";

        LoginRequest loginRequest = new LoginRequest("defaultUser", "defaultPassword");

        String result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        jwtToken = objectMapper.readTree(result).get("jwtToken").asText();
        mockMvc.perform(delete("/volunteer/" + volunteerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/volunteer/" + volunteerId)
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }

    @Test
    @Order(14)
    @DisplayName("Test that the volunteer endpoint deletes volunteer")
    public void volunteerDeleteSuccessTest() throws Exception {
        String volunteerId = "2";

        LoginRequest loginRequest = new LoginRequest("defaultUser", "defaultPassword");

        String result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        jwtToken = objectMapper.readTree(result).get("jwtToken").asText();

        mockMvc.perform(delete("/volunteer/" + volunteerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());

        loginRequest = new LoginRequest("LUManager", "LUPassword");

        result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        jwtToken = objectMapper.readTree(result).get("jwtToken").asText();

        mockMvc.perform(get("/volunteer/" + volunteerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(15)
    @DisplayName("Test that the volunteer endpoint deletes volunteer when requested by manager")
    public void volunteerManagerDeleteSuccessTest() throws Exception {
        String volunteerId = "6";
        mockMvc.perform(delete("/volunteer/" + volunteerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/volunteer/" + volunteerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(16)
    @DisplayName("Test that the volunteer endpoint can't deletes non existing volunteer when requested by manager")
    public void volunteerManagerDeleteNotFoundSuccessTest() throws Exception {
        String volunteerId = "-1";
        mockMvc.perform(delete("/volunteer/" + volunteerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }
}
