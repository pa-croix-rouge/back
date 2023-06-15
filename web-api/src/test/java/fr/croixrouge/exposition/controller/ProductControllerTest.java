package fr.croixrouge.exposition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import fr.croixrouge.config.InDBMockRepositoryConfig;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.exposition.dto.core.LoginRequest;
import fr.croixrouge.exposition.dto.product.CreateProductDTO;
import fr.croixrouge.storage.model.quantifier.WeightUnit;
import org.junit.jupiter.api.*;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import({InDBMockRepositoryConfig.class, MockRepositoryConfig.class})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;

    private static String createdProductId;

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
    @DisplayName("Test that the product endpoint returns a product when given a correct product id")
    public void productIdSuccessTest() throws Exception {
        mockMvc.perform(get("/product/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value("1"))
                .andExpect(jsonPath("$.name").value("Product 1"))
                .andExpect(jsonPath("$.quantity.measurementUnit").value(WeightUnit.KILOGRAM.getName()))
                .andExpect(jsonPath("$.quantity.value").value(1));
    }

 /*   @Test
    @DisplayName("Test that the product endpoint returns a FOOD product when given a correct product id")
    public void foodProductIdSuccessTest() throws Exception {
        mockMvc.perform(get("/product/3")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Product 1"))
                .andExpect(jsonPath("$.quantity.measurementUnit").value(WeightUnit.KILOGRAM.getName()))
                .andExpect(jsonPath("$.quantity.value").value(1));
    }*/

    @Test
    @DisplayName("Test that the product endpoint returns a 404 when given a incorrect product id")
    public void productIdFailedTest() throws Exception {
        mockMvc.perform(get("/product/-1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(1)
    @DisplayName("Test that the product post endpoint returns OK when given a correct product")
    public void productAddSuccessTest() throws Exception {
        CreateProductDTO createProductDTO = new CreateProductDTO("new Product", new QuantifierDTO(WeightUnit.KILOGRAM.getName(), 1));

        var res = mockMvc.perform(post("/product")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProductDTO)))
                .andExpect(status().isOk());

        createdProductId = JsonPath.read(res.andReturn().getResponse().getContentAsString(), "$.value").toString();

        mockMvc.perform(get("/product/" + createdProductId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(createdProductId))
                .andExpect(jsonPath("$.name").value(createProductDTO.getName()))
                .andExpect(jsonPath("$.quantity.measurementUnit").value(createProductDTO.getQuantity().getMeasurementUnit()))
                .andExpect(jsonPath("$.quantity.value").value(createProductDTO.getQuantity().getValue()));
    }

    @Test
    @Order(2)
    @DisplayName("Test that the product delete endpoint returns OK when given a correct product")
    public void productDeleteSuccessTest() throws Exception {

        mockMvc.perform(delete("/product/" + createdProductId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/product/" + createdProductId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}

