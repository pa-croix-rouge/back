package fr.croixrouge.exposition.controller;

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
import org.springframework.test.context.event.annotation.AfterTestMethod;
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
        String eventId = "1";
        String sessionId = "0";

        SingleEventDetailedResponse singleEventDetailedResponse = new SingleEventDetailedResponse(
            eventId,
            sessionId,
            "Formation PSC1",
            "Formation au PSC1",
            ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 10, 0), ZoneId.of("Europe/Paris")).toString(),
            ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 12, 0), ZoneId.of("Europe/Paris")).toString(),
            "1",
            "1",
            2,
            List.of()
        );

        mockMvc.perform(get("/event/details/" + eventId + "/" + sessionId)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.eventId").value(singleEventDetailedResponse.getEventId()))
            .andExpect(jsonPath("$.sessionId").value(singleEventDetailedResponse.getSessionId()))
            .andExpect(jsonPath("$.name").value(singleEventDetailedResponse.getName()))
            .andExpect(jsonPath("$.description").value(singleEventDetailedResponse.getDescription()))
            .andExpect(jsonPath("$.start").value(singleEventDetailedResponse.getStart()))
            .andExpect(jsonPath("$.end").value(singleEventDetailedResponse.getEnd()))
            .andExpect(jsonPath("$.referrerId").value(singleEventDetailedResponse.getReferrerId()))
            .andExpect(jsonPath("$.localUnitId").value(singleEventDetailedResponse.getLocalUnitId()))
            .andExpect(jsonPath("$.maxParticipants").value(singleEventDetailedResponse.getMaxParticipants()))
            .andExpect(jsonPath("$.participants").isArray());
    }

    @Test
    @DisplayName("Test that the event details endpoint updates an event when given a correct event and session id")
    public void eventUpdateSuccessTest() throws Exception {
        String eventId = "1";
        String sessionId = "0";

        SingleEventCreationRequest singleEventCreationRequest = new SingleEventCreationRequest(
                "Formation Premier Secours de niveau 1",
                "Formation au diplôme du PSC1 (Premier Secours de niveau 1)",
                Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 10, 0), ZoneId.of("Europe/Paris")).toLocalDateTime()),
                Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 12, 0), ZoneId.of("Europe/Paris")).toLocalDateTime()),
                "1",
                "1",
                1
        );

        mockMvc.perform(post("/event/details/" + eventId + "/" + sessionId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(singleEventCreationRequest)))
                .andExpect(status().isOk());

        SingleEventDetailedResponse singleEventDetailedResponse = new SingleEventDetailedResponse(
                eventId,
                sessionId,
                "Formation Premier Secours de niveau 1",
                "Formation au diplôme du PSC1 (Premier Secours de niveau 1)",
                ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                "1",
                "1",
                1,
                List.of()
        );

        mockMvc.perform(get("/event/details/" + eventId + "/" + sessionId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(singleEventDetailedResponse.getEventId()))
                .andExpect(jsonPath("$.sessionId").value(singleEventDetailedResponse.getSessionId()))
                .andExpect(jsonPath("$.name").value(singleEventDetailedResponse.getName()))
                .andExpect(jsonPath("$.description").value(singleEventDetailedResponse.getDescription()))
                .andExpect(jsonPath("$.start").value(singleEventDetailedResponse.getStart()))
                .andExpect(jsonPath("$.end").value(singleEventDetailedResponse.getEnd()))
                .andExpect(jsonPath("$.referrerId").value(singleEventDetailedResponse.getReferrerId()))
                .andExpect(jsonPath("$.localUnitId").value(singleEventDetailedResponse.getLocalUnitId()))
                .andExpect(jsonPath("$.maxParticipants").value(singleEventDetailedResponse.getMaxParticipants()))
                .andExpect(jsonPath("$.participants").isArray());
    }

    @Test
    @DisplayName("Test that the event details endpoint returns a 404 when given an incorrect event id")
    public void eventIdFailureTest() throws Exception {
        String eventId = "-1";
        String sessionId = "-1";

        mockMvc.perform(get("/event/details/" + eventId + "/" + sessionId)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON))
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
                "1",
                30
        );

        String eventId = mockMvc.perform(post("/event/details")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(singleEventCreationRequest)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/event/details/" + eventId + "/0")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(eventId))
                .andExpect(jsonPath("$.sessionId").value("0"))
                .andExpect(jsonPath("$.name").value(singleEventCreationRequest.getName()))
                .andExpect(jsonPath("$.description").value(singleEventCreationRequest.getDescription()))
                .andExpect(jsonPath("$.start").value(timestampToLocalDateTime(singleEventCreationRequest.getStart()).toString()))
                .andExpect(jsonPath("$.end").value(timestampToLocalDateTime(singleEventCreationRequest.getEnd()).toString()))
                .andExpect(jsonPath("$.referrerId").value(singleEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$.localUnitId").value(singleEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$.maxParticipants").value(singleEventCreationRequest.getMaxParticipants()))
                .andExpect(jsonPath("$.participants").isArray());
    }

    @Test
    @DisplayName("Test that the event details endpoint returns a 200 when deleting an event")
    public void eventDeleteSuccessTest() throws Exception {
        String eventId = "4";
        String sessionId = "0";

        mockMvc.perform(delete("/event/details/" + eventId + "/" + sessionId)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        mockMvc.perform(get("/event/details/" + eventId + "/" + sessionId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test that the event endpoint for local unit returns a list of events when given a correct local unit id")
    public void eventsLocalUnitSuccessTest() throws Exception {
        String localUnitId = "1";

        EventResponse eventResponse1 = new EventResponse(
                "1",
                "0",
                "Formation PSC1",
                "Formation au PSC1",
                ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                "1",
                "1",
                2,
                0,
                false
        );
        EventResponse eventResponse2 = new EventResponse(
                "2",
                "0",
                "Distribution alimentaire",
                "Distribution alimentaire gratuite",
                ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                "1",
                "1",
                30,
                0,
                false
        );
        EventResponse eventResponse3 = new EventResponse(
                "3",
                "0",
                "Formation PSC1",
                "Formation au PSC1",
                ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                "1",
                "1",
                30,
                0,
                false
        );

        mockMvc.perform(get("/event/all/" + localUnitId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventId").value(eventResponse1.getEventId()))
                .andExpect(jsonPath("$[0].sessionId").value(eventResponse1.getSessionId()))
                .andExpect(jsonPath("$[0].name").value(eventResponse1.getName()))
                .andExpect(jsonPath("$[0].description").value(eventResponse1.getDescription()))
                .andExpect(jsonPath("$[0].start").value(eventResponse1.getStart()))
                .andExpect(jsonPath("$[0].end").value(eventResponse1.getEnd()))
                .andExpect(jsonPath("$[0].referrerId").value(eventResponse1.getReferrerId()))
                .andExpect(jsonPath("$[0].localUnitId").value(eventResponse1.getLocalUnitId()))
                .andExpect(jsonPath("$[0].maxParticipants").value(eventResponse1.getMaxParticipants()))
                .andExpect(jsonPath("$[0].numberOfParticipants").value(eventResponse1.getNumberOfParticipants()))
                .andExpect(jsonPath("$[0].recurring").value(eventResponse1.isRecurring()))
                .andExpect(jsonPath("$[1].eventId").value(eventResponse2.getEventId()))
                .andExpect(jsonPath("$[1].sessionId").value(eventResponse2.getSessionId()))
                .andExpect(jsonPath("$[1].name").value(eventResponse2.getName()))
                .andExpect(jsonPath("$[1].description").value(eventResponse2.getDescription()))
                .andExpect(jsonPath("$[1].start").value(eventResponse2.getStart()))
                .andExpect(jsonPath("$[1].end").value(eventResponse2.getEnd()))
                .andExpect(jsonPath("$[1].referrerId").value(eventResponse2.getReferrerId()))
                .andExpect(jsonPath("$[1].localUnitId").value(eventResponse2.getLocalUnitId()))
                .andExpect(jsonPath("$[1].maxParticipants").value(eventResponse2.getMaxParticipants()))
                .andExpect(jsonPath("$[1].numberOfParticipants").value(eventResponse2.getNumberOfParticipants()))
                .andExpect(jsonPath("$[1].recurring").value(eventResponse2.isRecurring()))
                .andExpect(jsonPath("$[2].eventId").value(eventResponse3.getEventId()))
                .andExpect(jsonPath("$[2].sessionId").value(eventResponse3.getSessionId()))
                .andExpect(jsonPath("$[2].name").value(eventResponse3.getName()))
                .andExpect(jsonPath("$[2].description").value(eventResponse3.getDescription()))
                .andExpect(jsonPath("$[2].start").value(eventResponse3.getStart()))
                .andExpect(jsonPath("$[2].end").value(eventResponse3.getEnd()))
                .andExpect(jsonPath("$[2].referrerId").value(eventResponse3.getReferrerId()))
                .andExpect(jsonPath("$[2].localUnitId").value(eventResponse3.getLocalUnitId()))
                .andExpect(jsonPath("$[2].maxParticipants").value(eventResponse3.getMaxParticipants()))
                .andExpect(jsonPath("$[2].numberOfParticipants").value(eventResponse3.getNumberOfParticipants()))
                .andExpect(jsonPath("$[2].recurring").value(eventResponse3.isRecurring()));
    }

    @Test
    @DisplayName("Test that the event endpoint for local unit returns the stats when given a correct local unit id")
    public void eventsStatsLocalUnitSuccessTest() throws Exception {
        String localUnitId = "1";

        EventStatsResponse eventStatsResponse = new EventStatsResponse(
                0,
                0,
                0,
                0
        );

        mockMvc.perform(get("/event/stats/" + localUnitId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfEventsOverTheMonth").value(eventStatsResponse.getNumberOfEventsOverTheMonth()))
                .andExpect(jsonPath("$.totalParticipantsOverTheMonth").value(eventStatsResponse.getTotalParticipantsOverTheMonth()))
                .andExpect(jsonPath("$.numberOfEventsOverTheYear").value(eventStatsResponse.getNumberOfEventsOverTheYear()))
                .andExpect(jsonPath("$.totalParticipantsOverTheYear").value(eventStatsResponse.getTotalParticipantsOverTheYear()));
    }

    @Test
    @DisplayName("Test that the event endpoint for local unit and month returns a list of events when given a correct local unit id and month")
    public void eventsLocalUnitAndMonthSuccessTest() throws Exception {
        EventForLocalUnitAndMonthRequest eventForLocalUnitAndMonthRequest = new EventForLocalUnitAndMonthRequest("1", 7, 2000);

        EventResponse eventResponse = new EventResponse(
                "3",
                "0",
                "Formation PSC1",
                "Formation au PSC1",
                ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                "1",
                "1",
                30,
                0,
                false
        );

        mockMvc.perform(get("/event/date")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventForLocalUnitAndMonthRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventId").value(eventResponse.getEventId()))
                .andExpect(jsonPath("$[0].sessionId").value(eventResponse.getSessionId()))
                .andExpect(jsonPath("$[0].name").value(eventResponse.getName()))
                .andExpect(jsonPath("$[0].description").value(eventResponse.getDescription()))
                .andExpect(jsonPath("$[0].start").value(eventResponse.getStart()))
                .andExpect(jsonPath("$[0].end").value(eventResponse.getEnd()))
                .andExpect(jsonPath("$[0].referrerId").value(eventResponse.getReferrerId()))
                .andExpect(jsonPath("$[0].localUnitId").value(eventResponse.getLocalUnitId()))
                .andExpect(jsonPath("$[0].maxParticipants").value(eventResponse.getMaxParticipants()))
                .andExpect(jsonPath("$[0].numberOfParticipants").value(eventResponse.getNumberOfParticipants()))
                .andExpect(jsonPath("$[0].recurring").value(eventResponse.isRecurring()));
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

        mockMvc.perform(get("/event/details/" + eventRegistrationRequest.getEventId() + "/" + eventRegistrationRequest.getSessionId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participants[0]").value("1"));
    }

    @Test
    @DisplayName("Test that the event register endpoint does not adds an already registered user to an event")
    public void eventRegisterTwiceFailTest() throws Exception {
        EventRegistrationRequest eventRegistrationRequest = new EventRegistrationRequest("1", "0", "1");

        mockMvc.perform(post("/event/register")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRegistrationRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Test that the event register endpoint does not adds a user to an event if it's full")
    @AfterTestMethod("eventUpdateSuccessTest")
    public void eventRegisterWhenAnEventIsFullFailTest() throws Exception {
        EventRegistrationRequest eventRegistrationRequest = new EventRegistrationRequest("1", "0", "2");

        mockMvc.perform(post("/event/register")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRegistrationRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Test that the event register endpoint does not adds a user to a non existing event or session")
    public void eventRegisterFailOnNonExistingEventOrSessionTest() throws Exception {
        EventRegistrationRequest eventRegistrationRequest = new EventRegistrationRequest("-1", "0", "1");

        mockMvc.perform(post("/event/register")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRegistrationRequest)))
                .andExpect(status().isInternalServerError());

        eventRegistrationRequest.setEventId("1");
        eventRegistrationRequest.setSessionId("-1");

        mockMvc.perform(post("/event/register")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRegistrationRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Test that the event sessions endpoint returns a recurring event when given a correct event id")
    public void eventIdSessionSuccessTest() throws Exception {
        EventResponse eventResponse = new EventResponse(
                "4",
                "0",
                "EPISOL",
                "Ouverture de l'EPISOL",
                ZonedDateTime.of(LocalDateTime.of(2002, 1, 1, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                ZonedDateTime.of(LocalDateTime.of(2002, 1, 1, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                "1",
                "1",
                30,
                0,
                true
        );

        mockMvc.perform(get("/event/sessions/" + eventResponse.getEventId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].eventId").value(eventResponse.getEventId()))
                .andExpect(jsonPath("$[0].sessionId").value(eventResponse.getSessionId()))
                .andExpect(jsonPath("$[0].name").value(eventResponse.getName()))
                .andExpect(jsonPath("$[0].description").value(eventResponse.getDescription()))
                .andExpect(jsonPath("$[0].start").value(eventResponse.getStart()))
                .andExpect(jsonPath("$[0].end").value(eventResponse.getEnd()))
                .andExpect(jsonPath("$[0].referrerId").value(eventResponse.getReferrerId()))
                .andExpect(jsonPath("$[0].localUnitId").value(eventResponse.getLocalUnitId()))
                .andExpect(jsonPath("$[0].maxParticipants").value(eventResponse.getMaxParticipants()))
                .andExpect(jsonPath("$[0].numberOfParticipants").value(eventResponse.getNumberOfParticipants()))
                .andExpect(jsonPath("$[0].recurring").value(eventResponse.isRecurring()));
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
                7,
                30
        );

        String eventId = mockMvc.perform(post("/event/sessions")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recurrentEventCreationRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/event/sessions/" + eventId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].eventId").value(eventId))
                .andExpect(jsonPath("$[0].sessionId").value("0"))
                .andExpect(jsonPath("$[0].name").value(recurrentEventCreationRequest.getName()))
                .andExpect(jsonPath("$[0].description").value(recurrentEventCreationRequest.getDescription()))
                .andExpect(jsonPath("$[0].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).toString()))
                .andExpect(jsonPath("$[0].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusHours(2).toString()))
                .andExpect(jsonPath("$[0].referrerId").value(recurrentEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$[0].localUnitId").value(recurrentEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$[0].maxParticipants").value(recurrentEventCreationRequest.getMaxParticipants()))
                .andExpect(jsonPath("$[0].numberOfParticipants").value(0))
                .andExpect(jsonPath("$[0].recurring").value(true))
                .andExpect(jsonPath("$[1].eventId").value(eventId))
                .andExpect(jsonPath("$[1].sessionId").value("1"))
                .andExpect(jsonPath("$[1].name").value(recurrentEventCreationRequest.getName()))
                .andExpect(jsonPath("$[1].description").value(recurrentEventCreationRequest.getDescription()))
                .andExpect(jsonPath("$[1].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(7).toString()))
                .andExpect(jsonPath("$[1].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(7).plusHours(2).toString()))
                .andExpect(jsonPath("$[1].referrerId").value(recurrentEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$[1].localUnitId").value(recurrentEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$[1].maxParticipants").value(recurrentEventCreationRequest.getMaxParticipants()))
                .andExpect(jsonPath("$[1].numberOfParticipants").value(0))
                .andExpect(jsonPath("$[1].recurring").value(true))
                .andExpect(jsonPath("$[2].eventId").value(eventId))
                .andExpect(jsonPath("$[2].sessionId").value("2"))
                .andExpect(jsonPath("$[2].name").value(recurrentEventCreationRequest.getName()))
                .andExpect(jsonPath("$[2].description").value(recurrentEventCreationRequest.getDescription()))
                .andExpect(jsonPath("$[2].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(14).toString()))
                .andExpect(jsonPath("$[2].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(14).plusHours(2).toString()))
                .andExpect(jsonPath("$[2].referrerId").value(recurrentEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$[2].localUnitId").value(recurrentEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$[2].maxParticipants").value(recurrentEventCreationRequest.getMaxParticipants()))
                .andExpect(jsonPath("$[2].numberOfParticipants").value(0))
                .andExpect(jsonPath("$[2].recurring").value(true))
                .andExpect(jsonPath("$[3].eventId").value(eventId))
                .andExpect(jsonPath("$[3].sessionId").value("3"))
                .andExpect(jsonPath("$[3].name").value(recurrentEventCreationRequest.getName()))
                .andExpect(jsonPath("$[3].description").value(recurrentEventCreationRequest.getDescription()))
                .andExpect(jsonPath("$[3].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(21).toString()))
                .andExpect(jsonPath("$[3].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(21).plusHours(2).toString()))
                .andExpect(jsonPath("$[3].referrerId").value(recurrentEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$[3].localUnitId").value(recurrentEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$[3].maxParticipants").value(recurrentEventCreationRequest.getMaxParticipants()))
                .andExpect(jsonPath("$[3].numberOfParticipants").value(0))
                .andExpect(jsonPath("$[3].recurring").value(true))
                .andExpect(jsonPath("$[4].eventId").value(eventId))
                .andExpect(jsonPath("$[4].sessionId").value("4"))
                .andExpect(jsonPath("$[4].name").value(recurrentEventCreationRequest.getName()))
                .andExpect(jsonPath("$[4].description").value(recurrentEventCreationRequest.getDescription()))
                .andExpect(jsonPath("$[4].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(28).toString()))
                .andExpect(jsonPath("$[4].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(28).plusHours(2).toString()))
                .andExpect(jsonPath("$[4].referrerId").value(recurrentEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$[4].localUnitId").value(recurrentEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$[4].maxParticipants").value(recurrentEventCreationRequest.getMaxParticipants()))
                .andExpect(jsonPath("$[4].numberOfParticipants").value(0))
                .andExpect(jsonPath("$[4].recurring").value(true));
    }

    @Test
    @DisplayName("Test the the event sessions endpoint can delete all the sessions of a recurrent event")
    @AfterTestMethod("eventCreateSessionsSuccessTest")
    public void eventDeleteSessionsSuccessTest() throws Exception {
        RecurrentEventCreationRequest recurrentEventCreationRequest = new RecurrentEventCreationRequest(
                "Formation Benevole",
                "Formation pour devenir benevole",
                "1",
                "1",
                Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.of(2002, 3, 1, 10, 0), ZoneId.of("Europe/Paris")).toLocalDateTime()),
                Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.of(2002, 4, 1, 12, 0), ZoneId.of("Europe/Paris")).toLocalDateTime()),
                120,
                7,
                30
        );

        String eventId = mockMvc.perform(post("/event/sessions")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recurrentEventCreationRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(delete("/event/sessions/" + eventId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/event/sessions/" + eventId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
