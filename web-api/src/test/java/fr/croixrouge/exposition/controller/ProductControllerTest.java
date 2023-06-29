package fr.croixrouge.exposition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import fr.croixrouge.config.InDBMockRepositoryConfig;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.exposition.dto.core.LoginRequest;
import fr.croixrouge.exposition.dto.product.CreateProductDTO;
import fr.croixrouge.storage.model.product.ClothGender;
import fr.croixrouge.storage.model.product.ClothSize;
import fr.croixrouge.storage.model.product.FoodConservation;
import fr.croixrouge.storage.model.quantifier.NumberedUnit;
import fr.croixrouge.storage.model.quantifier.VolumeUnit;
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
                .andExpect(jsonPath("$.name").value("Chemises blanches"))
                .andExpect(jsonPath("$.quantity.measurementUnit").value(NumberedUnit.NUMBER.getName()))
                .andExpect(jsonPath("$.quantity.value").value(20));
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
    @Order(1)
    @DisplayName("Test that the product endpoint returns a 404 when given a incorrect product id")
    public void productIdFailedTest() throws Exception {
        mockMvc.perform(get("/product/-1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(2)
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
    @Order(3)
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

    @Test
    @Order(4)
    @DisplayName("Test that the product units endpoint returns the list of units")
    public void productReturnsListOfUnits() throws Exception {
        mockMvc.perform(get("/product/units")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.units").isMap())
                .andExpect(jsonPath("$.units").isNotEmpty())
                .andExpect(jsonPath("$.units.kilogram").value(WeightUnit.KILOGRAM.getLabel()))
                .andExpect(jsonPath("$.units.gram").value(WeightUnit.GRAM.getLabel()))
                .andExpect(jsonPath("$.units.litre").value(VolumeUnit.LITER.getLabel()))
                .andExpect(jsonPath("$.units.millilitre").value(VolumeUnit.MILLILITER.getLabel()))
                .andExpect(jsonPath("$.units.decilitre").value(VolumeUnit.DECILITER.getLabel()))
                .andExpect(jsonPath("$.units.pièce").value(NumberedUnit.NUMBER.getLabel()));
//                .andExpect(jsonPath("$.units").value(NumberedUnit.UNKNOWN.getLabel()));
    }

    @Test
    @Order(5)
    @DisplayName("Test that the product conservation endpoint returns the list of units")
    public void productReturnsListOfConservations() throws Exception {
        mockMvc.perform(get("/product/conservations")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conservations").isArray())
                .andExpect(jsonPath("$.conservations").isNotEmpty())
                .andExpect(jsonPath("$.conservations[0]").value(FoodConservation.FROZEN.getLabel()))
                .andExpect(jsonPath("$.conservations[1]").value(FoodConservation.REFRIGERATED.getLabel()))
                .andExpect(jsonPath("$.conservations[2]").value(FoodConservation.ROOM_TEMPERATURE.getLabel()));
    }

    @Test
    @Order(6)
    @DisplayName("Test that the product sizes endpoint returns the list of sizes")
    public void productReturnsListOfClothSizes() throws Exception {
        mockMvc.perform(get("/product/sizes")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0]").value(ClothSize.UNKNOWN.getLabel()))
                .andExpect(jsonPath("$[1]").value(ClothSize.CHILD.getLabel()))
                .andExpect(jsonPath("$[2]").value(ClothSize.XS.getLabel()))
                .andExpect(jsonPath("$[3]").value(ClothSize.S.getLabel()))
                .andExpect(jsonPath("$[4]").value(ClothSize.M.getLabel()))
                .andExpect(jsonPath("$[5]").value(ClothSize.L.getLabel()))
                .andExpect(jsonPath("$[6]").value(ClothSize.XL.getLabel()))
                .andExpect(jsonPath("$[7]").value(ClothSize.XXL.getLabel()))
                .andExpect(jsonPath("$[8]").value(ClothSize.XXXL.getLabel()));
    }

    @Test
    @Order(7)
    @DisplayName("Test that the product genders endpoint returns the list of gender")
    public void productReturnsListOfClothGenders() throws Exception {
        mockMvc.perform(get("/product/genders")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0]").value(ClothGender.MALE.getLabel()))
                .andExpect(jsonPath("$[1]").value(ClothGender.FEMALE.getLabel()))
                .andExpect(jsonPath("$[2]").value(ClothGender.NOT_SPECIFIED.getLabel()));
    }
}

