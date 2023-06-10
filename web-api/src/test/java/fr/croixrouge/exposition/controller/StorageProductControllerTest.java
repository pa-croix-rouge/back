package fr.croixrouge.exposition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.croixrouge.config.InDBMockRepositoryConfig;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.exposition.dto.core.LoginRequest;
import fr.croixrouge.exposition.dto.product.StorageProductResponse;
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
        StorageProductResponse storageProductResponse = new StorageProductResponse(
                1L,
                1L,
                1L,
                "Product 1",
                10,
                "1.0",
                "kilogram"
        );

        mockMvc.perform(get("/storage/1/product")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].storageProductId").value(storageProductResponse.getStorageProductId()))
                .andExpect(jsonPath("$[0].productId").value(storageProductResponse.getProductId()))
                .andExpect(jsonPath("$[0].storageId").value(storageProductResponse.getStorageId()))
                .andExpect(jsonPath("$[0].productName").value(storageProductResponse.getProductName()))
                .andExpect(jsonPath("$[0].quantity").value(storageProductResponse.getQuantity()))
                .andExpect(jsonPath("$[0].quantifierQuantity").value(storageProductResponse.getQuantifierQuantity()))
                .andExpect(jsonPath("$[0].quantifierName").value(storageProductResponse.getQuantifierName()));
    }
}
