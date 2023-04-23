package fr.croixrouge.exposition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.exposition.dto.AddressDTO;
import fr.croixrouge.exposition.dto.CreateStorageDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(MockRepositoryConfig.class)
class StorageControllerTest {

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
    @DisplayName("Test that the storage endpoint returns a storage when given a correct storage id")
    public void storageIdSuccessTest() throws Exception {
        mockMvc.perform(get("/storage/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    @DisplayName("Test that the storage endpoint returns a 404 when given a incorrect storage id")
    public void productIdFailedTest() throws Exception {
        mockMvc.perform(get("/storage/invalid-storage-id")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test that the storage post endpoint returns OK when given a correct storage")
    public void productAddSuccessTest() throws Exception {
        AddressDTO addressDTO = new AddressDTO("91", "91240", "St Michel sur Orge", "760 rue des Liers");
        CreateStorageDTO createStorageDTO = new CreateStorageDTO("1", addressDTO);

        var res = mockMvc.perform(post("/storage")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createStorageDTO)))
                .andExpect(status().isOk());

        String id = JsonPath.read(res.andReturn().getResponse().getContentAsString(), "$.value");

        mockMvc.perform(get("/storage/" + id)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.address.departmentCode").value(addressDTO.getDepartmentCode()))
                .andExpect(jsonPath("$.address.postalCode").value(addressDTO.getPostalCode()))
                .andExpect(jsonPath("$.address.city").value(addressDTO.getCity()))
                .andExpect(jsonPath("$.address.streetNumberAndName").value(addressDTO.getStreetNumberAndName()))
                .andExpect(jsonPath("$.localUnit.id").value("1"));
    }

    @Test
    @DisplayName("Test that the storage delete endpoint returns OK when given a correct storage")
    public void productDeleteSuccessTest() throws Exception {
        String id = "2";

        mockMvc.perform(delete("/storage/" + id)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/storage/" + id)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}