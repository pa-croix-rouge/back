package fr.croixrouge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.exposition.dto.EventRequest;
import fr.croixrouge.exposition.dto.EventResponse;
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

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(MockRepositoryConfig.class)
public class EventControllerTest {

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
    @DisplayName("Test that the event endpoint returns an event when given a correct event id")
    public void eventIdSuccessTest() throws Exception {
        EventRequest eventRequest = new EventRequest("1");

        EventResponse eventResponse = new EventResponse(
            "Formation PSC1",
            "Formation au PSC1",
            LocalDateTime.of(2000, 6, 1, 10, 0).toString(),
            LocalDateTime.of(2000, 6, 1, 12, 0).toString(),
            "1",
            "1"
        );

        mockMvc.perform(get("/event")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(eventResponse.getName()))
            .andExpect(jsonPath("$.description").value(eventResponse.getDescription()))
            .andExpect(jsonPath("$.start").value(eventResponse.getStart()))
            .andExpect(jsonPath("$.end").value(eventResponse.getEnd()))
            .andExpect(jsonPath("$.referrerId").value(eventResponse.getReferrerId()))
            .andExpect(jsonPath("$.localUnitId").value(eventResponse.getLocalUnitId()));
    }

    @Test
    @DisplayName("Test that the event endpoint returns a 404 when given an incorrect event id")
    public void eventIdFailureTest() throws Exception {
        EventRequest eventRequest = new EventRequest("2");

        mockMvc.perform(get("/event")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventRequest)))
            .andExpect(status().isNotFound());
    }
}
