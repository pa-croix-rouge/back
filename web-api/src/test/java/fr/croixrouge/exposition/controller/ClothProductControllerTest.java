package fr.croixrouge.exposition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.croixrouge.config.InDBMockRepositoryConfig;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.exposition.dto.core.LoginRequest;
import fr.croixrouge.exposition.dto.product.ClothProductResponse;
import fr.croixrouge.exposition.dto.product.CreateClothProductDTO;
import fr.croixrouge.storage.model.product.ClothGender;
import fr.croixrouge.storage.model.product.ClothSize;
import fr.croixrouge.storage.model.quantifier.NumberedUnit;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({InDBMockRepositoryConfig.class, MockRepositoryConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ClothProductControllerTest {

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

    @Order(1)
    @Test
    @DisplayName("Test that the id endpoint returns a cloth product when given a valid id")
    public void testGetClothByIdSuccessTest() throws Exception {
        ClothProductResponse clothProductResponse = new ClothProductResponse(
                1L,
                3L,
                "Chemises blanches",
                new QuantifierDTO(NumberedUnit.NUMBER.getName(), 20),
                ClothSize.S.getLabel(),
                ClothGender.NOT_SPECIFIED.getLabel());
        mockMvc.perform(get("/product/cloth/" + clothProductResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clothProductResponse.getId().toString()))
                .andExpect(jsonPath("$.name").value(clothProductResponse.getName()))
                .andExpect(jsonPath("$.quantity.measurementUnit").value(clothProductResponse.getQuantity().getMeasurementUnit()))
                .andExpect(jsonPath("$.quantity.value").value(clothProductResponse.getQuantity().getValue()))
                .andExpect(jsonPath("$.size").value(clothProductResponse.getSize()))
                .andExpect(jsonPath("$.gender").value(clothProductResponse.getGender()));
    }

    @Order(2)
    @Test
    @DisplayName("Test that the id endpoint returns not found product when given an invalid id")
    public void testGetClothByIdFailTest() throws Exception {
        mockMvc.perform(get("/product/cloth/" + "-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Order(3)
    @Test
    @DisplayName("Test that the get endpoint returns a list of cloths product")
    public void testGetAllClothSuccessTest() throws Exception {
        ClothProductResponse clothProductResponse1 = new ClothProductResponse(
                1L,
                3L,
                "Chemises blanches",
                new QuantifierDTO(NumberedUnit.NUMBER.getName(), 20),
                ClothSize.S.getLabel(),
                ClothGender.NOT_SPECIFIED.getLabel());
        ClothProductResponse clothProductResponse2 = new ClothProductResponse(
                2L,
                4L,
                "Chemises blanches",
                new QuantifierDTO(NumberedUnit.NUMBER.getName(), 20),
                ClothSize.M.getLabel(),
                ClothGender.NOT_SPECIFIED.getLabel());
        ClothProductResponse clothProductResponse3 = new ClothProductResponse(
                3L,
                5L,
                "Chemises blanches",
                new QuantifierDTO(NumberedUnit.NUMBER.getName(), 20),
                ClothSize.L.getLabel(),
                ClothGender.NOT_SPECIFIED.getLabel());
        ClothProductResponse clothProductResponse4 = new ClothProductResponse(
                4L,
                6L,
                "Chemises blanches",
                new QuantifierDTO(NumberedUnit.NUMBER.getName(), 20),
                ClothSize.XL.getLabel(),
                ClothGender.NOT_SPECIFIED.getLabel());
        ClothProductResponse clothProductResponse5 = new ClothProductResponse(
                5L,
                7L,
                "Chemises blanches",
                new QuantifierDTO(NumberedUnit.NUMBER.getName(), 20),
                ClothSize.XXL.getLabel(),
                ClothGender.NOT_SPECIFIED.getLabel());

        mockMvc.perform(get("/product/cloth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(clothProductResponse1.getName()))
                .andExpect(jsonPath("$[0].quantity.measurementUnit").value(clothProductResponse1.getQuantity().getMeasurementUnit()))
                .andExpect(jsonPath("$[0].quantity.value").value(clothProductResponse1.getQuantity().getValue()))
                .andExpect(jsonPath("$[0].size").value(clothProductResponse1.getSize()))
                .andExpect(jsonPath("$[0].gender").value(clothProductResponse1.getGender()))
                .andExpect(jsonPath("$[1].name").value(clothProductResponse2.getName()))
                .andExpect(jsonPath("$[1].quantity.measurementUnit").value(clothProductResponse2.getQuantity().getMeasurementUnit()))
                .andExpect(jsonPath("$[1].quantity.value").value(clothProductResponse2.getQuantity().getValue()))
                .andExpect(jsonPath("$[1].size").value(clothProductResponse2.getSize()))
                .andExpect(jsonPath("$[1].gender").value(clothProductResponse2.getGender()))
                .andExpect(jsonPath("$[2].name").value(clothProductResponse3.getName()))
                .andExpect(jsonPath("$[2].quantity.measurementUnit").value(clothProductResponse3.getQuantity().getMeasurementUnit()))
                .andExpect(jsonPath("$[2].quantity.value").value(clothProductResponse3.getQuantity().getValue()))
                .andExpect(jsonPath("$[2].size").value(clothProductResponse3.getSize()))
                .andExpect(jsonPath("$[2].gender").value(clothProductResponse3.getGender()))
                .andExpect(jsonPath("$[3].name").value(clothProductResponse4.getName()))
                .andExpect(jsonPath("$[3].quantity.measurementUnit").value(clothProductResponse4.getQuantity().getMeasurementUnit()))
                .andExpect(jsonPath("$[3].quantity.value").value(clothProductResponse4.getQuantity().getValue()))
                .andExpect(jsonPath("$[3].size").value(clothProductResponse4.getSize()))
                .andExpect(jsonPath("$[3].gender").value(clothProductResponse4.getGender()))
                .andExpect(jsonPath("$[4].name").value(clothProductResponse5.getName()))
                .andExpect(jsonPath("$[4].quantity.measurementUnit").value(clothProductResponse5.getQuantity().getMeasurementUnit()))
                .andExpect(jsonPath("$[4].quantity.value").value(clothProductResponse5.getQuantity().getValue()))
                .andExpect(jsonPath("$[4].size").value(clothProductResponse5.getSize()))
                .andExpect(jsonPath("$[4].gender").value(clothProductResponse5.getGender()));
    }

    @Order(4)
    @Test
    @DisplayName("Test that the post endpoint creates a cloth product when given valid data")
    public void testCreateClothSuccessTest() throws Exception {
        CreateClothProductDTO clothProductRequest = new CreateClothProductDTO(
                "T-shirt blanc",
                new QuantifierDTO(NumberedUnit.NUMBER.getName(), 100),
                ClothSize.L.getLabel(),
                "1",
                1,
                ClothGender.MALE.getLabel(),
                null);

        String result = mockMvc.perform(post("/product/cloth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(clothProductRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(result).get("value").asText();

        mockMvc.perform(get("/product/cloth/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.productId").isNumber())
                .andExpect(jsonPath("$.name").value(clothProductRequest.getName()))
                .andExpect(jsonPath("$.quantity.measurementUnit").value(clothProductRequest.getQuantity().getMeasurementUnit()))
                .andExpect(jsonPath("$.quantity.value").value(clothProductRequest.getQuantity().getValue()))
                .andExpect(jsonPath("$.size").value(clothProductRequest.getSize()))
                .andExpect(jsonPath("$.gender").value(clothProductRequest.getGender()));
    }

    @Order(5)
    @Test
    @DisplayName("Test that the id endpoint updates a cloth product when given a valid id and data")
    public void testUpdateClothByIdSuccessTest() throws Exception {
        ClothProductResponse clothProductResponse = new ClothProductResponse(
                1L,
                3L,
                "Chemises blanches",
                new QuantifierDTO(NumberedUnit.NUMBER.getName(), 20),
                ClothSize.S.getLabel(),
                ClothGender.NOT_SPECIFIED.getLabel());
        mockMvc.perform(get("/product/cloth/" + clothProductResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(clothProductResponse.getName()))
                .andExpect(jsonPath("$.quantity.measurementUnit").value(clothProductResponse.getQuantity().getMeasurementUnit()))
                .andExpect(jsonPath("$.quantity.value").value(clothProductResponse.getQuantity().getValue()))
                .andExpect(jsonPath("$.size").value(clothProductResponse.getSize()))
                .andExpect(jsonPath("$.gender").value(clothProductResponse.getGender()));


        CreateClothProductDTO clothProductRequest = new CreateClothProductDTO(
                "Chemises blanches",
                new QuantifierDTO(NumberedUnit.NUMBER.getName(), 50),
                ClothSize.S.getLabel(),
                "1",
                1,
                ClothGender.FEMALE.getLabel(),
                null);

        mockMvc.perform(post("/product/cloth/" + clothProductResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(clothProductRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/product/cloth/" + clothProductResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(clothProductRequest.getName()))
                .andExpect(jsonPath("$.quantity.measurementUnit").value(clothProductRequest.getQuantity().getMeasurementUnit()))
                .andExpect(jsonPath("$.quantity.value").value(50))
                .andExpect(jsonPath("$.size").value(clothProductRequest.getSize()))
                .andExpect(jsonPath("$.gender").value(clothProductRequest.getGender()));
    }

    @Order(6)
    @Test
    @DisplayName("Test that the id endpoint deletes a cloth product when given a valid id")
    public void testDeleteClothByIdSuccessTest() throws Exception {
        String id = "1";
        mockMvc.perform(get("/product/cloth/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/product/cloth/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/product/cloth/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }
}
