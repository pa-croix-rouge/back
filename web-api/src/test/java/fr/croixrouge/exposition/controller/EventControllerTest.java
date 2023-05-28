package fr.croixrouge.exposition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.croixrouge.config.MockRepositoryConfig;
import fr.croixrouge.exposition.dto.core.LoginRequest;
import fr.croixrouge.exposition.dto.event.*;
import org.junit.jupiter.api.*;
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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
        Long eventId = 1L;
        Long sessionId = 0L;

        SingleEventDetailedResponse singleEventDetailedResponse = new SingleEventDetailedResponse(
                eventId,
                sessionId,
                "Formation PSC1",
                "Formation au PSC1",
                ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                1L,
                1L,
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
            .andExpect(jsonPath("$.timeWindows").isArray())
            .andExpect(jsonPath("$.timeWindows[0].timeWindowId").value(0))
            .andExpect(jsonPath("$.timeWindows[0].start").value(singleEventDetailedResponse.getStart()))
            .andExpect(jsonPath("$.timeWindows[0].end").value(singleEventDetailedResponse.getEnd()))
            .andExpect(jsonPath("$.timeWindows[0].maxParticipants").value(singleEventDetailedResponse.getMaxParticipants()))
            .andExpect(jsonPath("$.timeWindows[0].participants").isArray())
            .andExpect(jsonPath("$.timeWindows[0].participants").isEmpty());
    }

    @Test
    @DisplayName("Test that the event details endpoint updates an event when given a correct event and session id")
    public void eventUpdateSuccessTest() throws Exception {
        Long eventId = 1L;
        Long sessionId = 0L;

        SingleEventCreationRequest singleEventCreationRequest = new SingleEventCreationRequest(
                "Formation Premier Secours de niveau 1",
                "Formation au diplôme du PSC1 (Premier Secours de niveau 1)",
                Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 10, 0), ZoneId.of("Europe/Paris")).toLocalDateTime()),
                1L,
                1L,
                120,
                1,
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
                timestampToLocalDateTime(Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 10, 0), ZoneId.of("Europe/Paris")).toLocalDateTime())).toString(),
                timestampToLocalDateTime(Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 12, 0), ZoneId.of("Europe/Paris")).toLocalDateTime())).toString(),
                1L,
                1L,
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
                .andExpect(jsonPath("$.timeWindows").isArray())
                .andExpect(jsonPath("$.timeWindows[0].timeWindowId").value(0))
                .andExpect(jsonPath("$.timeWindows[0].start").value(singleEventDetailedResponse.getStart()))
                .andExpect(jsonPath("$.timeWindows[0].end").value(singleEventDetailedResponse.getEnd()))
                .andExpect(jsonPath("$.timeWindows[0].maxParticipants").value(singleEventDetailedResponse.getMaxParticipants()))
                .andExpect(jsonPath("$.timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$.timeWindows[0].participants[0]").value("1"));
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
                1L,
                1L,
                120,
                1,
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
                .andExpect(jsonPath("$.end").value(timestampToLocalDateTime(singleEventCreationRequest.getStart()).plusMinutes((long) singleEventCreationRequest.getEventTimeWindowDuration() * singleEventCreationRequest.getEventTimeWindowOccurrence()).toString()))
                .andExpect(jsonPath("$.referrerId").value(singleEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$.localUnitId").value(singleEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$.maxParticipants").value(singleEventCreationRequest.getEventTimeWindowMaxParticipants() * singleEventCreationRequest.getEventTimeWindowOccurrence()))
                .andExpect(jsonPath("$.timeWindows").isArray())
                .andExpect(jsonPath("$.timeWindows[0].timeWindowId").value(0))
                .andExpect(jsonPath("$.timeWindows[0].start").value(timestampToLocalDateTime(singleEventCreationRequest.getStart()).toString()))
                .andExpect(jsonPath("$.timeWindows[0].end").value(timestampToLocalDateTime(singleEventCreationRequest.getStart()).plusMinutes(singleEventCreationRequest.getEventTimeWindowDuration()).toString()))
                .andExpect(jsonPath("$.timeWindows[0].maxParticipants").value(singleEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$.timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$.timeWindows[0].participants").isEmpty());
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
                1L,
                0L,
                "Formation PSC1",
                "Formation au PSC1",
                ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                1L,
                1L,
                2,
                0,
                List.of(new TimeWindowResponse(
                        "0",
                        ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                        ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                        2,
                        List.of()
                )),
                false
        );
        EventResponse eventResponse2 = new EventResponse(
                2L,
                0L,
                "Distribution alimentaire",
                "Distribution alimentaire gratuite",
                ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                1L,
                1L,
                30,
                0,
                List.of(new TimeWindowResponse(
                        "0",
                        ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                        ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 10, 40), ZoneId.of("Europe/Paris")).toString(),
                        10,
                        List.of()
                ), new TimeWindowResponse(
                        "1",
                        ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 10, 40), ZoneId.of("Europe/Paris")).toString(),
                        ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 11, 20), ZoneId.of("Europe/Paris")).toString(),
                        10,
                        List.of()
                ), new TimeWindowResponse(
                        "2",
                        ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 11, 20), ZoneId.of("Europe/Paris")).toString(),
                        ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                        10,
                        List.of()
                )),
                false
        );
        EventResponse eventResponse3 = new EventResponse(
                3L,
                0L,
                "Formation PSC1",
                "Formation au PSC1",
                ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                1L,
                1L,
                30,
                0,
                List.of(new TimeWindowResponse(
                        "0",
                        ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                        ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                        30,
                        List.of()
                )),
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
                .andExpect(jsonPath("$[0].timeWindows").isArray())
                .andExpect(jsonPath("$[0].timeWindows[0].timeWindowId").value(eventResponse1.getTimeWindows().get(0).getTimeWindowId()))
                .andExpect(jsonPath("$[0].timeWindows[0].start").value(eventResponse1.getTimeWindows().get(0).getStart()))
                .andExpect(jsonPath("$[0].timeWindows[0].end").value(eventResponse1.getTimeWindows().get(0).getEnd()))
                .andExpect(jsonPath("$[0].timeWindows[0].maxParticipants").value(eventResponse1.getTimeWindows().get(0).getMaxParticipants()))
                .andExpect(jsonPath("$[0].timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$[0].timeWindows[0].participants").isEmpty())
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
                .andExpect(jsonPath("$[1].timeWindows").isArray())
                .andExpect(jsonPath("$[1].timeWindows[0].timeWindowId").value(eventResponse2.getTimeWindows().get(0).getTimeWindowId()))
                .andExpect(jsonPath("$[1].timeWindows[0].start").value(eventResponse2.getTimeWindows().get(0).getStart()))
                .andExpect(jsonPath("$[1].timeWindows[0].end").value(eventResponse2.getTimeWindows().get(0).getEnd()))
                .andExpect(jsonPath("$[1].timeWindows[0].maxParticipants").value(eventResponse2.getTimeWindows().get(0).getMaxParticipants()))
                .andExpect(jsonPath("$[1].timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$[1].timeWindows[0].participants").isEmpty())
                .andExpect(jsonPath("$[1].timeWindows[1].timeWindowId").value(eventResponse2.getTimeWindows().get(1).getTimeWindowId()))
                .andExpect(jsonPath("$[1].timeWindows[1].start").value(eventResponse2.getTimeWindows().get(1).getStart()))
                .andExpect(jsonPath("$[1].timeWindows[1].end").value(eventResponse2.getTimeWindows().get(1).getEnd()))
                .andExpect(jsonPath("$[1].timeWindows[1].maxParticipants").value(eventResponse2.getTimeWindows().get(1).getMaxParticipants()))
                .andExpect(jsonPath("$[1].timeWindows[1].participants").isArray())
                .andExpect(jsonPath("$[1].timeWindows[1].participants").isEmpty())
                .andExpect(jsonPath("$[1].timeWindows[2].timeWindowId").value(eventResponse2.getTimeWindows().get(2).getTimeWindowId()))
                .andExpect(jsonPath("$[1].timeWindows[2].start").value(eventResponse2.getTimeWindows().get(2).getStart()))
                .andExpect(jsonPath("$[1].timeWindows[2].end").value(eventResponse2.getTimeWindows().get(2).getEnd()))
                .andExpect(jsonPath("$[1].timeWindows[2].maxParticipants").value(eventResponse2.getTimeWindows().get(2).getMaxParticipants()))
                .andExpect(jsonPath("$[1].timeWindows[2].participants").isArray())
                .andExpect(jsonPath("$[1].timeWindows[2].participants").isEmpty())
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
                .andExpect(jsonPath("$[2].timeWindows").isArray())
                .andExpect(jsonPath("$[2].timeWindows[0].timeWindowId").value(eventResponse3.getTimeWindows().get(0).getTimeWindowId()))
                .andExpect(jsonPath("$[2].timeWindows[0].start").value(eventResponse3.getTimeWindows().get(0).getStart()))
                .andExpect(jsonPath("$[2].timeWindows[0].end").value(eventResponse3.getTimeWindows().get(0).getEnd()))
                .andExpect(jsonPath("$[2].timeWindows[0].maxParticipants").value(eventResponse3.getTimeWindows().get(0).getMaxParticipants()))
                .andExpect(jsonPath("$[2].timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$[2].timeWindows[0].participants").isEmpty())
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
        final String localUnitId = "1";
        final int month = 7;
        final int year = 2000;

        EventResponse eventResponse = new EventResponse(
                3L,
                0L,
                "Formation PSC1",
                "Formation au PSC1",
                ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                1L,
                1L,
                30,
                0,
                List.of(new TimeWindowResponse(
                        "0",
                        ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                        ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                        30,
                        List.of()
                )),
                false
        );

        mockMvc.perform(get("/event/date?localUnitId=" + localUnitId + "&month=" + month + "&year=" + year)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
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
                .andExpect(jsonPath("$[0].timeWindows").isArray())
                .andExpect(jsonPath("$[0].timeWindows[0].timeWindowId").value(eventResponse.getTimeWindows().get(0).getTimeWindowId()))
                .andExpect(jsonPath("$[0].timeWindows[0].start").value(eventResponse.getTimeWindows().get(0).getStart()))
                .andExpect(jsonPath("$[0].timeWindows[0].end").value(eventResponse.getTimeWindows().get(0).getEnd()))
                .andExpect(jsonPath("$[0].timeWindows[0].maxParticipants").value(eventResponse.getTimeWindows().get(0).getMaxParticipants()))
                .andExpect(jsonPath("$[0].timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$[0].timeWindows[0].participants").isEmpty())
                .andExpect(jsonPath("$[0].recurring").value(eventResponse.isRecurring()));
    }

    @Test
    @DisplayName("Test that the event endpoint for local unit and month returns a list of events when given a correct local unit id for trimester")
    public void eventsLocalUnitForTrimesterSuccessTest() throws Exception {
        final String localUnitId = "1";
        final int month = 7;
        final int year = 2000;

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
                List.of(new TimeWindowResponse(
                        "0",
                        ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                        ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                        30,
                        List.of()
                )),
                false
        );

        mockMvc.perform(get("/event/date?localUnitId=" + localUnitId + "&month=" + month + "&year=" + year)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
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
                .andExpect(jsonPath("$[0].timeWindows").isArray())
                .andExpect(jsonPath("$[0].timeWindows[0].timeWindowId").value(eventResponse.getTimeWindows().get(0).getTimeWindowId()))
                .andExpect(jsonPath("$[0].timeWindows[0].start").value(eventResponse.getTimeWindows().get(0).getStart()))
                .andExpect(jsonPath("$[0].timeWindows[0].end").value(eventResponse.getTimeWindows().get(0).getEnd()))
                .andExpect(jsonPath("$[0].timeWindows[0].maxParticipants").value(eventResponse.getTimeWindows().get(0).getMaxParticipants()))
                .andExpect(jsonPath("$[0].timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$[0].timeWindows[0].participants").isEmpty())
                .andExpect(jsonPath("$[0].recurring").value(eventResponse.isRecurring()));
    }

    @Test
    @DisplayName("Test that the event register endpoint adds a user to an event")
    public void eventRegisterSuccessTest() throws Exception {
        Long timeWindowId = 1L;
        EventRegistrationRequest eventRegistrationRequest = new EventRegistrationRequest(1L, 0L, timeWindowId, 1L);


        mockMvc.perform(post("/event/register")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRegistrationRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/event/details/" + eventRegistrationRequest.getEventId() + "/" + eventRegistrationRequest.getSessionId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timeWindows[" + timeWindowId + "].participants[0]").value("1"));
    }

    @Test
    @DisplayName("Test that the event register endpoint does not adds an already registered user to an event")
    public void eventRegisterTwiceFailTest() throws Exception {
        EventRegistrationRequest eventRegistrationRequest = new EventRegistrationRequest(1L, 0L, 0L, 1L);

        mockMvc.perform(post("/event/register")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRegistrationRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Test that the event register endpoint does not adds a user to an event if it's full")
    public void eventRegisterWhenAnEventIsFullFailTest() throws Exception {
        EventRegistrationRequest eventRegistrationRequest = new EventRegistrationRequest(1L, 0L, 0L, 2L);

        mockMvc.perform(post("/event/register")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRegistrationRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Test that the event register endpoint does not adds a user to a non existing event or session")
    public void eventRegisterFailOnNonExistingEventOrSessionTest() throws Exception {
        EventRegistrationRequest eventRegistrationRequest = new EventRegistrationRequest(-1L, 0L, 0L, 1L);

        mockMvc.perform(post("/event/register")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRegistrationRequest)))
                .andExpect(status().isInternalServerError());

        eventRegistrationRequest.setEventId(1L);
        eventRegistrationRequest.setSessionId(-1L);

        mockMvc.perform(post("/event/register")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRegistrationRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Order(1)
    @DisplayName("Test that the event sessions endpoint returns a recurring event when given a correct event id")
    public void eventIdSessionSuccessTest() throws Exception {
        final ZonedDateTime eventStart = ZonedDateTime.of(LocalDateTime.of(2002, 1, 1, 10, 0), ZoneId.of("Europe/Paris"));
        final ZonedDateTime eventEnd = ZonedDateTime.of(LocalDateTime.of(2002, 1, 1, 12, 0), ZoneId.of("Europe/Paris"));

        EventResponse eventResponse = new EventResponse(
                4L,
                0L,
                "EPISOL",
                "Ouverture de l'EPISOL",
                eventStart.toString(),
                eventEnd.toString(),
                1L,
                1L,
                32,
                0,
                List.of(new TimeWindowResponse(
                        "0",
                        ZonedDateTime.of(LocalDateTime.of(2002, 1, 1, 10, 0), ZoneId.of("Europe/Paris")).toString(),
                        ZonedDateTime.of(LocalDateTime.of(2002, 1, 1, 10, 30), ZoneId.of("Europe/Paris")).toString(),
                        8,
                        List.of()
                ), new TimeWindowResponse(
                        "1",
                        ZonedDateTime.of(LocalDateTime.of(2002, 1, 1, 10, 30), ZoneId.of("Europe/Paris")).toString(),
                        ZonedDateTime.of(LocalDateTime.of(2002, 1, 1, 11, 0), ZoneId.of("Europe/Paris")).toString(),
                        8,
                        List.of()
                ), new TimeWindowResponse(
                        "2",
                        ZonedDateTime.of(LocalDateTime.of(2002, 1, 1, 11, 0), ZoneId.of("Europe/Paris")).toString(),
                        ZonedDateTime.of(LocalDateTime.of(2002, 1, 1, 11, 30), ZoneId.of("Europe/Paris")).toString(),
                        8,
                        List.of()
                ), new TimeWindowResponse(
                        "3",
                        ZonedDateTime.of(LocalDateTime.of(2002, 1, 1, 11, 30), ZoneId.of("Europe/Paris")).toString(),
                        ZonedDateTime.of(LocalDateTime.of(2002, 1, 1, 12, 0), ZoneId.of("Europe/Paris")).toString(),
                        8,
                        List.of()
                )),
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
                .andExpect(jsonPath("$[0].timeWindows").isArray())
                .andExpect(jsonPath("$[0].timeWindows[0].timeWindowId").value(eventResponse.getTimeWindows().get(0).getTimeWindowId()))
                .andExpect(jsonPath("$[0].timeWindows[0].start").value(eventResponse.getTimeWindows().get(0).getStart()))
                .andExpect(jsonPath("$[0].timeWindows[0].end").value(eventResponse.getTimeWindows().get(0).getEnd()))
                .andExpect(jsonPath("$[0].timeWindows[0].maxParticipants").value(eventResponse.getTimeWindows().get(0).getMaxParticipants()))
                .andExpect(jsonPath("$[0].timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$[0].timeWindows[0].participants").isEmpty())
                .andExpect(jsonPath("$[0].timeWindows[1].timeWindowId").value(eventResponse.getTimeWindows().get(1).getTimeWindowId()))
                .andExpect(jsonPath("$[0].timeWindows[1].start").value(eventResponse.getTimeWindows().get(1).getStart()))
                .andExpect(jsonPath("$[0].timeWindows[1].end").value(eventResponse.getTimeWindows().get(1).getEnd()))
                .andExpect(jsonPath("$[0].timeWindows[1].maxParticipants").value(eventResponse.getTimeWindows().get(1).getMaxParticipants()))
                .andExpect(jsonPath("$[0].timeWindows[1].participants").isArray())
                .andExpect(jsonPath("$[0].timeWindows[1].participants").isEmpty())
                .andExpect(jsonPath("$[0].timeWindows[2].timeWindowId").value(eventResponse.getTimeWindows().get(2).getTimeWindowId()))
                .andExpect(jsonPath("$[0].timeWindows[2].start").value(eventResponse.getTimeWindows().get(2).getStart()))
                .andExpect(jsonPath("$[0].timeWindows[2].end").value(eventResponse.getTimeWindows().get(2).getEnd()))
                .andExpect(jsonPath("$[0].timeWindows[2].maxParticipants").value(eventResponse.getTimeWindows().get(2).getMaxParticipants()))
                .andExpect(jsonPath("$[0].timeWindows[2].participants").isArray())
                .andExpect(jsonPath("$[0].timeWindows[2].participants").isEmpty())
                .andExpect(jsonPath("$[0].timeWindows[3].timeWindowId").value(eventResponse.getTimeWindows().get(3).getTimeWindowId()))
                .andExpect(jsonPath("$[0].timeWindows[3].start").value(eventResponse.getTimeWindows().get(3).getStart()))
                .andExpect(jsonPath("$[0].timeWindows[3].end").value(eventResponse.getTimeWindows().get(3).getEnd()))
                .andExpect(jsonPath("$[0].timeWindows[3].maxParticipants").value(eventResponse.getTimeWindows().get(3).getMaxParticipants()))
                .andExpect(jsonPath("$[0].timeWindows[3].participants").isArray())
                .andExpect(jsonPath("$[0].timeWindows[3].participants").isEmpty())
                .andExpect(jsonPath("$[0].recurring").value(eventResponse.isRecurring()))
                .andExpect(jsonPath("$[1].eventId").value(eventResponse.getEventId()))
                .andExpect(jsonPath("$[1].sessionId").value("1"))
                .andExpect(jsonPath("$[1].name").value(eventResponse.getName()))
                .andExpect(jsonPath("$[1].description").value(eventResponse.getDescription()))
                .andExpect(jsonPath("$[1].start").value(eventStart.plusDays(7).toString()))
                .andExpect(jsonPath("$[1].end").value(eventEnd.plusDays(7).toString()))
                .andExpect(jsonPath("$[1].referrerId").value(eventResponse.getReferrerId()))
                .andExpect(jsonPath("$[1].localUnitId").value(eventResponse.getLocalUnitId()))
                .andExpect(jsonPath("$[1].maxParticipants").value(eventResponse.getMaxParticipants()))
                .andExpect(jsonPath("$[1].numberOfParticipants").value(eventResponse.getNumberOfParticipants()))
                .andExpect(jsonPath("$[1].timeWindows").isArray())
                .andExpect(jsonPath("$[1].timeWindows[0].timeWindowId").value(eventResponse.getTimeWindows().get(0).getTimeWindowId()))
                .andExpect(jsonPath("$[1].timeWindows[0].start").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(0).getStart()).plusDays(7).toString()))
                .andExpect(jsonPath("$[1].timeWindows[0].end").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(0).getEnd()).plusDays(7).toString()))
                .andExpect(jsonPath("$[1].timeWindows[0].maxParticipants").value(eventResponse.getTimeWindows().get(0).getMaxParticipants()))
                .andExpect(jsonPath("$[1].timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$[1].timeWindows[0].participants").isEmpty())
                .andExpect(jsonPath("$[1].timeWindows[1].timeWindowId").value(eventResponse.getTimeWindows().get(1).getTimeWindowId()))
                .andExpect(jsonPath("$[1].timeWindows[1].start").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(1).getStart()).plusDays(7).toString()))
                .andExpect(jsonPath("$[1].timeWindows[1].end").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(1).getEnd()).plusDays(7).toString()))
                .andExpect(jsonPath("$[1].timeWindows[1].maxParticipants").value(eventResponse.getTimeWindows().get(1).getMaxParticipants()))
                .andExpect(jsonPath("$[1].timeWindows[1].participants").isArray())
                .andExpect(jsonPath("$[1].timeWindows[1].participants").isEmpty())
                .andExpect(jsonPath("$[1].timeWindows[2].timeWindowId").value(eventResponse.getTimeWindows().get(2).getTimeWindowId()))
                .andExpect(jsonPath("$[1].timeWindows[2].start").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(2).getStart()).plusDays(7).toString()))
                .andExpect(jsonPath("$[1].timeWindows[2].end").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(2).getEnd()).plusDays(7).toString()))
                .andExpect(jsonPath("$[1].timeWindows[2].maxParticipants").value(eventResponse.getTimeWindows().get(2).getMaxParticipants()))
                .andExpect(jsonPath("$[1].timeWindows[2].participants").isArray())
                .andExpect(jsonPath("$[1].timeWindows[2].participants").isEmpty())
                .andExpect(jsonPath("$[1].timeWindows[3].timeWindowId").value(eventResponse.getTimeWindows().get(3).getTimeWindowId()))
                .andExpect(jsonPath("$[1].timeWindows[3].start").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(3).getStart()).plusDays(7).toString()))
                .andExpect(jsonPath("$[1].timeWindows[3].end").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(3).getEnd()).plusDays(7).toString()))
                .andExpect(jsonPath("$[1].timeWindows[3].maxParticipants").value(eventResponse.getTimeWindows().get(3).getMaxParticipants()))
                .andExpect(jsonPath("$[1].timeWindows[3].participants").isArray())
                .andExpect(jsonPath("$[1].timeWindows[3].participants").isEmpty())
                .andExpect(jsonPath("$[1].recurring").value(eventResponse.isRecurring()))
                .andExpect(jsonPath("$[2].eventId").value(eventResponse.getEventId()))
                .andExpect(jsonPath("$[2].sessionId").value("2"))
                .andExpect(jsonPath("$[2].name").value(eventResponse.getName()))
                .andExpect(jsonPath("$[2].description").value(eventResponse.getDescription()))
                .andExpect(jsonPath("$[2].start").value(eventStart.plusDays(14).toString()))
                .andExpect(jsonPath("$[2].end").value(eventEnd.plusDays(14).toString()))
                .andExpect(jsonPath("$[2].referrerId").value(eventResponse.getReferrerId()))
                .andExpect(jsonPath("$[2].localUnitId").value(eventResponse.getLocalUnitId()))
                .andExpect(jsonPath("$[2].maxParticipants").value(eventResponse.getMaxParticipants()))
                .andExpect(jsonPath("$[2].numberOfParticipants").value(eventResponse.getNumberOfParticipants()))
                .andExpect(jsonPath("$[2].timeWindows").isArray())
                .andExpect(jsonPath("$[2].timeWindows[0].timeWindowId").value(eventResponse.getTimeWindows().get(0).getTimeWindowId()))
                .andExpect(jsonPath("$[2].timeWindows[0].start").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(0).getStart()).plusDays(14).toString()))
                .andExpect(jsonPath("$[2].timeWindows[0].end").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(0).getEnd()).plusDays(14).toString()))
                .andExpect(jsonPath("$[2].timeWindows[0].maxParticipants").value(eventResponse.getTimeWindows().get(0).getMaxParticipants()))
                .andExpect(jsonPath("$[2].timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$[2].timeWindows[0].participants").isEmpty())
                .andExpect(jsonPath("$[2].timeWindows[1].timeWindowId").value(eventResponse.getTimeWindows().get(1).getTimeWindowId()))
                .andExpect(jsonPath("$[2].timeWindows[1].start").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(1).getStart()).plusDays(14).toString()))
                .andExpect(jsonPath("$[2].timeWindows[1].end").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(1).getEnd()).plusDays(14).toString()))
                .andExpect(jsonPath("$[2].timeWindows[1].maxParticipants").value(eventResponse.getTimeWindows().get(1).getMaxParticipants()))
                .andExpect(jsonPath("$[2].timeWindows[1].participants").isArray())
                .andExpect(jsonPath("$[2].timeWindows[1].participants").isEmpty())
                .andExpect(jsonPath("$[2].timeWindows[2].timeWindowId").value(eventResponse.getTimeWindows().get(2).getTimeWindowId()))
                .andExpect(jsonPath("$[2].timeWindows[2].start").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(2).getStart()).plusDays(14).toString()))
                .andExpect(jsonPath("$[2].timeWindows[2].end").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(2).getEnd()).plusDays(14).toString()))
                .andExpect(jsonPath("$[2].timeWindows[2].maxParticipants").value(eventResponse.getTimeWindows().get(2).getMaxParticipants()))
                .andExpect(jsonPath("$[2].timeWindows[2].participants").isArray())
                .andExpect(jsonPath("$[2].timeWindows[2].participants").isEmpty())
                .andExpect(jsonPath("$[2].timeWindows[3].timeWindowId").value(eventResponse.getTimeWindows().get(3).getTimeWindowId()))
                .andExpect(jsonPath("$[2].timeWindows[3].start").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(3).getStart()).plusDays(14).toString()))
                .andExpect(jsonPath("$[2].timeWindows[3].end").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(3).getEnd()).plusDays(14).toString()))
                .andExpect(jsonPath("$[2].timeWindows[3].maxParticipants").value(eventResponse.getTimeWindows().get(3).getMaxParticipants()))
                .andExpect(jsonPath("$[2].timeWindows[3].participants").isArray())
                .andExpect(jsonPath("$[2].timeWindows[3].participants").isEmpty())
                .andExpect(jsonPath("$[2].recurring").value(eventResponse.isRecurring()))
                .andExpect(jsonPath("$[3].eventId").value(eventResponse.getEventId()))
                .andExpect(jsonPath("$[3].sessionId").value("3"))
                .andExpect(jsonPath("$[3].name").value(eventResponse.getName()))
                .andExpect(jsonPath("$[3].description").value(eventResponse.getDescription()))
                .andExpect(jsonPath("$[3].start").value(eventStart.plusDays(21).toString()))
                .andExpect(jsonPath("$[3].end").value(eventEnd.plusDays(21).toString()))
                .andExpect(jsonPath("$[3].referrerId").value(eventResponse.getReferrerId()))
                .andExpect(jsonPath("$[3].localUnitId").value(eventResponse.getLocalUnitId()))
                .andExpect(jsonPath("$[3].maxParticipants").value(eventResponse.getMaxParticipants()))
                .andExpect(jsonPath("$[3].numberOfParticipants").value(eventResponse.getNumberOfParticipants()))
                .andExpect(jsonPath("$[3].timeWindows").isArray())
                .andExpect(jsonPath("$[3].timeWindows[0].timeWindowId").value(eventResponse.getTimeWindows().get(0).getTimeWindowId()))
                .andExpect(jsonPath("$[3].timeWindows[0].start").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(0).getStart()).plusDays(21).toString()))
                .andExpect(jsonPath("$[3].timeWindows[0].end").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(0).getEnd()).plusDays(21).toString()))
                .andExpect(jsonPath("$[3].timeWindows[0].maxParticipants").value(eventResponse.getTimeWindows().get(0).getMaxParticipants()))
                .andExpect(jsonPath("$[3].timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$[3].timeWindows[0].participants").isEmpty())
                .andExpect(jsonPath("$[3].timeWindows[1].timeWindowId").value(eventResponse.getTimeWindows().get(1).getTimeWindowId()))
                .andExpect(jsonPath("$[3].timeWindows[1].start").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(1).getStart()).plusDays(21).toString()))
                .andExpect(jsonPath("$[3].timeWindows[1].end").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(1).getEnd()).plusDays(21).toString()))
                .andExpect(jsonPath("$[3].timeWindows[1].maxParticipants").value(eventResponse.getTimeWindows().get(1).getMaxParticipants()))
                .andExpect(jsonPath("$[3].timeWindows[1].participants").isArray())
                .andExpect(jsonPath("$[3].timeWindows[1].participants").isEmpty())
                .andExpect(jsonPath("$[3].timeWindows[2].timeWindowId").value(eventResponse.getTimeWindows().get(2).getTimeWindowId()))
                .andExpect(jsonPath("$[3].timeWindows[2].start").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(2).getStart()).plusDays(21).toString()))
                .andExpect(jsonPath("$[3].timeWindows[2].end").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(2).getEnd()).plusDays(21).toString()))
                .andExpect(jsonPath("$[3].timeWindows[2].maxParticipants").value(eventResponse.getTimeWindows().get(2).getMaxParticipants()))
                .andExpect(jsonPath("$[3].timeWindows[2].participants").isArray())
                .andExpect(jsonPath("$[3].timeWindows[2].participants").isEmpty())
                .andExpect(jsonPath("$[3].timeWindows[3].timeWindowId").value(eventResponse.getTimeWindows().get(3).getTimeWindowId()))
                .andExpect(jsonPath("$[3].timeWindows[3].start").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(3).getStart()).plusDays(21).toString()))
                .andExpect(jsonPath("$[3].timeWindows[3].end").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(3).getEnd()).plusDays(21).toString()))
                .andExpect(jsonPath("$[3].timeWindows[3].maxParticipants").value(eventResponse.getTimeWindows().get(3).getMaxParticipants()))
                .andExpect(jsonPath("$[3].timeWindows[3].participants").isArray())
                .andExpect(jsonPath("$[3].timeWindows[3].participants").isEmpty())
                .andExpect(jsonPath("$[3].recurring").value(eventResponse.isRecurring()))
                .andExpect(jsonPath("$[4].eventId").value(eventResponse.getEventId()))
                .andExpect(jsonPath("$[4].sessionId").value("4"))
                .andExpect(jsonPath("$[4].name").value(eventResponse.getName()))
                .andExpect(jsonPath("$[4].description").value(eventResponse.getDescription()))
                .andExpect(jsonPath("$[4].start").value(eventStart.plusDays(28).toString()))
                .andExpect(jsonPath("$[4].end").value(eventEnd.plusDays(28).toString()))
                .andExpect(jsonPath("$[4].referrerId").value(eventResponse.getReferrerId()))
                .andExpect(jsonPath("$[4].localUnitId").value(eventResponse.getLocalUnitId()))
                .andExpect(jsonPath("$[4].maxParticipants").value(eventResponse.getMaxParticipants()))
                .andExpect(jsonPath("$[4].numberOfParticipants").value(eventResponse.getNumberOfParticipants()))
                .andExpect(jsonPath("$[4].timeWindows").isArray())
                .andExpect(jsonPath("$[4].timeWindows[0].timeWindowId").value(eventResponse.getTimeWindows().get(0).getTimeWindowId()))
                .andExpect(jsonPath("$[4].timeWindows[0].start").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(0).getStart()).plusDays(28).toString()))
                .andExpect(jsonPath("$[4].timeWindows[0].end").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(0).getEnd()).plusDays(28).toString()))
                .andExpect(jsonPath("$[4].timeWindows[0].maxParticipants").value(eventResponse.getTimeWindows().get(0).getMaxParticipants()))
                .andExpect(jsonPath("$[4].timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$[4].timeWindows[0].participants").isEmpty())
                .andExpect(jsonPath("$[4].timeWindows[1].timeWindowId").value(eventResponse.getTimeWindows().get(1).getTimeWindowId()))
                .andExpect(jsonPath("$[4].timeWindows[1].start").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(1).getStart()).plusDays(28).toString()))
                .andExpect(jsonPath("$[4].timeWindows[1].end").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(1).getEnd()).plusDays(28).toString()))
                .andExpect(jsonPath("$[4].timeWindows[1].maxParticipants").value(eventResponse.getTimeWindows().get(1).getMaxParticipants()))
                .andExpect(jsonPath("$[4].timeWindows[1].participants").isArray())
                .andExpect(jsonPath("$[4].timeWindows[1].participants").isEmpty())
                .andExpect(jsonPath("$[4].timeWindows[2].timeWindowId").value(eventResponse.getTimeWindows().get(2).getTimeWindowId()))
                .andExpect(jsonPath("$[4].timeWindows[2].start").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(2).getStart()).plusDays(28).toString()))
                .andExpect(jsonPath("$[4].timeWindows[2].end").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(2).getEnd()).plusDays(28).toString()))
                .andExpect(jsonPath("$[4].timeWindows[2].maxParticipants").value(eventResponse.getTimeWindows().get(2).getMaxParticipants()))
                .andExpect(jsonPath("$[4].timeWindows[2].participants").isArray())
                .andExpect(jsonPath("$[4].timeWindows[2].participants").isEmpty())
                .andExpect(jsonPath("$[4].timeWindows[3].timeWindowId").value(eventResponse.getTimeWindows().get(3).getTimeWindowId()))
                .andExpect(jsonPath("$[4].timeWindows[3].start").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(3).getStart()).plusDays(28).toString()))
                .andExpect(jsonPath("$[4].timeWindows[3].end").value(ZonedDateTime.parse(eventResponse.getTimeWindows().get(3).getEnd()).plusDays(28).toString()))
                .andExpect(jsonPath("$[4].timeWindows[3].maxParticipants").value(eventResponse.getTimeWindows().get(3).getMaxParticipants()))
                .andExpect(jsonPath("$[4].timeWindows[3].participants").isArray())
                .andExpect(jsonPath("$[4].timeWindows[3].participants").isEmpty())
                .andExpect(jsonPath("$[4].recurring").value(eventResponse.isRecurring()));
    }

    @Test
    @Order(2)
    @DisplayName("Test that the event sessions endpoint updates a recurring event when given correct event id and parameters")
    public void eventUpdatesSessionSuccessTest() throws Exception {
        final String eventId = "4";
        final String sessionId = "0";
        final ZonedDateTime eventStart = ZonedDateTime.of(LocalDateTime.of(2002, 1, 1, 10, 0), ZoneId.of("Europe/Paris"));
        final ZonedDateTime eventEnd = ZonedDateTime.of(LocalDateTime.of(2002, 1, 1, 12, 0), ZoneId.of("Europe/Paris"));

        SingleEventCreationRequest singleEventCreationRequest = new SingleEventCreationRequest(
                "EPIcerie SOciaLe",
                "Ouverture de l'epicerie sociale pour les personnes dans le besoin",
                Timestamp.valueOf(eventStart.toLocalDateTime()),
                1L,
                1L,
                120,
                1,
                20
        );

        mockMvc.perform(post("/event/sessions/" + eventId + "/" + sessionId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(singleEventCreationRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/event/sessions/" + eventId )
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].eventId").value(eventId))
                .andExpect(jsonPath("$[0].sessionId").value(sessionId))
                .andExpect(jsonPath("$[0].name").value(singleEventCreationRequest.getName()))
                .andExpect(jsonPath("$[0].description").value(singleEventCreationRequest.getDescription()))
                .andExpect(jsonPath("$[0].start").value(eventStart.toString()))
                .andExpect(jsonPath("$[0].end").value(eventEnd.toString()))
                .andExpect(jsonPath("$[0].referrerId").value(singleEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$[0].localUnitId").value(singleEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$[0].maxParticipants").value(singleEventCreationRequest.getEventTimeWindowMaxParticipants() * singleEventCreationRequest.getEventTimeWindowOccurrence()))
                .andExpect(jsonPath("$[0].timeWindows").isArray())
                .andExpect(jsonPath("$[0].timeWindows[0].timeWindowId").value("0"))
                .andExpect(jsonPath("$[0].timeWindows[0].start").value(eventStart.toString()))
                .andExpect(jsonPath("$[0].timeWindows[0].end").value(eventEnd.toString()))
                .andExpect(jsonPath("$[0].timeWindows[0].maxParticipants").value(singleEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[0].timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$[0].timeWindows[0].participants").isEmpty())
                .andExpect(jsonPath("$[0].recurring").value(true))
                .andExpect(jsonPath("$[1].eventId").value(eventId))
                .andExpect(jsonPath("$[1].sessionId").value("1"))
                .andExpect(jsonPath("$[1].name").value(singleEventCreationRequest.getName()))
                .andExpect(jsonPath("$[1].description").value(singleEventCreationRequest.getDescription()))
                .andExpect(jsonPath("$[1].start").value(eventStart.plusDays(7).toString()))
                .andExpect(jsonPath("$[1].end").value(eventEnd.plusDays(7).toString()))
                .andExpect(jsonPath("$[1].referrerId").value(singleEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$[1].localUnitId").value(singleEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$[1].maxParticipants").value(singleEventCreationRequest.getEventTimeWindowMaxParticipants() * singleEventCreationRequest.getEventTimeWindowOccurrence()))
                .andExpect(jsonPath("$[1].timeWindows").isArray())
                .andExpect(jsonPath("$[1].timeWindows[0].timeWindowId").value("0"))
                .andExpect(jsonPath("$[1].timeWindows[0].start").value(eventStart.plusDays(7).toString()))
                .andExpect(jsonPath("$[1].timeWindows[0].end").value(eventEnd.plusDays(7).toString()))
                .andExpect(jsonPath("$[1].timeWindows[0].maxParticipants").value(singleEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[1].timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$[1].timeWindows[0].participants").isEmpty())
                .andExpect(jsonPath("$[1].recurring").value(true))
                .andExpect(jsonPath("$[2].eventId").value(eventId))
                .andExpect(jsonPath("$[2].sessionId").value("2"))
                .andExpect(jsonPath("$[2].name").value(singleEventCreationRequest.getName()))
                .andExpect(jsonPath("$[2].description").value(singleEventCreationRequest.getDescription()))
                .andExpect(jsonPath("$[2].start").value(eventStart.plusDays(14).toString()))
                .andExpect(jsonPath("$[2].end").value(eventEnd.plusDays(14).toString()))
                .andExpect(jsonPath("$[2].referrerId").value(singleEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$[2].localUnitId").value(singleEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$[2].maxParticipants").value(singleEventCreationRequest.getEventTimeWindowMaxParticipants() * singleEventCreationRequest.getEventTimeWindowOccurrence()))
                .andExpect(jsonPath("$[2].timeWindows").isArray())
                .andExpect(jsonPath("$[2].timeWindows[0].timeWindowId").value("0"))
                .andExpect(jsonPath("$[2].timeWindows[0].start").value(eventStart.plusDays(14).toString()))
                .andExpect(jsonPath("$[2].timeWindows[0].end").value(eventEnd.plusDays(14).toString()))
                .andExpect(jsonPath("$[2].timeWindows[0].maxParticipants").value(singleEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[2].timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$[2].timeWindows[0].participants").isEmpty())
                .andExpect(jsonPath("$[2].recurring").value(true))
                .andExpect(jsonPath("$[3].eventId").value(eventId))
                .andExpect(jsonPath("$[3].sessionId").value("3"))
                .andExpect(jsonPath("$[3].name").value(singleEventCreationRequest.getName()))
                .andExpect(jsonPath("$[3].description").value(singleEventCreationRequest.getDescription()))
                .andExpect(jsonPath("$[3].start").value(eventStart.plusDays(21).toString()))
                .andExpect(jsonPath("$[3].end").value(eventEnd.plusDays(21).toString()))
                .andExpect(jsonPath("$[3].referrerId").value(singleEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$[3].localUnitId").value(singleEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$[3].maxParticipants").value(singleEventCreationRequest.getEventTimeWindowMaxParticipants() * singleEventCreationRequest.getEventTimeWindowOccurrence()))
                .andExpect(jsonPath("$[3].timeWindows").isArray())
                .andExpect(jsonPath("$[3].timeWindows[0].timeWindowId").value("0"))
                .andExpect(jsonPath("$[3].timeWindows[0].start").value(eventStart.plusDays(21).toString()))
                .andExpect(jsonPath("$[3].timeWindows[0].end").value(eventEnd.plusDays(21).toString()))
                .andExpect(jsonPath("$[3].timeWindows[0].maxParticipants").value(singleEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[3].timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$[3].timeWindows[0].participants").isEmpty())
                .andExpect(jsonPath("$[3].recurring").value(true))
                .andExpect(jsonPath("$[4].eventId").value(eventId))
                .andExpect(jsonPath("$[4].sessionId").value("4"))
                .andExpect(jsonPath("$[4].name").value(singleEventCreationRequest.getName()))
                .andExpect(jsonPath("$[4].description").value(singleEventCreationRequest.getDescription()))
                .andExpect(jsonPath("$[4].start").value(eventStart.plusDays(28).toString()))
                .andExpect(jsonPath("$[4].end").value(eventEnd.plusDays(28).toString()))
                .andExpect(jsonPath("$[4].referrerId").value(singleEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$[4].localUnitId").value(singleEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$[4].maxParticipants").value(singleEventCreationRequest.getEventTimeWindowMaxParticipants() * singleEventCreationRequest.getEventTimeWindowOccurrence()))
                .andExpect(jsonPath("$[4].timeWindows").isArray())
                .andExpect(jsonPath("$[4].timeWindows[0].timeWindowId").value("0"))
                .andExpect(jsonPath("$[4].timeWindows[0].start").value(eventStart.plusDays(28).toString()))
                .andExpect(jsonPath("$[4].timeWindows[0].end").value(eventEnd.plusDays(28).toString()))
                .andExpect(jsonPath("$[4].timeWindows[0].maxParticipants").value(singleEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[4].timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$[4].timeWindows[0].participants").isEmpty())
                .andExpect(jsonPath("$[4].recurring").value(true));
    }

    @Test
    @DisplayName("Test that the event sessions endpoint create a recurring event when given the correct parameters")
    public void eventCreateSessionsSuccessTest() throws Exception {
        RecurrentEventCreationRequest recurrentEventCreationRequest = new RecurrentEventCreationRequest(
                "Formation Benevole",
                "Formation pour devenir benevole",
                1L,
                1L,
                Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.of(2002, 3, 1, 10, 0), ZoneId.of("Europe/Paris")).toLocalDateTime()),
                Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.of(2002, 4, 1, 12, 0), ZoneId.of("Europe/Paris")).toLocalDateTime()),
                7,
                30,
                6,
                8
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
                .andExpect(jsonPath("$[0].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusHours(3).toString()))
                .andExpect(jsonPath("$[0].referrerId").value(recurrentEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$[0].localUnitId").value(recurrentEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$[0].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants() * recurrentEventCreationRequest.getEventTimeWindowOccurrence()))
                .andExpect(jsonPath("$[0].numberOfParticipants").value(0))
                .andExpect(jsonPath("$[0].timeWindows").isArray())
                .andExpect(jsonPath("$[0].timeWindows[0].timeWindowId").value("0"))
                .andExpect(jsonPath("$[0].timeWindows[0].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).toString()))
                .andExpect(jsonPath("$[0].timeWindows[0].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusMinutes(30).toString()))
                .andExpect(jsonPath("$[0].timeWindows[0].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[0].timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$[0].timeWindows[0].participants").isEmpty())
                .andExpect(jsonPath("$[0].timeWindows[1].timeWindowId").value("1"))
                .andExpect(jsonPath("$[0].timeWindows[1].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusMinutes(30).toString()))
                .andExpect(jsonPath("$[0].timeWindows[1].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusMinutes(60).toString()))
                .andExpect(jsonPath("$[0].timeWindows[1].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[0].timeWindows[1].participants").isArray())
                .andExpect(jsonPath("$[0].timeWindows[1].participants").isEmpty())
                .andExpect(jsonPath("$[0].timeWindows[2].timeWindowId").value("2"))
                .andExpect(jsonPath("$[0].timeWindows[2].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusMinutes(60).toString()))
                .andExpect(jsonPath("$[0].timeWindows[2].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusMinutes(90).toString()))
                .andExpect(jsonPath("$[0].timeWindows[2].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[0].timeWindows[2].participants").isArray())
                .andExpect(jsonPath("$[0].timeWindows[2].participants").isEmpty())
                .andExpect(jsonPath("$[0].timeWindows[3].timeWindowId").value("3"))
                .andExpect(jsonPath("$[0].timeWindows[3].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusMinutes(90).toString()))
                .andExpect(jsonPath("$[0].timeWindows[3].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusMinutes(120).toString()))
                .andExpect(jsonPath("$[0].timeWindows[3].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[0].timeWindows[3].participants").isArray())
                .andExpect(jsonPath("$[0].timeWindows[3].participants").isEmpty())
                .andExpect(jsonPath("$[0].timeWindows[4].timeWindowId").value("4"))
                .andExpect(jsonPath("$[0].timeWindows[4].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusMinutes(120).toString()))
                .andExpect(jsonPath("$[0].timeWindows[4].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusMinutes(150).toString()))
                .andExpect(jsonPath("$[0].timeWindows[4].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[0].timeWindows[4].participants").isArray())
                .andExpect(jsonPath("$[0].timeWindows[4].participants").isEmpty())
                .andExpect(jsonPath("$[0].timeWindows[5].timeWindowId").value("5"))
                .andExpect(jsonPath("$[0].timeWindows[5].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusMinutes(150).toString()))
                .andExpect(jsonPath("$[0].timeWindows[5].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusMinutes(180).toString()))
                .andExpect(jsonPath("$[0].timeWindows[5].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[0].timeWindows[5].participants").isArray())
                .andExpect(jsonPath("$[0].timeWindows[5].participants").isEmpty())
                .andExpect(jsonPath("$[0].recurring").value(true))
                .andExpect(jsonPath("$[1].eventId").value(eventId))
                .andExpect(jsonPath("$[1].sessionId").value("1"))
                .andExpect(jsonPath("$[1].name").value(recurrentEventCreationRequest.getName()))
                .andExpect(jsonPath("$[1].description").value(recurrentEventCreationRequest.getDescription()))
                .andExpect(jsonPath("$[1].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(7).toString()))
                .andExpect(jsonPath("$[1].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(7).plusHours(3).toString()))
                .andExpect(jsonPath("$[1].referrerId").value(recurrentEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$[1].localUnitId").value(recurrentEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$[1].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants() * recurrentEventCreationRequest.getEventTimeWindowOccurrence()))
                .andExpect(jsonPath("$[1].numberOfParticipants").value(0))
                .andExpect(jsonPath("$[1].timeWindows").isArray())
                .andExpect(jsonPath("$[1].timeWindows[0].timeWindowId").value("0"))
                .andExpect(jsonPath("$[1].timeWindows[0].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(7).toString()))
                .andExpect(jsonPath("$[1].timeWindows[0].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(7).plusMinutes(30).toString()))
                .andExpect(jsonPath("$[1].timeWindows[0].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[1].timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$[1].timeWindows[0].participants").isEmpty())
                .andExpect(jsonPath("$[1].timeWindows[1].timeWindowId").value("1"))
                .andExpect(jsonPath("$[1].timeWindows[1].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(7).plusMinutes(30).toString()))
                .andExpect(jsonPath("$[1].timeWindows[1].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(7).plusMinutes(60).toString()))
                .andExpect(jsonPath("$[1].timeWindows[1].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[1].timeWindows[1].participants").isArray())
                .andExpect(jsonPath("$[1].timeWindows[1].participants").isEmpty())
                .andExpect(jsonPath("$[1].timeWindows[2].timeWindowId").value("2"))
                .andExpect(jsonPath("$[1].timeWindows[2].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(7).plusMinutes(60).toString()))
                .andExpect(jsonPath("$[1].timeWindows[2].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(7).plusMinutes(90).toString()))
                .andExpect(jsonPath("$[1].timeWindows[2].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[1].timeWindows[2].participants").isArray())
                .andExpect(jsonPath("$[1].timeWindows[2].participants").isEmpty())
                .andExpect(jsonPath("$[1].timeWindows[3].timeWindowId").value("3"))
                .andExpect(jsonPath("$[1].timeWindows[3].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(7).plusMinutes(90).toString()))
                .andExpect(jsonPath("$[1].timeWindows[3].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(7).plusMinutes(120).toString()))
                .andExpect(jsonPath("$[1].timeWindows[3].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[1].timeWindows[3].participants").isArray())
                .andExpect(jsonPath("$[1].timeWindows[3].participants").isEmpty())
                .andExpect(jsonPath("$[1].timeWindows[4].timeWindowId").value("4"))
                .andExpect(jsonPath("$[1].timeWindows[4].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(7).plusMinutes(120).toString()))
                .andExpect(jsonPath("$[1].timeWindows[4].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(7).plusMinutes(150).toString()))
                .andExpect(jsonPath("$[1].timeWindows[4].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[1].timeWindows[4].participants").isArray())
                .andExpect(jsonPath("$[1].timeWindows[4].participants").isEmpty())
                .andExpect(jsonPath("$[1].timeWindows[5].timeWindowId").value("5"))
                .andExpect(jsonPath("$[1].timeWindows[5].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(7).plusMinutes(150).toString()))
                .andExpect(jsonPath("$[1].timeWindows[5].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(7).plusMinutes(180).toString()))
                .andExpect(jsonPath("$[1].timeWindows[5].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[1].timeWindows[5].participants").isArray())
                .andExpect(jsonPath("$[1].timeWindows[5].participants").isEmpty())
                .andExpect(jsonPath("$[1].recurring").value(true))
                .andExpect(jsonPath("$[2].eventId").value(eventId))
                .andExpect(jsonPath("$[2].sessionId").value("2"))
                .andExpect(jsonPath("$[2].name").value(recurrentEventCreationRequest.getName()))
                .andExpect(jsonPath("$[2].description").value(recurrentEventCreationRequest.getDescription()))
                .andExpect(jsonPath("$[2].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(14).toString()))
                .andExpect(jsonPath("$[2].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(14).plusHours(3).toString()))
                .andExpect(jsonPath("$[2].referrerId").value(recurrentEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$[2].localUnitId").value(recurrentEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$[2].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants() * recurrentEventCreationRequest.getEventTimeWindowOccurrence()))
                .andExpect(jsonPath("$[2].numberOfParticipants").value(0))
                .andExpect(jsonPath("$[2].timeWindows").isArray())
                .andExpect(jsonPath("$[2].timeWindows[0].timeWindowId").value("0"))
                .andExpect(jsonPath("$[2].timeWindows[0].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(14).toString()))
                .andExpect(jsonPath("$[2].timeWindows[0].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(14).plusMinutes(30).toString()))
                .andExpect(jsonPath("$[2].timeWindows[0].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[2].timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$[2].timeWindows[0].participants").isEmpty())
                .andExpect(jsonPath("$[2].timeWindows[1].timeWindowId").value("1"))
                .andExpect(jsonPath("$[2].timeWindows[1].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(14).plusMinutes(30).toString()))
                .andExpect(jsonPath("$[2].timeWindows[1].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(14).plusMinutes(60).toString()))
                .andExpect(jsonPath("$[2].timeWindows[1].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[2].timeWindows[1].participants").isArray())
                .andExpect(jsonPath("$[2].timeWindows[1].participants").isEmpty())
                .andExpect(jsonPath("$[2].timeWindows[2].timeWindowId").value("2"))
                .andExpect(jsonPath("$[2].timeWindows[2].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(14).plusMinutes(60).toString()))
                .andExpect(jsonPath("$[2].timeWindows[2].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(14).plusMinutes(90).toString()))
                .andExpect(jsonPath("$[2].timeWindows[2].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[2].timeWindows[2].participants").isArray())
                .andExpect(jsonPath("$[2].timeWindows[2].participants").isEmpty())
                .andExpect(jsonPath("$[2].timeWindows[3].timeWindowId").value("3"))
                .andExpect(jsonPath("$[2].timeWindows[3].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(14).plusMinutes(90).toString()))
                .andExpect(jsonPath("$[2].timeWindows[3].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(14).plusMinutes(120).toString()))
                .andExpect(jsonPath("$[2].timeWindows[3].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[2].timeWindows[3].participants").isArray())
                .andExpect(jsonPath("$[2].timeWindows[3].participants").isEmpty())
                .andExpect(jsonPath("$[2].timeWindows[4].timeWindowId").value("4"))
                .andExpect(jsonPath("$[2].timeWindows[4].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(14).plusMinutes(120).toString()))
                .andExpect(jsonPath("$[2].timeWindows[4].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(14).plusMinutes(150).toString()))
                .andExpect(jsonPath("$[2].timeWindows[4].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[2].timeWindows[4].participants").isArray())
                .andExpect(jsonPath("$[2].timeWindows[4].participants").isEmpty())
                .andExpect(jsonPath("$[2].timeWindows[5].timeWindowId").value("5"))
                .andExpect(jsonPath("$[2].timeWindows[5].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(14).plusMinutes(150).toString()))
                .andExpect(jsonPath("$[2].timeWindows[5].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(14).plusMinutes(180).toString()))
                .andExpect(jsonPath("$[2].timeWindows[5].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[2].timeWindows[5].participants").isArray())
                .andExpect(jsonPath("$[2].timeWindows[5].participants").isEmpty())
                .andExpect(jsonPath("$[2].recurring").value(true))
                .andExpect(jsonPath("$[3].eventId").value(eventId))
                .andExpect(jsonPath("$[3].sessionId").value("3"))
                .andExpect(jsonPath("$[3].name").value(recurrentEventCreationRequest.getName()))
                .andExpect(jsonPath("$[3].description").value(recurrentEventCreationRequest.getDescription()))
                .andExpect(jsonPath("$[3].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(21).toString()))
                .andExpect(jsonPath("$[3].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(21).plusHours(3).toString()))
                .andExpect(jsonPath("$[3].referrerId").value(recurrentEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$[3].localUnitId").value(recurrentEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$[3].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants() * recurrentEventCreationRequest.getEventTimeWindowOccurrence()))
                .andExpect(jsonPath("$[3].numberOfParticipants").value(0))
                .andExpect(jsonPath("$[3].timeWindows").isArray())
                .andExpect(jsonPath("$[3].timeWindows[0].timeWindowId").value("0"))
                .andExpect(jsonPath("$[3].timeWindows[0].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(21).toString()))
                .andExpect(jsonPath("$[3].timeWindows[0].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(21).plusMinutes(30).toString()))
                .andExpect(jsonPath("$[3].timeWindows[0].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[3].timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$[3].timeWindows[0].participants").isEmpty())
                .andExpect(jsonPath("$[3].timeWindows[1].timeWindowId").value("1"))
                .andExpect(jsonPath("$[3].timeWindows[1].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(21).plusMinutes(30).toString()))
                .andExpect(jsonPath("$[3].timeWindows[1].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(21).plusMinutes(60).toString()))
                .andExpect(jsonPath("$[3].timeWindows[1].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[3].timeWindows[1].participants").isArray())
                .andExpect(jsonPath("$[3].timeWindows[1].participants").isEmpty())
                .andExpect(jsonPath("$[3].timeWindows[2].timeWindowId").value("2"))
                .andExpect(jsonPath("$[3].timeWindows[2].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(21).plusMinutes(60).toString()))
                .andExpect(jsonPath("$[3].timeWindows[2].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(21).plusMinutes(90).toString()))
                .andExpect(jsonPath("$[3].timeWindows[2].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[3].timeWindows[2].participants").isArray())
                .andExpect(jsonPath("$[3].timeWindows[2].participants").isEmpty())
                .andExpect(jsonPath("$[3].timeWindows[3].timeWindowId").value("3"))
                .andExpect(jsonPath("$[3].timeWindows[3].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(21).plusMinutes(90).toString()))
                .andExpect(jsonPath("$[3].timeWindows[3].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(21).plusMinutes(120).toString()))
                .andExpect(jsonPath("$[3].timeWindows[3].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[3].timeWindows[3].participants").isArray())
                .andExpect(jsonPath("$[3].timeWindows[3].participants").isEmpty())
                .andExpect(jsonPath("$[3].timeWindows[4].timeWindowId").value("4"))
                .andExpect(jsonPath("$[3].timeWindows[4].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(21).plusMinutes(120).toString()))
                .andExpect(jsonPath("$[3].timeWindows[4].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(21).plusMinutes(150).toString()))
                .andExpect(jsonPath("$[3].timeWindows[4].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[3].timeWindows[4].participants").isArray())
                .andExpect(jsonPath("$[3].timeWindows[4].participants").isEmpty())
                .andExpect(jsonPath("$[3].timeWindows[5].timeWindowId").value("5"))
                .andExpect(jsonPath("$[3].timeWindows[5].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(21).plusMinutes(150).toString()))
                .andExpect(jsonPath("$[3].timeWindows[5].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(21).plusMinutes(180).toString()))
                .andExpect(jsonPath("$[3].timeWindows[5].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[3].timeWindows[5].participants").isArray())
                .andExpect(jsonPath("$[3].timeWindows[5].participants").isEmpty())
                .andExpect(jsonPath("$[3].recurring").value(true))
                .andExpect(jsonPath("$[4].eventId").value(eventId))
                .andExpect(jsonPath("$[4].sessionId").value("4"))
                .andExpect(jsonPath("$[4].name").value(recurrentEventCreationRequest.getName()))
                .andExpect(jsonPath("$[4].description").value(recurrentEventCreationRequest.getDescription()))
                .andExpect(jsonPath("$[4].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(28).toString()))
                .andExpect(jsonPath("$[4].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(28).plusHours(3).toString()))
                .andExpect(jsonPath("$[4].referrerId").value(recurrentEventCreationRequest.getReferrerId()))
                .andExpect(jsonPath("$[4].localUnitId").value(recurrentEventCreationRequest.getLocalUnitId()))
                .andExpect(jsonPath("$[4].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants() * recurrentEventCreationRequest.getEventTimeWindowOccurrence()))
                .andExpect(jsonPath("$[4].numberOfParticipants").value(0))
                .andExpect(jsonPath("$[4].timeWindows").isArray())
                .andExpect(jsonPath("$[4].timeWindows[0].timeWindowId").value("0"))
                .andExpect(jsonPath("$[4].timeWindows[0].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(28).toString()))
                .andExpect(jsonPath("$[4].timeWindows[0].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(28).plusMinutes(30).toString()))
                .andExpect(jsonPath("$[4].timeWindows[0].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[4].timeWindows[0].participants").isArray())
                .andExpect(jsonPath("$[4].timeWindows[0].participants").isEmpty())
                .andExpect(jsonPath("$[4].timeWindows[1].timeWindowId").value("1"))
                .andExpect(jsonPath("$[4].timeWindows[1].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(28).plusMinutes(30).toString()))
                .andExpect(jsonPath("$[4].timeWindows[1].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(28).plusMinutes(60).toString()))
                .andExpect(jsonPath("$[4].timeWindows[1].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[4].timeWindows[1].participants").isArray())
                .andExpect(jsonPath("$[4].timeWindows[1].participants").isEmpty())
                .andExpect(jsonPath("$[4].timeWindows[2].timeWindowId").value("2"))
                .andExpect(jsonPath("$[4].timeWindows[2].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(28).plusMinutes(60).toString()))
                .andExpect(jsonPath("$[4].timeWindows[2].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(28).plusMinutes(90).toString()))
                .andExpect(jsonPath("$[4].timeWindows[2].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[4].timeWindows[2].participants").isArray())
                .andExpect(jsonPath("$[4].timeWindows[2].participants").isEmpty())
                .andExpect(jsonPath("$[4].timeWindows[3].timeWindowId").value("3"))
                .andExpect(jsonPath("$[4].timeWindows[3].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(28).plusMinutes(90).toString()))
                .andExpect(jsonPath("$[4].timeWindows[3].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(28).plusMinutes(120).toString()))
                .andExpect(jsonPath("$[4].timeWindows[3].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[4].timeWindows[3].participants").isArray())
                .andExpect(jsonPath("$[4].timeWindows[3].participants").isEmpty())
                .andExpect(jsonPath("$[4].timeWindows[4].timeWindowId").value("4"))
                .andExpect(jsonPath("$[4].timeWindows[4].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(28).plusMinutes(120).toString()))
                .andExpect(jsonPath("$[4].timeWindows[4].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(28).plusMinutes(150).toString()))
                .andExpect(jsonPath("$[4].timeWindows[4].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[4].timeWindows[4].participants").isArray())
                .andExpect(jsonPath("$[4].timeWindows[4].participants").isEmpty())
                .andExpect(jsonPath("$[4].timeWindows[5].timeWindowId").value("5"))
                .andExpect(jsonPath("$[4].timeWindows[5].start").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(28).plusMinutes(150).toString()))
                .andExpect(jsonPath("$[4].timeWindows[5].end").value(timestampToLocalDateTime(recurrentEventCreationRequest.getFirstStart()).plusDays(28).plusMinutes(180).toString()))
                .andExpect(jsonPath("$[4].timeWindows[5].maxParticipants").value(recurrentEventCreationRequest.getEventTimeWindowMaxParticipants()))
                .andExpect(jsonPath("$[4].timeWindows[5].participants").isArray())
                .andExpect(jsonPath("$[4].timeWindows[5].participants").isEmpty())
                .andExpect(jsonPath("$[4].recurring").value(true));
    }

    @Test
    @DisplayName("Test the the event sessions endpoint can delete all the sessions of a recurrent event")
    public void eventDeleteSessionsSuccessTest() throws Exception {
        RecurrentEventCreationRequest recurrentEventCreationRequest = new RecurrentEventCreationRequest(
                "Formation Benevole",
                "Formation pour devenir benevole",
                1L,
                1L,
                Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.of(2002, 3, 1, 10, 0), ZoneId.of("Europe/Paris")).toLocalDateTime()),
                Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.of(2002, 4, 1, 12, 0), ZoneId.of("Europe/Paris")).toLocalDateTime()),
                7,
                30,
                4,
                5
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
