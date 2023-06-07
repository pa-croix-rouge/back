package fr.croixrouge.exposition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.croixrouge.config.InDBMockRepositoryConfig;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.exposition.dto.core.LoginRequest;
import fr.croixrouge.exposition.dto.product.CreateFoodProductDTO;
import fr.croixrouge.storage.model.product.FoodConservation;
import fr.croixrouge.storage.model.quantifier.WeightUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({InDBMockRepositoryConfig.class, MockRepositoryConfig.class})
class FoodProductControllerTest {

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
    @DisplayName("Test that the endpoint returns a food product when given a correct id")
    public void productIdSuccessTest() throws Exception {
        mockMvc.perform(get("/product/food/3")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.name").value("FoodProduct 1"))
                .andExpect(jsonPath("$.quantity.measurementUnit").value(WeightUnit.KILOGRAM.getName()))
                .andExpect(jsonPath("$.quantity.value").value(1))
                .andExpect(jsonPath("$.foodConservation").value("ROOM_TEMPERATURE"))
                .andExpect(jsonPath("$.expirationDate").value(LocalDateTime.of(2023, 5, 1, 15, 14, 1).toString()))
                .andExpect(jsonPath("$.optimalConsumptionDate").value(LocalDateTime.of(2023, 4, 10, 15, 14, 1).toString()));


    }


    @Test
    @DisplayName("Test that the endpoint returns a 404 when given a incorrect food product id")
    public void productIdFailedTest() throws Exception {
        mockMvc.perform(get("/product/food/-1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test that the post endpoint returns OK when given a correct food product")
    public void productAddSuccessTest() throws Exception {
        CreateFoodProductDTO createProductDTO = new CreateFoodProductDTO("new Product",
                new QuantifierDTO(WeightUnit.KILOGRAM.getName(), 1),
                FoodConservation.ROOM_TEMPERATURE,
                LocalDateTime.of(2023, 5, 1, 15, 14, 1),
                LocalDateTime.of(2023, 4, 10, 15, 14, 1),
                1);


        var res = mockMvc.perform(post("/product/food")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProductDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(res).get("value").asText();

        mockMvc.perform(get("/product/food/" + id)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(createProductDTO.getName()))
                .andExpect(jsonPath("$.quantity.measurementUnit").value(createProductDTO.getQuantity().getMeasurementUnit()))
                .andExpect(jsonPath("$.quantity.value").value(createProductDTO.getQuantity().getValue()))
                .andExpect(jsonPath("$.foodConservation").value(createProductDTO.getFoodConservation().toString()))
                .andExpect(jsonPath("$.expirationDate").value(createProductDTO.getExpirationDate().toString()))
                .andExpect(jsonPath("$.optimalConsumptionDate").value(createProductDTO.getOptimalConsumptionDate().toString()));
    }

    @Test
    @DisplayName("Test that the delete endpoint returns OK when given a correct food product id")
    public void productDeleteSuccessTest() throws Exception {
        String id = "4";

        mockMvc.perform(delete("/product/food/" + id)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/product/food/" + id)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
