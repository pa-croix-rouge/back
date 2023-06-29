package fr.croixrouge.exposition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import fr.croixrouge.config.InDBMockRepositoryConfig;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.exposition.dto.CreateStorageDTO;
import fr.croixrouge.exposition.dto.StorageResponse;
import fr.croixrouge.exposition.dto.core.AddressDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({InDBMockRepositoryConfig.class, MockRepositoryConfig.class})
class StorageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;

    @BeforeEach
    public void setUp() throws Exception {
        LoginRequest loginRequest = new LoginRequest("defaultUser", "defaultPassword");

        String result = mockMvc.perform(post("/login/volunteer")
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
        mockMvc.perform(get("/storage/-1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test that the storage post endpoint returns OK when given a correct storage")
    public void productAddSuccessTest() throws Exception {
        AddressDTO addressDTO = new AddressDTO("91", "91240", "St Michel sur Orge", "76 rue des Liers");
        CreateStorageDTO createStorageDTO = new CreateStorageDTO("newStorage", 1L, addressDTO);

        var res = mockMvc.perform(post("/storage")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createStorageDTO)))
                .andExpect(status().isOk());

        String id = JsonPath.read(res.andReturn().getResponse().getContentAsString(), "$.value").toString();

        mockMvc.perform(get("/storage/" + id)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(createStorageDTO.getName()))
                .andExpect(jsonPath("$.address.departmentCode").value(addressDTO.getDepartmentCode()))
                .andExpect(jsonPath("$.address.postalCode").value(addressDTO.getPostalCode()))
                .andExpect(jsonPath("$.address.city").value(addressDTO.getCity()))
                .andExpect(jsonPath("$.address.streetNumberAndName").value(addressDTO.getStreetNumberAndName()))
                .andExpect(jsonPath("$.localUnitId").value("1"));
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

    @Test
    @DisplayName("Test that the storage endpoint returns a list of storage matching your local unit id")
    public void storageAllSuccessTest() throws Exception {
        AddressDTO addressDTO = new AddressDTO(
                "91",
                "91240",
                "St Michel sur Orge",
                "76 rue des Liers"
        );

        StorageResponse storageResponse1 = new StorageResponse(
                1L,
                "defaultStorage",
                addressDTO,
                1L);

        StorageResponse storageResponse2 = new StorageResponse(
                3L,
                "newStorage",
                addressDTO,
                1L);

        mockMvc.perform(get("/storage")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(storageResponse1.getId()))
                .andExpect(jsonPath("$[0].name").value(storageResponse1.getName()))
                .andExpect(jsonPath("$[0].address.departmentCode").value(storageResponse1.getAddress().getDepartmentCode()))
                .andExpect(jsonPath("$[0].address.postalCode").value(storageResponse1.getAddress().getPostalCode()))
                .andExpect(jsonPath("$[0].address.city").value(storageResponse1.getAddress().getCity()))
                .andExpect(jsonPath("$[0].address.streetNumberAndName").value(storageResponse1.getAddress().getStreetNumberAndName()))
                .andExpect(jsonPath("$[0].localUnitId").value(storageResponse1.getLocalUnitId()))
                .andExpect(jsonPath("$[1].id").value(storageResponse2.getId()))
                .andExpect(jsonPath("$[1].name").value(storageResponse2.getName()))
                .andExpect(jsonPath("$[1].address.departmentCode").value(storageResponse2.getAddress().getDepartmentCode()))
                .andExpect(jsonPath("$[1].address.postalCode").value(storageResponse2.getAddress().getPostalCode()))
                .andExpect(jsonPath("$[1].address.city").value(storageResponse2.getAddress().getCity()))
                .andExpect(jsonPath("$[1].address.streetNumberAndName").value(storageResponse2.getAddress().getStreetNumberAndName()))
                .andExpect(jsonPath("$[1].localUnitId").value(storageResponse2.getLocalUnitId()));
    }

    @Test
    @DisplayName("Test that the storage endpoint returns a list of storage which is empty matching your local unit id")
    public void storageAllEmptySuccessTest() throws Exception {
        LoginRequest loginRequest = new LoginRequest("SLUManager", "SLUPassword");

        String result = mockMvc.perform(post("/login/volunteer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String southernJwtToken = objectMapper.readTree(result).get("jwtToken").asText();

        mockMvc.perform(get("/storage")
                        .header("Authorization", "Bearer " + southernJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}
