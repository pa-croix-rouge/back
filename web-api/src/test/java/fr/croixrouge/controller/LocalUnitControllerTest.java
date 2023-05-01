package fr.croixrouge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.exposition.dto.core.AddressDTO;
import fr.croixrouge.exposition.dto.core.LocalUnitRequest;
import fr.croixrouge.exposition.dto.core.LocalUnitResponse;
import fr.croixrouge.exposition.dto.core.LoginRequest;
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
        AddressDTO addressDTO = new AddressDTO(
                "91",
                "91240",
                "St Michel sur Orge",
                "76 rue des Liers"
        );
        LocalUnitRequest localUnitRequest = new LocalUnitRequest("91240");

        LocalUnitResponse localUnitResponse = new LocalUnitResponse(
                "91240",
                "Unite Local du Val d'Orge",
                addressDTO,
                "LUManager"
        );

        mockMvc.perform(get("/localunit")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(localUnitRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(localUnitResponse.getName()))
                .andExpect(jsonPath("address.departmentCode").value(addressDTO.getDepartmentCode()))
                .andExpect(jsonPath("address.postalCode").value(addressDTO.getPostalCode()))
                .andExpect(jsonPath("address.city").value(addressDTO.getCity()))
                .andExpect(jsonPath("address.streetNumberAndName").value(addressDTO.getStreetNumberAndName()))
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
