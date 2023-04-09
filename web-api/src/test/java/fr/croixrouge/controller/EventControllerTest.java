package fr.croixrouge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.exposition.dto.*;
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
    @DisplayName("Test that the event details endpoint returns an event when given a correct event id")
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

        mockMvc.perform(get("/event/details")
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
    @DisplayName("Test that the event details endpoint returns a 404 when given an incorrect event id")
    public void eventIdFailureTest() throws Exception {
        EventRequest eventRequest = new EventRequest("-1");

        mockMvc.perform(get("/event/details")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventRequest)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test that the event endpoint for local unit returns a list of events when given a correct local unit id")
    public void eventsLocalUnitSuccessTest() throws Exception {
        EventForLocalUnitRequest eventForLocalUnitRequest = new EventForLocalUnitRequest("1");

        EventResponse eventResponse1 = new EventResponse(
                "Formation PSC1",
                "Formation au PSC1",
                LocalDateTime.of(2000, 6, 1, 10, 0).toString(),
                LocalDateTime.of(2000, 6, 1, 12, 0).toString(),
                "1",
                "1"
        );
        EventResponse eventResponse2 = new EventResponse(
                "Distribution alimentaire",
                "Distribution alimentaire gratuite",
                LocalDateTime.of(2000, 6, 2, 10, 0).toString(),
                LocalDateTime.of(2000, 6, 2, 12, 0).toString(),
                "1",
                "1"
        );
        EventResponse eventResponse3 = new EventResponse(
                "Formation PSC1",
                "Formation au PSC1",
                LocalDateTime.of(2000, 7, 1, 10, 0).toString(),
                LocalDateTime.of(2000, 7, 1, 12, 0).toString(),
                "1",
                "1"
        );

        mockMvc.perform(get("/event/all")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventForLocalUnitRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(eventResponse1.getName()))
                .andExpect(jsonPath("$[0].description").value(eventResponse1.getDescription()))
                .andExpect(jsonPath("$[0].start").value(eventResponse1.getStart()))
                .andExpect(jsonPath("$[0].end").value(eventResponse1.getEnd()))
                .andExpect(jsonPath("$[0].referrerId").value(eventResponse1.getReferrerId()))
                .andExpect(jsonPath("$[0].localUnitId").value(eventResponse1.getLocalUnitId()))
                .andExpect(jsonPath("$[1].name").value(eventResponse2.getName()))
                .andExpect(jsonPath("$[1].description").value(eventResponse2.getDescription()))
                .andExpect(jsonPath("$[1].start").value(eventResponse2.getStart()))
                .andExpect(jsonPath("$[1].end").value(eventResponse2.getEnd()))
                .andExpect(jsonPath("$[1].referrerId").value(eventResponse2.getReferrerId()))
                .andExpect(jsonPath("$[1].localUnitId").value(eventResponse2.getLocalUnitId()))
                .andExpect(jsonPath("$[2].name").value(eventResponse3.getName()))
                .andExpect(jsonPath("$[2].description").value(eventResponse3.getDescription()))
                .andExpect(jsonPath("$[2].start").value(eventResponse3.getStart()))
                .andExpect(jsonPath("$[2].end").value(eventResponse3.getEnd()))
                .andExpect(jsonPath("$[2].referrerId").value(eventResponse3.getReferrerId()))
                .andExpect(jsonPath("$[2].localUnitId").value(eventResponse3.getLocalUnitId()));
    }

    @Test
    @DisplayName("Test that the event endpoint for local unit and month returns a list of events when given a correct local unit id and month")
    public void eventsLocalUnitAndMonthSuccessTest() throws Exception {
        EventForLocalUnitAndMonthRequest eventForLocalUnitAndMonthRequest = new EventForLocalUnitAndMonthRequest("1", 7);

        EventResponse eventResponse = new EventResponse(
                "Formation PSC1",
                "Formation au PSC1",
                LocalDateTime.of(2000, 7, 1, 10, 0).toString(),
                LocalDateTime.of(2000, 7, 1, 12, 0).toString(),
                "1",
                "1"
        );

        mockMvc.perform(get("/event")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventForLocalUnitAndMonthRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(eventResponse.getName()))
                .andExpect(jsonPath("$[0].description").value(eventResponse.getDescription()))
                .andExpect(jsonPath("$[0].start").value(eventResponse.getStart()))
                .andExpect(jsonPath("$[0].end").value(eventResponse.getEnd()))
                .andExpect(jsonPath("$[0].referrerId").value(eventResponse.getReferrerId()))
                .andExpect(jsonPath("$[0].localUnitId").value(eventResponse.getLocalUnitId()));
    }
}
