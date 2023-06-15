package fr.croixrouge.exposition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.croixrouge.config.InDBMockRepositoryConfig;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.exposition.dto.core.LoginRequest;
import fr.croixrouge.exposition.dto.product.ClothProductResponse;
import fr.croixrouge.storage.model.product.ClothSize;
import fr.croixrouge.storage.model.quantifier.NumberedUnit;
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
public class ClothProductControllerTest {

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
    @DisplayName("Test that the id endpoint returns a cloth product when given a valid id")
    public void testGetClothByIdSuccessTest() throws Exception {
        ClothProductResponse clothProductResponse = new ClothProductResponse(
                1L,
                3L,
                "Chemises blanches",
                new QuantifierDTO(NumberedUnit.NUMBER.getName(), 20),
                ClothSize.S);
        mockMvc.perform(get("/product/cloth/" + clothProductResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clothProductResponse.getId().toString()))
                .andExpect(jsonPath("$.productId").value(clothProductResponse.getProductId().toString()))
                .andExpect(jsonPath("$.name").value(clothProductResponse.getName()))
                .andExpect(jsonPath("$.quantity.measurementUnit").value(clothProductResponse.getQuantity().getMeasurementUnit()))
                .andExpect(jsonPath("$.quantity.value").value(clothProductResponse.getQuantity().getValue()))
                .andExpect(jsonPath("$.size").value(clothProductResponse.getSize().toString()));
    }

    @Test
    @DisplayName("Test that the id endpoint returns a cloth product when given a valid id")
    public void testGetClothByIdFailTest() throws Exception {
        mockMvc.perform(get("/product/cloth/" + "-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test that the id endpoint returns a cloth product when given a valid id")
    public void testGetAllClothSuccessTest() throws Exception {
        ClothProductResponse clothProductResponse1 = new ClothProductResponse(
                1L,
                3L,
                "Chemises blanches",
                new QuantifierDTO(NumberedUnit.NUMBER.getName(), 20),
                ClothSize.S);
        ClothProductResponse clothProductResponse2 = new ClothProductResponse(
                2L,
                4L,
                "Chemises blanches",
                new QuantifierDTO(NumberedUnit.NUMBER.getName(), 20),
                ClothSize.M);
        ClothProductResponse clothProductResponse3 = new ClothProductResponse(
                3L,
                5L,
                "Chemises blanches",
                new QuantifierDTO(NumberedUnit.NUMBER.getName(), 20),
                ClothSize.L);
        ClothProductResponse clothProductResponse4 = new ClothProductResponse(
                4L,
                6L,
                "Chemises blanches",
                new QuantifierDTO(NumberedUnit.NUMBER.getName(), 20),
                ClothSize.XL);
        ClothProductResponse clothProductResponse5 = new ClothProductResponse(
                5L,
                7L,
                "Chemises blanches",
                new QuantifierDTO(NumberedUnit.NUMBER.getName(), 20),
                ClothSize.XXL);

        mockMvc.perform(get("/product/cloth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(clothProductResponse1.getId().toString()))
                .andExpect(jsonPath("$[0].productId").value(clothProductResponse1.getProductId().toString()))
                .andExpect(jsonPath("$[0].name").value(clothProductResponse1.getName()))
                .andExpect(jsonPath("$[0].quantity.measurementUnit").value(clothProductResponse1.getQuantity().getMeasurementUnit()))
                .andExpect(jsonPath("$[0].quantity.value").value(clothProductResponse1.getQuantity().getValue()))
                .andExpect(jsonPath("$[0].size").value(clothProductResponse1.getSize().toString()))
                .andExpect(jsonPath("$[1].id").value(clothProductResponse2.getId().toString()))
                .andExpect(jsonPath("$[1].productId").value(clothProductResponse2.getProductId().toString()))
                .andExpect(jsonPath("$[1].name").value(clothProductResponse2.getName()))
                .andExpect(jsonPath("$[1].quantity.measurementUnit").value(clothProductResponse2.getQuantity().getMeasurementUnit()))
                .andExpect(jsonPath("$[1].quantity.value").value(clothProductResponse2.getQuantity().getValue()))
                .andExpect(jsonPath("$[1].size").value(clothProductResponse2.getSize().toString()))
                .andExpect(jsonPath("$[2].id").value(clothProductResponse3.getId().toString()))
                .andExpect(jsonPath("$[2].productId").value(clothProductResponse3.getProductId().toString()))
                .andExpect(jsonPath("$[2].name").value(clothProductResponse3.getName()))
                .andExpect(jsonPath("$[2].quantity.measurementUnit").value(clothProductResponse3.getQuantity().getMeasurementUnit()))
                .andExpect(jsonPath("$[2].quantity.value").value(clothProductResponse3.getQuantity().getValue()))
                .andExpect(jsonPath("$[2].size").value(clothProductResponse3.getSize().toString()))
                .andExpect(jsonPath("$[3].id").value(clothProductResponse4.getId().toString()))
                .andExpect(jsonPath("$[3].productId").value(clothProductResponse4.getProductId().toString()))
                .andExpect(jsonPath("$[3].name").value(clothProductResponse4.getName()))
                .andExpect(jsonPath("$[3].quantity.measurementUnit").value(clothProductResponse4.getQuantity().getMeasurementUnit()))
                .andExpect(jsonPath("$[3].quantity.value").value(clothProductResponse4.getQuantity().getValue()))
                .andExpect(jsonPath("$[3].size").value(clothProductResponse4.getSize().toString()))
                .andExpect(jsonPath("$[4].id").value(clothProductResponse5.getId().toString()))
                .andExpect(jsonPath("$[4].productId").value(clothProductResponse5.getProductId().toString()))
                .andExpect(jsonPath("$[4].name").value(clothProductResponse5.getName()))
                .andExpect(jsonPath("$[4].quantity.measurementUnit").value(clothProductResponse5.getQuantity().getMeasurementUnit()))
                .andExpect(jsonPath("$[4].quantity.value").value(clothProductResponse5.getQuantity().getValue()))
                .andExpect(jsonPath("$[4].size").value(clothProductResponse5.getSize().toString()));
    }
}
