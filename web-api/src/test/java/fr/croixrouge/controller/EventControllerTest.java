package fr.croixrouge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.exposition.dto.core.LoginRequest;
import fr.croixrouge.exposition.dto.event.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private ZonedDateTime timestampToLocalDateTime(Timestamp timestamp) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp.getTime()), ZoneId.of("Europe/Paris"));
    }

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
        SingleEventRequest singleEventRequest = new SingleEventRequest("1", "0");

        SingleEventDetailedResponse singleEventDetailedResponse = new SingleEventDetailedResponse(
            "Formation PSC1",
            "Formation au PSC1",
            ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 10, 0), ZoneId.of("Europe/Paris")).toString(),
            ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 12, 0), ZoneId.of("Europe/Paris")).toString(),
            "1",
            "1",
            List.of()
        );

        mockMvc.perform(get("/event/details")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(singleEventRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(singleEventDetailedResponse.getName()))
            .andExpect(jsonPath("$.description").value(singleEventDetailedResponse.getDescription()))
            .andExpect(jsonPath("$.start").value(singleEventDetailedResponse.getStart()))
            .andExpect(jsonPath("$.end").value(singleEventDetailedResponse.getEnd()))
            .andExpect(jsonPath("$.referrerId").value(singleEventDetailedResponse.getReferrerId()))
            .andExpect(jsonPath("$.localUnitId").value(singleEventDetailedResponse.getLocalUnitId()))
            .andExpect(jsonPath("$.participants").isArray());
    }

    @Test
    @DisplayName("Test that the event details endpoint returns a 404 when given an incorrect event id")
    public void eventIdFailureTest() throws Exception {
        SingleEventRequest singleEventRequest = new SingleEventRequest("-1", "-1");

        mockMvc.perform(get("/event/details")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(singleEventRequest)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test that the event details endpoint create an event when given the correct parameters")
    public void eventCreateSuccessTest() throws Exception {
        SingleEventCreationRequest singleEventCreationRequest = new SingleEventCreationRequest(
                "Formation Benevole",
                "Formation pour devenir benevole",
                Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.of(2001, 1, 1, 10, 0), ZoneId.of("Europe/Paris")).toLocalDateTime()),
                Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.of(2001, 1, 1, 12, 0), ZoneId.of("Europe/Paris")).toLocalDateTime()),
                "1",
                "1"
        );

        String eventId = mockMvc.perform(post("/event/details")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(singleEventCreationRequest)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        SingleEventRequest singleEventRequest = new SingleEventRequest(eventId, "0");

        mockMvc.perform(get("/event/details")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(singleEventRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(singleEventCreationRequest.getName()))
                .andExpect(jsonPath("$.description").value(singleEventCreationRequest.getDescription()))
                .andExpect(jsonPath("$.start").value(singleEventCreationRequest.getStart().toString()))
                .andExpect(jsonPath("$.end").value(singleEventCreationRequest.getEnd().toString()))
                .andExpect(jsonPath("$.referrerId").value(singleEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$.localUnitId").value(singleEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$.participants").isArray());
    }

    @Test
    @DisplayName("Test that the event details endpoint returns a 200 when deleting an event")
    public void eventDeleteSuccessTest() throws Exception {
        SingleEventRequest singleEventRequest = new SingleEventRequest("4", "0");

        mockMvc.perform(delete("/event/details")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(singleEventRequest)))
            .andExpect(status().isOk());

        mockMvc.perform(get("/event/details")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(singleEventRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test that the event endpoint for local unit returns a list of events when given a correct local unit id")
    public void eventsLocalUnitSuccessTest() throws Exception {
        EventForLocalUnitRequest eventForLocalUnitRequest = new EventForLocalUnitRequest("1");

        EventResponse eventResponse1 = new EventResponse(
                "Formation PSC1",
                "Formation au PSC1",
                ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                "1",
                "1",
                0
        );
        EventResponse eventResponse2 = new EventResponse(
                "Distribution alimentaire",
                "Distribution alimentaire gratuite",
                ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                "1",
                "1",
                0
        );
        EventResponse eventResponse3 = new EventResponse(
                "Formation PSC1",
                "Formation au PSC1",
                ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                "1",
                "1",
                0
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
                .andExpect(jsonPath("$[0].numberOfParticipants").value(eventResponse1.getNumberOfParticipants()))
                .andExpect(jsonPath("$[1].name").value(eventResponse2.getName()))
                .andExpect(jsonPath("$[1].description").value(eventResponse2.getDescription()))
                .andExpect(jsonPath("$[1].start").value(eventResponse2.getStart()))
                .andExpect(jsonPath("$[1].end").value(eventResponse2.getEnd()))
                .andExpect(jsonPath("$[1].referrerId").value(eventResponse2.getReferrerId()))
                .andExpect(jsonPath("$[1].localUnitId").value(eventResponse2.getLocalUnitId()))
                .andExpect(jsonPath("$[1].numberOfParticipants").value(eventResponse1.getNumberOfParticipants()))
                .andExpect(jsonPath("$[2].name").value(eventResponse3.getName()))
                .andExpect(jsonPath("$[2].description").value(eventResponse3.getDescription()))
                .andExpect(jsonPath("$[2].start").value(eventResponse3.getStart()))
                .andExpect(jsonPath("$[2].end").value(eventResponse3.getEnd()))
                .andExpect(jsonPath("$[2].referrerId").value(eventResponse3.getReferrerId()))
                .andExpect(jsonPath("$[2].localUnitId").value(eventResponse3.getLocalUnitId()))
                .andExpect(jsonPath("$[2].numberOfParticipants").value(eventResponse1.getNumberOfParticipants()));
    }

    @Test
    @DisplayName("Test that the event endpoint for local unit and month returns a list of events when given a correct local unit id and month")
    public void eventsLocalUnitAndMonthSuccessTest() throws Exception {
        EventForLocalUnitAndMonthRequest eventForLocalUnitAndMonthRequest = new EventForLocalUnitAndMonthRequest("1", 7, 2000);

        EventResponse eventResponse = new EventResponse(
                "Formation PSC1",
                "Formation au PSC1",
                ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                "1",
                "1",
                0
        );

        mockMvc.perform(get("/event/date")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventForLocalUnitAndMonthRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(eventResponse.getName()))
                .andExpect(jsonPath("$[0].description").value(eventResponse.getDescription()))
                .andExpect(jsonPath("$[0].start").value(eventResponse.getStart()))
                .andExpect(jsonPath("$[0].end").value(eventResponse.getEnd()))
                .andExpect(jsonPath("$[0].referrerId").value(eventResponse.getReferrerId()))
                .andExpect(jsonPath("$[0].localUnitId").value(eventResponse.getLocalUnitId()))
                .andExpect(jsonPath("$[0].numberOfParticipants").value(eventResponse.getNumberOfParticipants()));
    }

    @Test
    @DisplayName("Test that the event register endpoint adds a user to an event")
    public void eventRegisterSuccessTest() throws Exception {
        EventRegistrationRequest eventRegistrationRequest = new EventRegistrationRequest("1", "0", "1");

        mockMvc.perform(post("/event/register")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRegistrationRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/event/details")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRegistrationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participants[0]").value("1"));
    }

    @Test
    @DisplayName("Test that the event sessions endpoint returns a recurring event when given a correct event id")
    public void eventIdSessionSuccessTest() throws Exception {
        SessionForEventRequest sessionForEventRequest = new SessionForEventRequest("4");

        EventResponse eventResponse = new EventResponse(
                "EPISOL",
                "Ouverture de l'EPISOL",
                ZonedDateTime.of(LocalDateTime.of(2002, 1, 1, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                ZonedDateTime.of(LocalDateTime.of(2002, 1, 1, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                "1",
                "1",
                0
        );

        mockMvc.perform(get("/event/sessions")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionForEventRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(eventResponse.getName()))
                .andExpect(jsonPath("$[0].description").value(eventResponse.getDescription()))
                .andExpect(jsonPath("$[0].start").value(eventResponse.getStart()))
                .andExpect(jsonPath("$[0].end").value(eventResponse.getEnd()))
                .andExpect(jsonPath("$[0].referrerId").value(eventResponse.getReferrerId()))
                .andExpect(jsonPath("$[0].localUnitId").value(eventResponse.getLocalUnitId()))
                .andExpect(jsonPath("$[0].numberOfParticipants").value(eventResponse.getNumberOfParticipants()));
    }

    @Test
    @DisplayName("Test that the event sessions endpoint create a recurring event when given the correct parameters")
    public void eventCreateSessionsSuccessTest() throws Exception {
        RecurrentEventCreationRequest recurrentEventCreationRequest = new RecurrentEventCreationRequest(
                "Formation Benevole",
                "Formation pour devenir benevole",
                "1",
                "1",
                Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.of(2002, 3, 1, 10, 0), ZoneId.of("Europe/Paris")).toLocalDateTime()),
                Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.of(2002, 4, 1, 12, 0), ZoneId.of("Europe/Paris")).toLocalDateTime()),
                120,
                7
        );

        String eventId = mockMvc.perform(post("/event/sessions")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recurrentEventCreationRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        SingleEventRequest singleEventRequest = new SingleEventRequest(eventId, "0");

        mockMvc.perform(get("/event/sessions")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(singleEventRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(recurrentEventCreationRequest.getName()))
                .andExpect(jsonPath("$[0].description").value(recurrentEventCreationRequest.getDescription()))
                .andExpect(jsonPath("$[0].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).toString()))
                .andExpect(jsonPath("$[0].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusHours(2).toString()))
                .andExpect(jsonPath("$[0].referrerId").value(recurrentEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$[0].localUnitId").value(recurrentEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$[0].numberOfParticipants").value(0))
                .andExpect(jsonPath("$[1].name").value(recurrentEventCreationRequest.getName()))
                .andExpect(jsonPath("$[1].description").value(recurrentEventCreationRequest.getDescription()))
                .andExpect(jsonPath("$[1].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(7).toString()))
                .andExpect(jsonPath("$[1].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(7).plusHours(2).toString()))
                .andExpect(jsonPath("$[1].referrerId").value(recurrentEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$[1].localUnitId").value(recurrentEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$[1].numberOfParticipants").value(0))
                .andExpect(jsonPath("$[2].name").value(recurrentEventCreationRequest.getName()))
                .andExpect(jsonPath("$[2].description").value(recurrentEventCreationRequest.getDescription()))
                .andExpect(jsonPath("$[2].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(14).toString()))
                .andExpect(jsonPath("$[2].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(14).plusHours(2).toString()))
                .andExpect(jsonPath("$[2].referrerId").value(recurrentEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$[2].localUnitId").value(recurrentEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$[2].numberOfParticipants").value(0))
                .andExpect(jsonPath("$[3].name").value(recurrentEventCreationRequest.getName()))
                .andExpect(jsonPath("$[3].description").value(recurrentEventCreationRequest.getDescription()))
                .andExpect(jsonPath("$[3].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(21).toString()))
                .andExpect(jsonPath("$[3].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(21).plusHours(2).toString()))
                .andExpect(jsonPath("$[3].referrerId").value(recurrentEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$[3].localUnitId").value(recurrentEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$[3].numberOfParticipants").value(0))
                .andExpect(jsonPath("$[4].name").value(recurrentEventCreationRequest.getName()))
                .andExpect(jsonPath("$[4].description").value(recurrentEventCreationRequest.getDescription()))
                .andExpect(jsonPath("$[4].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(28).toString()))
                .andExpect(jsonPath("$[4].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(28).plusHours(2).toString()))
                .andExpect(jsonPath("$[4].referrerId").value(recurrentEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$[4].localUnitId").value(recurrentEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$[4].numberOfParticipants").value(0));
    }
}
