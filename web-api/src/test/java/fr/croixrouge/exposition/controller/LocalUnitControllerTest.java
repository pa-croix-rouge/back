package fr.croixrouge.exposition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.croixrouge.config.InDBMockRepositoryConfig;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.core.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({InDBMockRepositoryConfig.class, MockRepositoryConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class LocalUnitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private  ObjectMapper objectMapper;

    private String jwtToken;

    private Long createdLocalUnitId;


    @BeforeEach
    public void setUp() throws Exception {
        LoginRequest loginRequest = new LoginRequest("LUManager", "LUPassword");

        String result = mockMvc.perform(post("/login/volunteer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        jwtToken = objectMapper.readTree(result).get("jwtToken").asText();
    }

    @Test
    @Order(1)
    @DisplayName("Test that the localunit endpoint returns a local unit when given a correct postal code")
    public void localUnitPostalCodeSuccessTest() throws Exception {
        String localUnitPostCode = "91240";

        AddressDTO addressDTO = new AddressDTO(
                "91",
                "91240",
                "St Michel sur Orge",
                "76 rue des Liers"
        );

        LocalUnitResponse localUnitResponse = new LocalUnitResponse(
                91240L,
                "Unite Local du Val d'Orge",
                addressDTO,
                "LUManager",
                "91240-000"
        );

        mockMvc.perform(get("/localunit/postcode/" + localUnitPostCode)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(localUnitResponse.getName()))
                .andExpect(jsonPath("address.departmentCode").value(addressDTO.getDepartmentCode()))
                .andExpect(jsonPath("address.postalCode").value(addressDTO.getPostalCode()))
                .andExpect(jsonPath("address.city").value(addressDTO.getCity()))
                .andExpect(jsonPath("address.streetNumberAndName").value(addressDTO.getStreetNumberAndName()))
                .andExpect(jsonPath("managerName").value(localUnitResponse.getManagerName()))
                .andExpect(jsonPath("code").value(localUnitResponse.getCode()));
    }

    @Test
    @Order(2)
    @DisplayName("Test that the localunit endpoint returns a 404 when given a wrong postal code")
    public void localUnitPostalCodeFailedTest() throws Exception {
        String localUnitPostCode ="00100";

        mockMvc.perform(get("/localunit/postcode/" + localUnitPostCode)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(2)
    @DisplayName("Test that the localunit post endpoint returns a 200")
    public void createLocalUnit() throws Exception {
        LocalUnitCreationRequest localUnitCreationRequest = new LocalUnitCreationRequest(
                "temp local unit ",
                 new AddressDTO(
                        "75",
                        "75012",
                        "Paris",
                        "oui"),
                "defaultUser",
                "75012-000"
        );

        LocalUnitResponse localUnitResponse = new LocalUnitResponse(
                75012L,
                "temp local unit ",
                new AddressDTO(
                        "75",
                        "75012",
                        "Paris",
                        "oui"),
                "defaultUser",
                "75012-000"
        );

        var res = mockMvc.perform(post("/localunit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(localUnitCreationRequest)))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString();

        createdLocalUnitId = objectMapper.readValue(res, ID.class).value();

        var resLocalUnit = mockMvc.perform(get("/localunit/" + createdLocalUnitId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var localUnit = objectMapper.readValue(resLocalUnit, LocalUnitResponse.class);

        Assertions.assertEquals(localUnitResponse.getName(), localUnit.getName());
        Assertions.assertEquals(localUnitResponse.getAddress(), localUnit.getAddress());
        Assertions.assertEquals(localUnitResponse.getManagerName(), localUnit.getManagerName());
        Assertions.assertEquals(localUnitResponse.getCode(), localUnit.getCode());
    }

    @Test
    @Order(3)
    @DisplayName("Test that the localunit endpoint returns a local unit when given a correct id")
    public void localUnitIdSuccessTest() throws Exception {
        String localUnitId = "1";

        AddressDTO addressDTO = new AddressDTO(
                "91",
                "91240",
                "St Michel sur Orge",
                "76 rue des Liers"
        );

        LocalUnitResponse localUnitResponse = new LocalUnitResponse(
                91240L,
                "Unite Local du Val d'Orge",
                addressDTO,
                "LUManager",
                "91240-000"
        );

        mockMvc.perform(get("/localunit/" + localUnitId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(localUnitResponse.getName()))
                .andExpect(jsonPath("address.departmentCode").value(addressDTO.getDepartmentCode()))
                .andExpect(jsonPath("address.postalCode").value(addressDTO.getPostalCode()))
                .andExpect(jsonPath("address.city").value(addressDTO.getCity()))
                .andExpect(jsonPath("address.streetNumberAndName").value(addressDTO.getStreetNumberAndName()))
                .andExpect(jsonPath("managerName").value(localUnitResponse.getManagerName()))
                .andExpect(jsonPath("code").value(localUnitResponse.getCode()));
    }

    @Test
    @Order(4)
    @DisplayName("Test that the localunit endpoint returns a 404 when given a wrong id")
    public void localUnitIdFailedTest() throws Exception {
        String localUnitId ="-1";

        mockMvc.perform(get("/localunit/" + localUnitId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(5)
    @DisplayName("Test the the localunit endpoint regenerate the secret code when requested by manager")
    public void localUnitRegenerateSecretSuccessTest() throws Exception {
        Long localUnitId = 1L;

        LocalUnitUpdateSecretRequest localUnitUpdateSecretRequest = new LocalUnitUpdateSecretRequest(localUnitId);

        mockMvc.perform(get("/localunit/" + localUnitId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value("91240-000"));

        String newSecret = mockMvc.perform(post("/localunit/secret")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(localUnitUpdateSecretRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isString())
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/localunit/" + localUnitId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(newSecret));
    }

    @Test
    @Order(6)
    @DisplayName("Test the the localunit endpoint does not regenerate the secret code when requested by a non manager")
    public void localUnitRegenerateSecretFailTest() throws Exception {
        Long localUnitId = 1L;

        LocalUnitUpdateSecretRequest localUnitUpdateSecretRequest = new LocalUnitUpdateSecretRequest(localUnitId);

        LoginRequest loginRequest = new LoginRequest("defaultUser", "defaultPassword");

        String result = mockMvc.perform(post("/login/volunteer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        jwtToken = objectMapper.readTree(result).get("jwtToken").asText();

        mockMvc.perform(post("/localunit/secret")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(localUnitUpdateSecretRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    @DisplayName("Test the the localunit endpoint does not regenerate the secret code when given a wrong localunit id")
    public void localUnitRegenerateSecretNotFoundTest() throws Exception {
        Long localUnitId = -1L;

        LocalUnitUpdateSecretRequest localUnitUpdateSecretRequest = new LocalUnitUpdateSecretRequest(localUnitId);

        mockMvc.perform(post("/localunit/secret")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(localUnitUpdateSecretRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(8)
    @DisplayName("Test that the localunit stats endpoint returns a local unit stats when given a correct id")
    public void localUnitStatsSuccessTest() throws Exception {
        LocalUnitStatsResponse localUnitStatsResponse = new LocalUnitStatsResponse(
                3,
                1
        );

        mockMvc.perform(get("/localunit/stats")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("numberOfVolunteers").value(localUnitStatsResponse.getNumberOfVolunteers()))
                .andExpect(jsonPath("numberOfBeneficiaries").value(localUnitStatsResponse.getNumberOfBeneficiaries()));
    }
}
