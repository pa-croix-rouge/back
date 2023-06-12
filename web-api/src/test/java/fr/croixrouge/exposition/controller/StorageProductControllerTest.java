package fr.croixrouge.exposition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.croixrouge.config.InDBMockRepositoryConfig;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.exposition.dto.core.LoginRequest;
import fr.croixrouge.exposition.dto.product.ClothStorageProductResponse;
import fr.croixrouge.exposition.dto.product.FoodStorageProductResponse;
import fr.croixrouge.exposition.dto.product.StorageProductStatsResponse;
import fr.croixrouge.storage.model.product.ClothSize;
import fr.croixrouge.storage.model.product.FoodConservation;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({InDBMockRepositoryConfig.class, MockRepositoryConfig.class})
public class StorageProductControllerTest {

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
    @DisplayName("Test that the storage product endpoint returns a list of products for the requested storage")
    public void testGetProductsByStorageSuccess() throws Exception {
        ClothStorageProductResponse chemise1 = new ClothStorageProductResponse(
                1L,
                3L,
                3L,
                1L,
                "Chemises blanches",
                10,
                "20.0",
                "pièce(s)",
                ClothSize.S);
        ClothStorageProductResponse chemise2 = new ClothStorageProductResponse(
                2L,
                4L,
                4L,
                1L,
                "Chemises blanches",
                10,
                "20.0",
                "pièce(s)",
                ClothSize.M);
        ClothStorageProductResponse chemise3 = new ClothStorageProductResponse(
                3L,
                5L,
                5L,
                1L,
                "Chemises blanches",
                10,
                "20.0",
                "pièce(s)",
                ClothSize.L);
        ClothStorageProductResponse chemise4 = new ClothStorageProductResponse(
                4L,
                6L,
                6L,
                1L,
                "Chemises blanches",
                10,
                "20.0",
                "pièce(s)",
                ClothSize.XL);
        ClothStorageProductResponse chemise5 = new ClothStorageProductResponse(
                5L,
                7L,
                7L,
                1L,
                "Chemises blanches",
                10,
                "20.0",
                "pièce(s)",
                ClothSize.XXL);
        FoodStorageProductResponse food1 = new FoodStorageProductResponse(
                1L,
                8L,
                8L,
                1L,
                "Pommes",
                10,
                "1.0",
                "kilogram",
                FoodConservation.ROOM_TEMPERATURE,
                LocalDateTime.of(2023, 5, 1, 15, 14, 1),
                LocalDateTime.of(2023, 4, 10, 15, 14, 1));
        FoodStorageProductResponse food2 = new FoodStorageProductResponse(
                2L,
                9L,
                9L,
                1L,
                "Pates",
                10,
                "1.0",
                "kilogram",
                FoodConservation.ROOM_TEMPERATURE,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now());

        mockMvc.perform(get("/storage/product/" + chemise1.getStorageId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clothProducts").isArray())
                .andExpect(jsonPath("$.clothProducts").isNotEmpty())
                .andExpect(jsonPath("$.clothProducts[0].id").value(chemise1.getId()))
                .andExpect(jsonPath("$.clothProducts[0].productId").value(chemise1.getProductId()))
                .andExpect(jsonPath("$.clothProducts[0].storageProductId").value(chemise1.getStorageProductId()))
                .andExpect(jsonPath("$.clothProducts[0].storageId").value(chemise1.getStorageId()))
                .andExpect(jsonPath("$.clothProducts[0].productName").value(chemise1.getProductName()))
                .andExpect(jsonPath("$.clothProducts[0].quantity").value(chemise1.getQuantity()))
                .andExpect(jsonPath("$.clothProducts[0].quantifierQuantity").value(chemise1.getQuantifierQuantity()))
                .andExpect(jsonPath("$.clothProducts[0].quantifierName").value(chemise1.getQuantifierName()))
                .andExpect(jsonPath("$.clothProducts[0].size").value(chemise1.getSize().toString()))
                .andExpect(jsonPath("$.clothProducts[1].id").value(chemise2.getId()))
                .andExpect(jsonPath("$.clothProducts[1].productId").value(chemise2.getProductId()))
                .andExpect(jsonPath("$.clothProducts[1].storageProductId").value(chemise2.getStorageProductId()))
                .andExpect(jsonPath("$.clothProducts[1].storageId").value(chemise2.getStorageId()))
                .andExpect(jsonPath("$.clothProducts[1].productName").value(chemise2.getProductName()))
                .andExpect(jsonPath("$.clothProducts[1].quantity").value(chemise2.getQuantity()))
                .andExpect(jsonPath("$.clothProducts[1].quantifierQuantity").value(chemise2.getQuantifierQuantity()))
                .andExpect(jsonPath("$.clothProducts[1].quantifierName").value(chemise2.getQuantifierName()))
                .andExpect(jsonPath("$.clothProducts[1].size").value(chemise2.getSize().toString()))
                .andExpect(jsonPath("$.clothProducts[2].id").value(chemise3.getId()))
                .andExpect(jsonPath("$.clothProducts[2].productId").value(chemise3.getProductId()))
                .andExpect(jsonPath("$.clothProducts[2].storageProductId").value(chemise3.getStorageProductId()))
                .andExpect(jsonPath("$.clothProducts[2].storageId").value(chemise3.getStorageId()))
                .andExpect(jsonPath("$.clothProducts[2].productName").value(chemise3.getProductName()))
                .andExpect(jsonPath("$.clothProducts[2].quantity").value(chemise3.getQuantity()))
                .andExpect(jsonPath("$.clothProducts[2].quantifierQuantity").value(chemise3.getQuantifierQuantity()))
                .andExpect(jsonPath("$.clothProducts[2].quantifierName").value(chemise3.getQuantifierName()))
                .andExpect(jsonPath("$.clothProducts[2].size").value(chemise3.getSize().toString()))
                .andExpect(jsonPath("$.clothProducts[3].id").value(chemise4.getId()))
                .andExpect(jsonPath("$.clothProducts[3].productId").value(chemise4.getProductId()))
                .andExpect(jsonPath("$.clothProducts[3].storageProductId").value(chemise4.getStorageProductId()))
                .andExpect(jsonPath("$.clothProducts[3].storageId").value(chemise4.getStorageId()))
                .andExpect(jsonPath("$.clothProducts[3].productName").value(chemise4.getProductName()))
                .andExpect(jsonPath("$.clothProducts[3].quantity").value(chemise4.getQuantity()))
                .andExpect(jsonPath("$.clothProducts[3].quantifierQuantity").value(chemise4.getQuantifierQuantity()))
                .andExpect(jsonPath("$.clothProducts[3].quantifierName").value(chemise4.getQuantifierName()))
                .andExpect(jsonPath("$.clothProducts[3].size").value(chemise4.getSize().toString()))
                .andExpect(jsonPath("$.clothProducts[4].id").value(chemise5.getId()))
                .andExpect(jsonPath("$.clothProducts[4].productId").value(chemise5.getProductId()))
                .andExpect(jsonPath("$.clothProducts[4].storageProductId").value(chemise5.getStorageProductId()))
                .andExpect(jsonPath("$.clothProducts[4].storageId").value(chemise5.getStorageId()))
                .andExpect(jsonPath("$.clothProducts[4].productName").value(chemise5.getProductName()))
                .andExpect(jsonPath("$.clothProducts[4].quantity").value(chemise5.getQuantity()))
                .andExpect(jsonPath("$.clothProducts[4].quantifierQuantity").value(chemise5.getQuantifierQuantity()))
                .andExpect(jsonPath("$.clothProducts[4].quantifierName").value(chemise5.getQuantifierName()))
                .andExpect(jsonPath("$.clothProducts[4].size").value(chemise5.getSize().toString()))
                .andExpect(jsonPath("$.foodProducts").isArray())
                .andExpect(jsonPath("$.foodProducts").isNotEmpty())
                .andExpect(jsonPath("$.foodProducts[0].id").value(food1.getId()))
                .andExpect(jsonPath("$.foodProducts[0].productId").value(food1.getProductId()))
                .andExpect(jsonPath("$.foodProducts[0].storageProductId").value(food1.getStorageProductId()))
                .andExpect(jsonPath("$.foodProducts[0].storageId").value(food1.getStorageId()))
                .andExpect(jsonPath("$.foodProducts[0].productName").value(food1.getProductName()))
                .andExpect(jsonPath("$.foodProducts[0].quantity").value(food1.getQuantity()))
                .andExpect(jsonPath("$.foodProducts[0].quantifierQuantity").value(food1.getQuantifierQuantity()))
                .andExpect(jsonPath("$.foodProducts[0].quantifierName").value(food1.getQuantifierName()))
                .andExpect(jsonPath("$.foodProducts[0].foodConservation").value(food1.getFoodConservation().toString()))
                .andExpect(jsonPath("$.foodProducts[0].expirationDate").value(food1.getExpirationDate().toString()))
                .andExpect(jsonPath("$.foodProducts[0].optimalConsumptionDate").value(food1.getOptimalConsumptionDate().toString()))
                .andExpect(jsonPath("$.foodProducts[1].id").value(food2.getId()))
                .andExpect(jsonPath("$.foodProducts[1].productId").value(food2.getProductId()))
                .andExpect(jsonPath("$.foodProducts[1].storageProductId").value(food2.getStorageProductId()))
                .andExpect(jsonPath("$.foodProducts[1].storageId").value(food2.getStorageId()))
                .andExpect(jsonPath("$.foodProducts[1].productName").value(food2.getProductName()))
                .andExpect(jsonPath("$.foodProducts[1].quantity").value(food2.getQuantity()))
                .andExpect(jsonPath("$.foodProducts[1].quantifierQuantity").value(food2.getQuantifierQuantity()))
                .andExpect(jsonPath("$.foodProducts[1].quantifierName").value(food2.getQuantifierName()))
                .andExpect(jsonPath("$.foodProducts[1].foodConservation").value(food2.getFoodConservation().toString()))
                .andExpect(jsonPath("$.foodProducts[1].expirationDate").exists())
                .andExpect(jsonPath("$.foodProducts[1].optimalConsumptionDate").exists());
    }

    @Test
    @DisplayName("Test that the storage product endpoint returns a list of products based on local unit id")
    public void testGetProductsByLocalUnitSuccess() throws Exception {
        ClothStorageProductResponse chemise1 = new ClothStorageProductResponse(
                1L,
                3L,
                3L,
                1L,
                "Chemises blanches",
                10,
                "20.0",
                "pièce(s)",
                ClothSize.S);
        ClothStorageProductResponse chemise2 = new ClothStorageProductResponse(
                2L,
                4L,
                4L,
                1L,
                "Chemises blanches",
                10,
                "20.0",
                "pièce(s)",
                ClothSize.M);
        ClothStorageProductResponse chemise3 = new ClothStorageProductResponse(
                3L,
                5L,
                5L,
                1L,
                "Chemises blanches",
                10,
                "20.0",
                "pièce(s)",
                ClothSize.L);
        ClothStorageProductResponse chemise4 = new ClothStorageProductResponse(
                4L,
                6L,
                6L,
                1L,
                "Chemises blanches",
                10,
                "20.0",
                "pièce(s)",
                ClothSize.XL);
        ClothStorageProductResponse chemise5 = new ClothStorageProductResponse(
                5L,
                7L,
                7L,
                1L,
                "Chemises blanches",
                10,
                "20.0",
                "pièce(s)",
                ClothSize.XXL);
        FoodStorageProductResponse food1 = new FoodStorageProductResponse(
                1L,
                8L,
                8L,
                1L,
                "Pommes",
                10,
                "1.0",
                "kilogram",
                FoodConservation.ROOM_TEMPERATURE,
                LocalDateTime.of(2023, 5, 1, 15, 14, 1),
                LocalDateTime.of(2023, 4, 10, 15, 14, 1));
        FoodStorageProductResponse food2 = new FoodStorageProductResponse(
                2L,
                9L,
                9L,
                1L,
                "Pates",
                10,
                "1.0",
                "kilogram",
                FoodConservation.ROOM_TEMPERATURE,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now());

        mockMvc.perform(get("/storage/product/localunit")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clothProducts").isArray())
                .andExpect(jsonPath("$.clothProducts").isNotEmpty())
                .andExpect(jsonPath("$.clothProducts[0].id").value(chemise1.getId()))
                .andExpect(jsonPath("$.clothProducts[0].productId").value(chemise1.getProductId()))
                .andExpect(jsonPath("$.clothProducts[0].storageProductId").value(chemise1.getStorageProductId()))
                .andExpect(jsonPath("$.clothProducts[0].storageId").value(chemise1.getStorageId()))
                .andExpect(jsonPath("$.clothProducts[0].productName").value(chemise1.getProductName()))
                .andExpect(jsonPath("$.clothProducts[0].quantity").value(chemise1.getQuantity()))
                .andExpect(jsonPath("$.clothProducts[0].quantifierQuantity").value(chemise1.getQuantifierQuantity()))
                .andExpect(jsonPath("$.clothProducts[0].quantifierName").value(chemise1.getQuantifierName()))
                .andExpect(jsonPath("$.clothProducts[0].size").value(chemise1.getSize().toString()))
                .andExpect(jsonPath("$.clothProducts[1].id").value(chemise2.getId()))
                .andExpect(jsonPath("$.clothProducts[1].productId").value(chemise2.getProductId()))
                .andExpect(jsonPath("$.clothProducts[1].storageProductId").value(chemise2.getStorageProductId()))
                .andExpect(jsonPath("$.clothProducts[1].storageId").value(chemise2.getStorageId()))
                .andExpect(jsonPath("$.clothProducts[1].productName").value(chemise2.getProductName()))
                .andExpect(jsonPath("$.clothProducts[1].quantity").value(chemise2.getQuantity()))
                .andExpect(jsonPath("$.clothProducts[1].quantifierQuantity").value(chemise2.getQuantifierQuantity()))
                .andExpect(jsonPath("$.clothProducts[1].quantifierName").value(chemise2.getQuantifierName()))
                .andExpect(jsonPath("$.clothProducts[1].size").value(chemise2.getSize().toString()))
                .andExpect(jsonPath("$.clothProducts[2].id").value(chemise3.getId()))
                .andExpect(jsonPath("$.clothProducts[2].productId").value(chemise3.getProductId()))
                .andExpect(jsonPath("$.clothProducts[2].storageProductId").value(chemise3.getStorageProductId()))
                .andExpect(jsonPath("$.clothProducts[2].storageId").value(chemise3.getStorageId()))
                .andExpect(jsonPath("$.clothProducts[2].productName").value(chemise3.getProductName()))
                .andExpect(jsonPath("$.clothProducts[2].quantity").value(chemise3.getQuantity()))
                .andExpect(jsonPath("$.clothProducts[2].quantifierQuantity").value(chemise3.getQuantifierQuantity()))
                .andExpect(jsonPath("$.clothProducts[2].quantifierName").value(chemise3.getQuantifierName()))
                .andExpect(jsonPath("$.clothProducts[2].size").value(chemise3.getSize().toString()))
                .andExpect(jsonPath("$.clothProducts[3].id").value(chemise4.getId()))
                .andExpect(jsonPath("$.clothProducts[3].productId").value(chemise4.getProductId()))
                .andExpect(jsonPath("$.clothProducts[3].storageProductId").value(chemise4.getStorageProductId()))
                .andExpect(jsonPath("$.clothProducts[3].storageId").value(chemise4.getStorageId()))
                .andExpect(jsonPath("$.clothProducts[3].productName").value(chemise4.getProductName()))
                .andExpect(jsonPath("$.clothProducts[3].quantity").value(chemise4.getQuantity()))
                .andExpect(jsonPath("$.clothProducts[3].quantifierQuantity").value(chemise4.getQuantifierQuantity()))
                .andExpect(jsonPath("$.clothProducts[3].quantifierName").value(chemise4.getQuantifierName()))
                .andExpect(jsonPath("$.clothProducts[3].size").value(chemise4.getSize().toString()))
                .andExpect(jsonPath("$.clothProducts[4].id").value(chemise5.getId()))
                .andExpect(jsonPath("$.clothProducts[4].productId").value(chemise5.getProductId()))
                .andExpect(jsonPath("$.clothProducts[4].storageProductId").value(chemise5.getStorageProductId()))
                .andExpect(jsonPath("$.clothProducts[4].storageId").value(chemise5.getStorageId()))
                .andExpect(jsonPath("$.clothProducts[4].productName").value(chemise5.getProductName()))
                .andExpect(jsonPath("$.clothProducts[4].quantity").value(chemise5.getQuantity()))
                .andExpect(jsonPath("$.clothProducts[4].quantifierQuantity").value(chemise5.getQuantifierQuantity()))
                .andExpect(jsonPath("$.clothProducts[4].quantifierName").value(chemise5.getQuantifierName()))
                .andExpect(jsonPath("$.clothProducts[4].size").value(chemise5.getSize().toString()))
                .andExpect(jsonPath("$.foodProducts").isArray())
                .andExpect(jsonPath("$.foodProducts").isNotEmpty())
                .andExpect(jsonPath("$.foodProducts[0].id").value(food1.getId()))
                .andExpect(jsonPath("$.foodProducts[0].productId").value(food1.getProductId()))
                .andExpect(jsonPath("$.foodProducts[0].storageProductId").value(food1.getStorageProductId()))
                .andExpect(jsonPath("$.foodProducts[0].storageId").value(food1.getStorageId()))
                .andExpect(jsonPath("$.foodProducts[0].productName").value(food1.getProductName()))
                .andExpect(jsonPath("$.foodProducts[0].quantity").value(food1.getQuantity()))
                .andExpect(jsonPath("$.foodProducts[0].quantifierQuantity").value(food1.getQuantifierQuantity()))
                .andExpect(jsonPath("$.foodProducts[0].quantifierName").value(food1.getQuantifierName()))
                .andExpect(jsonPath("$.foodProducts[0].foodConservation").value(food1.getFoodConservation().toString()))
                .andExpect(jsonPath("$.foodProducts[0].expirationDate").value(food1.getExpirationDate().toString()))
                .andExpect(jsonPath("$.foodProducts[0].optimalConsumptionDate").value(food1.getOptimalConsumptionDate().toString()))
                .andExpect(jsonPath("$.foodProducts[1].id").value(food2.getId()))
                .andExpect(jsonPath("$.foodProducts[1].productId").value(food2.getProductId()))
                .andExpect(jsonPath("$.foodProducts[1].storageProductId").value(food2.getStorageProductId()))
                .andExpect(jsonPath("$.foodProducts[1].storageId").value(food2.getStorageId()))
                .andExpect(jsonPath("$.foodProducts[1].productName").value(food2.getProductName()))
                .andExpect(jsonPath("$.foodProducts[1].quantity").value(food2.getQuantity()))
                .andExpect(jsonPath("$.foodProducts[1].quantifierQuantity").value(food2.getQuantifierQuantity()))
                .andExpect(jsonPath("$.foodProducts[1].quantifierName").value(food2.getQuantifierName()))
                .andExpect(jsonPath("$.foodProducts[1].foodConservation").value(food2.getFoodConservation().toString()))
                .andExpect(jsonPath("$.foodProducts[1].expirationDate").exists())
                .andExpect(jsonPath("$.foodProducts[1].optimalConsumptionDate").exists());
    }

    @Test
    @DisplayName("Test that the storage stats endpoints returns the correct stats")
    public void testGetStatsSuccess() throws Exception {
        StorageProductStatsResponse statsResponse = new StorageProductStatsResponse(20, 50);

        mockMvc.perform(get("/storage/product/stats")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalFoodQuantity").value(statsResponse.getTotalFoodQuantity()))
                .andExpect(jsonPath("$.totalClothesQuantity").value(statsResponse.getTotalClothesQuantity()));
    }
}
