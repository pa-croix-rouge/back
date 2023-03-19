package fr.croixrouge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.croixrouge.presentation.dto.LocalUnitRequest;
import fr.croixrouge.presentation.dto.LocalUnitResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LocalUnitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void localUnitPostalCodeFailedPostalCodeTest() throws Exception {
        LocalUnitRequest localUnitRequest = new LocalUnitRequest("00100");

        mockMvc.perform(get("/localunit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(localUnitRequest)))
                .andExpect(status().isNotFound());
    }
}
