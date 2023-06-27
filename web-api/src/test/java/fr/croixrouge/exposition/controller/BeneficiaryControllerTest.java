package fr.croixrouge.exposition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.croixrouge.config.InDBMockRepositoryConfig;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.core.BeneficiaryResponse;
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
@Import({InDBMockRepositoryConfig.class, MockRepositoryConfig.class})
public class BeneficiaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;

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
    @DisplayName("Test that the localunit endpoints returns all beneficiaries of the localunit")
    public void testGetAllBeneficiariesOfLocalUnit() throws Exception {
        BeneficiaryResponse beneficiaryResponse = new BeneficiaryResponse(
                null,
                "benefUser",
                "beneficiaryFirstName",
                "beneficiaryLastName",
                null,
                "+33 6 00 00 00 00",
                true,
                ID.of(1L)
        );

        mockMvc.perform(get("/beneficiaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].username").value(beneficiaryResponse.getUsername()))
                .andExpect(jsonPath("$[0].firstName").value(beneficiaryResponse.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(beneficiaryResponse.getLastName()))
                .andExpect(jsonPath("$[0].phoneNumber").value(beneficiaryResponse.getPhoneNumber()))
                .andExpect(jsonPath("$[0].isValidated").value(beneficiaryResponse.isValidated()))
                .andExpect(jsonPath("$[0].localUnitId").value(beneficiaryResponse.getLocalUnitId()));
    }
}
