package fr.croixrouge.repository.db.event;

import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;
import fr.croixrouge.model.EventStats;
import fr.croixrouge.model.EventTimeWindow;
import fr.croixrouge.repository.EventRepository;
import fr.croixrouge.repository.db.localunit.InDBLocalUnitRepository;
import fr.croixrouge.repository.db.user.InDBUserRepository;
import fr.croixrouge.repository.db.volunteer.InDBVolunteerRepository;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class InDBEventRepository implements EventRepository {

    private final EventDBRepository eventDBRepository;

    private final EventSessionDBRepository eventSessionDBRepository;

    private final EventTimeWindowDBRepository eventTimeWindowDBRepository;

    private final InDBUserRepository userDBRepository;

    private final InDBVolunteerRepository inDBVolunteerRepository;

    private final InDBLocalUnitRepository inDBLocalUnitRepository;

    public InDBEventRepository(EventDBRepository eventDBRepository, EventSessionDBRepository eventSessionDBRepository, EventTimeWindowDBRepository eventTimeWindowDBRepository, InDBUserRepository userDBRepository, InDBVolunteerRepository inDBVolunteerRepository, InDBLocalUnitRepository inDBLocalUnitRepository) {
        this.eventDBRepository = eventDBRepository;
        this.eventSessionDBRepository = eventSessionDBRepository;
        this.eventTimeWindowDBRepository = eventTimeWindowDBRepository;
        this.userDBRepository = userDBRepository;
        this.inDBVolunteerRepository = inDBVolunteerRepository;
        this.inDBLocalUnitRepository = inDBLocalUnitRepository;
    }


    public EventTimeWindow toEventTimeWindow(EventTimeWindowDB eventTimeWindowDB) {
        return new EventTimeWindow(
                new ID(eventTimeWindowDB.getId()),
                eventTimeWindowDB.getStart(),
                eventTimeWindowDB.getEnd(),
                eventTimeWindowDB.getMaxParticipants(),
                new ArrayList<>(eventTimeWindowDB.getUserDBs().stream()
                        .map(userDBRepository::toUser).map(Entity::getId).toList())
        );
    }

    public EventTimeWindowDB toEventTimeWindowDB(EventTimeWindow eventTimeWindow, EventSessionDB eventDB) {
        return new EventTimeWindowDB(
                eventTimeWindow.getId() == null ? null : eventTimeWindow.getId().value(),
                eventDB,
                eventTimeWindow.getStart(),
                eventTimeWindow.getEnd(),
                eventTimeWindow.getMaxParticipants(),
                eventTimeWindow.getParticipants().stream()
                        .map(id -> userDBRepository.findById(id).orElseThrow())
                        .map(userDBRepository::toUserDB)
                        .collect(Collectors.toSet())
        );
    }

    public Event toEvent(EventDB eventDB) {
        var eventSessions = new ArrayList<>(eventSessionDBRepository.findByEventDB_Id(eventDB.getId()).stream()
                .map(this::toEventSession)
                .toList());
        return this.toEvent(eventDB, eventSessions);
    }

    public Event toEvent(EventDB eventDB, List<EventSession> eventSessions) {
        return new Event(
                new ID(eventDB.getId()),
                eventDB.getName(),
                eventDB.getDescription(),
                inDBVolunteerRepository.toVolunteer(eventDB.getVolunteerDB()),
                inDBLocalUnitRepository.toLocalUnit(eventDB.getLocalUnitDB()),
                eventSessions,
                eventSessions.size()
        );
    }

    public EventDB toEventDB(Event event) {
        return new EventDB(
                event.getId() == null ? null : event.getId().value(),
                inDBLocalUnitRepository.toLocalUnitDB(event.getLocalUnit()),
                inDBVolunteerRepository.toVolunteerDB(event.getReferrer()),
                event.getDescription(),
                event.getName(),
                event.getLastEnd(),
                event.getFirstStart()
        );
    }

    public EventSession toEventSession(EventSessionDB eventSessionDB) {
        var eventTimeWindows = new ArrayList<>(eventTimeWindowDBRepository.findByEventSessionDB_Id(eventSessionDB.getId()).stream()
                .map(this::toEventTimeWindow)
                .toList());

        return new EventSession(
                new ID(eventSessionDB.getId()),
                eventTimeWindows
        );
    }

    public EventSessionDB toEventSessionDB(EventSession eventSession, EventDB eventDB) {
        return new EventSessionDB(
                eventSession.getId() == null ? null : eventSession.getId().value(),
                eventSession.getStart(),
                eventSession.getEnd(),
                eventDB
        );
    }

    @Override
    public Optional<Event> findById(ID id) {
        return eventDBRepository.findById(id.value()).map(this::toEvent);
    }

    @Override
    public ID save(Event object) {
        var eventDB = toEventDB(object);
        var id = new ID(eventDBRepository.save(eventDB).getId());
        object.setId(id);

        for (var session : object.getSessions()) {
            var sessionDB = eventSessionDBRepository.save(toEventSessionDB(session, eventDB));
            for (var timeWindow : session.getTimeWindows()) {
                eventTimeWindowDBRepository.save(toEventTimeWindowDB(timeWindow, sessionDB));
            }
        }

        return id;
    }

    @Override
    public void delete(Event object) {
        var eventDB = toEventDB(object);
        var sessions = eventSessionDBRepository.findByEventDB_Id(eventDB.getId());

        for (var session : sessions) {
            eventTimeWindowDBRepository.deleteAll(eventTimeWindowDBRepository.findByEventSessionDB_Id(session.getId()));
        }

        eventSessionDBRepository.deleteAll(sessions);

        eventDBRepository.delete(eventDB);
    }

    @Override
    public List<Event> findAll() {
        return StreamSupport.stream(eventDBRepository.findAll().spliterator(), false).map(this::toEvent).toList();
    }

    @Override
    public Optional<Event> findByEventIdSessionId(ID eventId, ID sessionId) {
        var sessions = eventSessionDBRepository.findByIdAndEventDB_Id(sessionId.value(), eventId.value());
        if (sessions.isEmpty()) {
            return Optional.empty();
        }
        return eventDBRepository.findById(eventId.value())
                .map(eventDB -> toEvent(eventDB, List.of(sessions.map(this::toEventSession).orElse(null))));
    }

    @Override
    public List<Event> findByLocalUnitId(ID localUnitId) {
        return eventDBRepository.findByLocalUnitDB_LocalUnitID(localUnitId.value()).stream().map(this::toEvent).toList();
    }

    @Override
    public EventStats findByLocalUnitIdOver12Month(ID localUnitId) {
        ZonedDateTime oneYearAgo = ZonedDateTime.of(ZonedDateTime.now().getYear(), ZonedDateTime.now().getMonthValue(), 1, 0, 0, 0, 0, ZonedDateTime.now().getZone()).minusYears(1);
        ZonedDateTime lastDayOfYear = ZonedDateTime.of(ZonedDateTime.now().getYear(), 12, 31, 23, 59, 59, 0, ZonedDateTime.now().getZone());
        ZonedDateTime oneMonthAgo = ZonedDateTime.of(ZonedDateTime.now().getYear(), ZonedDateTime.now().getMonthValue(), 1, 0, 0, 0, 0, ZonedDateTime.now().getZone());
        ZonedDateTime lastDayOfMonth = ZonedDateTime.of(ZonedDateTime.now().getYear(), ZonedDateTime.now().getMonthValue(), 1, 0, 0, 0, 0, ZonedDateTime.now().getZone()).plusMonths(1);
        List<EventTimeWindowDB> timeWindowDBS = eventTimeWindowDBRepository.findByEventSessionDB_EventDB_LocalUnitIDAndStartTimeAfter(localUnitId.value(), ZonedDateTime.now().minusMonths(12));
        Map<EventSessionDB, List<EventTimeWindowDB>> timeWindowDBMap = timeWindowDBS.stream().collect(Collectors.groupingBy(EventTimeWindowDB::getEventSessionDB));

        return new EventStats(
                (int) timeWindowDBMap.entrySet().stream().filter(entry -> entry.getValue().get(0).getStart().isAfter(oneMonthAgo) && entry.getValue().get(0).getStart().isBefore(lastDayOfMonth)).map(Map.Entry::getKey).count(),
                timeWindowDBMap.entrySet().stream().filter(entry -> entry.getValue().get(0).getStart().isAfter(oneMonthAgo) && entry.getValue().get(0).getStart().isBefore(lastDayOfMonth)).map(entry -> entry.getValue().stream().map(eventTimeWindowDB -> eventTimeWindowDB.getUserDBs().size()).reduce(0, Integer::sum)).reduce(0, Integer::sum),
                (int) timeWindowDBMap.entrySet().stream().filter(entry -> entry.getValue().get(0).getStart().isAfter(oneYearAgo) && entry.getValue().get(0).getStart().isBefore(lastDayOfYear)).map(Map.Entry::getKey).count(),
                timeWindowDBMap.entrySet().stream().filter(entry -> entry.getValue().get(0).getStart().isAfter(oneYearAgo) && entry.getValue().get(0).getStart().isBefore(lastDayOfYear)).map(entry -> entry.getValue().stream().map(eventTimeWindowDB -> eventTimeWindowDB.getUserDBs().size()).reduce(0, Integer::sum)).reduce(0, Integer::sum)
        );
    }

    @Override
    public List<Event> findByLocalUnitIdAndMonth(ID localUnitId, int month, int year) {
        ZonedDateTime start = ZonedDateTime.of(year, month, 1, 0, 0, 0, 0, ZonedDateTime.now().getZone());
        ZonedDateTime end = start.plusMonths(1);

        List<EventSessionDB> eventSessions = eventSessionDBRepository.findByLocalUnitDB_LocalUnitIDAndStartTimeAfterOrEndTimeBefore(localUnitId.value(), start, end);

        Set<EventDB> eventDBs = eventSessions.stream().map(EventSessionDB::getEventDB).collect(Collectors.toSet());

        return eventDBs.stream().map(eventDB -> toEvent(eventDB, eventSessions.stream().filter(eventSession -> eventSession.getEventDB().getId().equals(eventDB.getId())).map(this::toEventSession).toList())).toList();
    }

    @Override
    public void updateEventSession(EventSession eventSession, Event event) {
        eventSessionDBRepository.save(toEventSessionDB(eventSession, toEventDB(event)));
    }

    @Override
    public List<Event> findByLocalUnitIdAndTrimester(ID localUnitId, int month, int year) {
        ZonedDateTime start = ZonedDateTime.of(year, month, 1, 0, 0, 0, 0, ZonedDateTime.now().getZone());
        ZonedDateTime end = start.plusMonths(3);

        List<EventSessionDB> eventSessions = eventSessionDBRepository.findByLocalUnitDB_LocalUnitIDAndStartTimeAfterOrEndTimeBefore(localUnitId.value(), start, end);

        Set<EventDB> eventDBs = eventSessions.stream().map(EventSessionDB::getEventDB).collect(Collectors.toSet());

        return eventDBs.stream().map(eventDB -> toEvent(eventDB, eventSessions.stream().filter(eventSession -> eventSession.getEventDB().getId().equals(eventDB.getId())).map(this::toEventSession).toList())).toList();
    }

    @Override
    public boolean registerParticipant(ID eventId, ID sessionId, ID timeWindowId, ID participantId) {
        Event event = this.findById(eventId).orElse(null);
        if (event == null) {
            return false;
        }
        EventSession session = event.getSessions().stream().filter(s -> s.getId().equals(sessionId)).findFirst().orElse(null);
        if (session == null) {
            return false;
        }
        EventTimeWindow timeWindow = session.getTimeWindows().stream().filter(t -> t.getId().equals(timeWindowId)).findFirst().orElse(null);
        if (timeWindow == null) {
            return false;
        }
        if (timeWindow.getParticipants().size() >= timeWindow.getMaxParticipants()) {
            return false;
        }
        if (timeWindow.getParticipants().contains(participantId)) {
            return false;
        }
        timeWindow.getParticipants().add(participantId);

        eventTimeWindowDBRepository.save(toEventTimeWindowDB(timeWindow, toEventSessionDB(session, toEventDB(event))));
        return true;
    }

    @Override
    public boolean removeParticipant(ID eventId, ID sessionId, ID timeWindowId, ID participantId) {
        Event event = this.findById(eventId).orElse(null);
        if (event == null) {
            return false;
        }
        EventSession session = event.getSessions().stream().filter(s -> s.getId().equals(sessionId)).findFirst().orElse(null);
        if (session == null) {
            return false;
        }
        EventTimeWindow timeWindow = session.getTimeWindows().stream().filter(t -> t.getId().equals(timeWindowId)).findFirst().orElse(null);
        if (timeWindow == null) {
            return false;
        }

        if (!timeWindow.getParticipants().contains(participantId)) {
            return false;
        }
        timeWindow.getParticipants().remove(participantId);

        eventTimeWindowDBRepository.save(toEventTimeWindowDB(timeWindow, toEventSessionDB(session, toEventDB(event))));
        return true;
    }

    @Override
    public void updateEventSession(EventSession event) {

    }

    @Override
    public boolean updateSingleEvent(ID eventId, ID sessionId, Event event) {
        if (event.getFirstStart().isBefore(ZonedDateTime.now())) {
            return false;
        }

        Event eventToUpdate = this.findById(eventId).orElse(null);
        if (eventToUpdate == null) {
            return false;
        }

        EventSession sessionToUpdate = eventToUpdate.getSessions().stream().filter(s -> s.getId().equals(sessionId)).findFirst().orElse(null);
        if (sessionToUpdate == null) {
            return false;
        }


        List<EventSession> updatedSessions = new ArrayList<>();
        for (EventSession session : eventToUpdate.getSessions()) {
            if (session.getId().equals(sessionId)) {
                if (sessionToUpdate.getParticipants() > event.getSessions().get(0).getMaxParticipants()) {
                    return false;
                }
                List<EventTimeWindow> updatedTimeWindows = new ArrayList<>();
                for (EventTimeWindow timeWindow : event.getSessions().get(0).getTimeWindows()) {
                    updatedTimeWindows.add(new EventTimeWindow(null, timeWindow.getStart(), timeWindow.getEnd(), timeWindow.getMaxParticipants(), new ArrayList<>()));
                }
                Map<ZonedDateTime, List<ID>> participantsByTimeWindow = sessionToUpdate.getTimeWindows().stream().collect(Collectors.toMap(EventTimeWindow::getStart, EventTimeWindow::getParticipants));
                for (EventTimeWindow timeWindow : updatedTimeWindows) {
                    for (Map.Entry<ZonedDateTime, List<ID>> entry : participantsByTimeWindow.entrySet()) {
                        if (entry.getKey().toEpochSecond() >= timeWindow.getStart().toEpochSecond() && entry.getKey().toEpochSecond() < timeWindow.getEnd().toEpochSecond()) {
                            List<ID> participants = entry.getValue().stream().toList();
                            for (ID participant : participants) {
                                if (timeWindow.getParticipants().size() < timeWindow.getMaxParticipants()) {
                                    timeWindow.getParticipants().add(participant);
                                }
                            }
                            participantsByTimeWindow.get(entry.getKey()).removeAll(timeWindow.getParticipants());
                        }
                    }
                }
                List<ID> participants = participantsByTimeWindow.values().stream().flatMap(Collection::stream).toList();
                int currentTimeWindowIndex = 0;
                for (ID participant : participants) {
                    EventTimeWindow timeWindow = updatedTimeWindows.get(currentTimeWindowIndex);
                    while (timeWindow.getParticipants().size() >= timeWindow.getMaxParticipants()) {
                        currentTimeWindowIndex++;
                        timeWindow = updatedTimeWindows.get(currentTimeWindowIndex);
                    }
                    timeWindow.getParticipants().add(participant);
                }
                updatedSessions.add(new EventSession(session.getId(), updatedTimeWindows));
            } else {
                updatedSessions.add(session);
            }
        }

        for (EventSession session : eventToUpdate.getSessions()) {
            EventSessionDB eventSessionDB = toEventSessionDB(session, toEventDB(event));
            for (EventTimeWindow timeWindow : session.getTimeWindows()) {
                eventTimeWindowDBRepository.delete(toEventTimeWindowDB(timeWindow, eventSessionDB));
            }
        }

        Event updatedEvent = new Event(eventId, event.getName(), event.getDescription(), event.getReferrer(), event.getLocalUnit(), updatedSessions, eventToUpdate.getOccurrences());

        save(updatedEvent);
        return true;
    }

    @Override
    public boolean updateEventSessions(ID eventId, ID sessionId, Event event, int eventTimeWindowDuration, int eventTimeWindowOccurrence, int eventTimeWindowMaxParticipants) {
        if (event.getFirstStart().isBefore(ZonedDateTime.now())) {
            return false;
        }

        Event eventToUpdate = this.findById(eventId).orElse(null);
        if (eventToUpdate == null) {
            return false;
        }

        if (eventToUpdate.getSessions().stream().filter(eventSession -> eventSession.getId().equals(sessionId)).findFirst().orElseThrow().getStart().isBefore(ZonedDateTime.now())) {
            return false;
        }

        EventSession sessionToUpdate = eventToUpdate.getSessions().stream().filter(s -> s.getId().equals(sessionId)).findFirst().orElse(null);
        if (sessionToUpdate == null) {
            return false;
        }

        List<EventSession> updatedSessions = new ArrayList<>();
        for (EventSession session : eventToUpdate.getSessions()) {
            if (session.getStart().isBefore(ZonedDateTime.now())) {
                updatedSessions.add(session);
                continue;
            }
            if (session.getId().equals(sessionId)) {
                if (sessionToUpdate.getParticipants() > event.getSessions().get(0).getMaxParticipants()) {
                    return false;
                }
                List<EventTimeWindow> updatedTimeWindows = new ArrayList<>();
                for (int i = 0; i < eventTimeWindowOccurrence; i++) {
                    updatedTimeWindows.add(new EventTimeWindow(
                            null,
                            session.getStart().plusMinutes((long) i * eventTimeWindowDuration),
                            session.getStart().plusMinutes((long) (i + 1) * eventTimeWindowDuration),
                            eventTimeWindowMaxParticipants,
                            new ArrayList<>()
                    ));
                }
                Map<ZonedDateTime, List<ID>> participantsByTimeWindow = sessionToUpdate.getTimeWindows().stream().collect(Collectors.toMap(EventTimeWindow::getStart, EventTimeWindow::getParticipants));
                for (EventTimeWindow timeWindow : updatedTimeWindows) {
                    for (Map.Entry<ZonedDateTime, List<ID>> entry : participantsByTimeWindow.entrySet()) {
                        if (entry.getKey().toEpochSecond() >= timeWindow.getStart().toEpochSecond() && entry.getKey().toEpochSecond() < timeWindow.getEnd().toEpochSecond()) {
                            List<ID> participants = entry.getValue().stream().toList();
                            for (ID participant : participants) {
                                if (timeWindow.getParticipants().size() < timeWindow.getMaxParticipants()) {
                                    timeWindow.getParticipants().add(participant);
                                }
                            }
                            participantsByTimeWindow.get(entry.getKey()).removeAll(timeWindow.getParticipants());
                        }
                    }
                }
                List<ID> participants = sessionToUpdate.getTimeWindows().stream().map(EventTimeWindow::getParticipants).flatMap(List::stream).toList();
                int currentTimeWindowIndex = 0;
                for (ID participant : participants) {
                    EventTimeWindow timeWindow = updatedTimeWindows.get(currentTimeWindowIndex);
                    if (timeWindow.getParticipants().size() >= timeWindow.getMaxParticipants()) {
                        currentTimeWindowIndex++;
                        timeWindow = updatedTimeWindows.get(currentTimeWindowIndex);
                    }
                    timeWindow.getParticipants().add(participant);
                }
                updatedSessions.add(new EventSession(session.getId(), updatedTimeWindows));
            } else {
                if (session.getParticipants() > event.getSessions().get(0).getMaxParticipants()) {
                    return false;
                }
                List<EventTimeWindow> updatedTimeWindows = new ArrayList<>();
                for (int i = 0; i < eventTimeWindowOccurrence; i++) {
                    updatedTimeWindows.add(new EventTimeWindow(
                            null,
                            session.getStart().plusMinutes((long) i * eventTimeWindowDuration),
                            session.getStart().plusMinutes((long) (i + 1) * eventTimeWindowDuration),
                            eventTimeWindowMaxParticipants,
                            new ArrayList<>()
                    ));
                }Map<ZonedDateTime, List<ID>> participantsByTimeWindow = session.getTimeWindows().stream().collect(Collectors.toMap(EventTimeWindow::getStart, EventTimeWindow::getParticipants));
                for (EventTimeWindow timeWindow : updatedTimeWindows) {
                    for (Map.Entry<ZonedDateTime, List<ID>> entry : participantsByTimeWindow.entrySet()) {
                        if (entry.getKey().toEpochSecond() >= timeWindow.getStart().toEpochSecond() && entry.getKey().toEpochSecond() < timeWindow.getEnd().toEpochSecond()) {
                            List<ID> participants = entry.getValue().stream().toList();
                            for (ID participant : participants) {
                                if (timeWindow.getParticipants().size() < timeWindow.getMaxParticipants()) {
                                    timeWindow.getParticipants().add(participant);
                                }
                            }
                            participantsByTimeWindow.get(entry.getKey()).removeAll(timeWindow.getParticipants());
                        }
                    }
                }
                List<ID> participants = session.getTimeWindows().stream().map(EventTimeWindow::getParticipants).flatMap(List::stream).toList();
                int currentTimeWindowIndex = 0;
                for (ID participant : participants) {
                    EventTimeWindow timeWindow = updatedTimeWindows.get(currentTimeWindowIndex);
                    if (timeWindow.getParticipants().size() >= timeWindow.getMaxParticipants()) {
                        currentTimeWindowIndex++;
                        timeWindow = updatedTimeWindows.get(currentTimeWindowIndex);
                    }
                    timeWindow.getParticipants().add(participant);
                }
                updatedSessions.add(new EventSession(session.getId(), updatedTimeWindows));
            }
        }

        for (EventSession session : eventToUpdate.getSessions()) {
            EventSessionDB eventSessionDB = toEventSessionDB(session, toEventDB(event));
            for (EventTimeWindow timeWindow : session.getTimeWindows()) {
                eventTimeWindowDBRepository.delete(toEventTimeWindowDB(timeWindow, eventSessionDB));
            }
        }

        Event updatedEvent = new Event(eventId, event.getName(), event.getDescription(), event.getReferrer(), event.getLocalUnit(), updatedSessions, eventToUpdate.getOccurrences());

        save(updatedEvent);
        return true;
    }

    @Override
    public boolean deleteEventSession(ID eventId, ID sessionId) {
        EventDB eventDB = eventDBRepository.findById(eventId.value()).orElse(null);
        if (eventDB == null) {
            return false;
        }
        EventSessionDB eventSessionDB = eventSessionDBRepository.findById(sessionId.value()).orElse(null);
        if (eventSessionDB == null) {
            return false;
        }
        for (EventTimeWindow timeWindow : toEventSession(eventSessionDB).getTimeWindows()) {
            eventTimeWindowDBRepository.delete(toEventTimeWindowDB(timeWindow, eventSessionDB));
        }
        eventSessionDBRepository.deleteById(sessionId.value());
        return true;
    }
}
