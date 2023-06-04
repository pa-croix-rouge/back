package fr.croixrouge.repository.db.event;

import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;
import fr.croixrouge.repository.EventRepository;
import fr.croixrouge.repository.db.localunit.InDBLocalUnitRepository;
import fr.croixrouge.repository.db.user.InDBUserRepository;
import fr.croixrouge.repository.db.user.UserDB;
import fr.croixrouge.repository.db.volunteer.InDBVolunteerRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class InDBEventRepository implements EventRepository {

    private final EventDBRepository eventDBRepository;

    private final EventSessionDBRepository eventSessionDBRepository;

    private final InDBUserRepository userDBRepository;

    private final InDBVolunteerRepository inDBVolunteerRepository;

    private final InDBLocalUnitRepository inDBLocalUnitRepository;

    public InDBEventRepository(EventDBRepository eventDBRepository, EventSessionDBRepository eventSessionDBRepository, InDBUserRepository userDBRepository, InDBVolunteerRepository inDBVolunteerRepository, InDBLocalUnitRepository inDBLocalUnitRepository) {
        this.eventDBRepository = eventDBRepository;
        this.eventSessionDBRepository = eventSessionDBRepository;
        this.userDBRepository = userDBRepository;
        this.inDBVolunteerRepository = inDBVolunteerRepository;
        this.inDBLocalUnitRepository = inDBLocalUnitRepository;
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
                eventDB.getStartTime(),
                eventDB.getEndTime(),
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


        return new EventSession(
                new ID(eventSessionDB.getId()),
                eventSessionDB.getStartTime(),
                eventSessionDB.getEndTime(),
                eventSessionDB.getMaxParticipants(),
                new ArrayList<>(eventSessionDB.getUserDBs().stream()
                        .map(userDBRepository::toUser).map(Entity::getId).toList())
        );
    }

    public EventSessionDB toEventSessionDB(EventSession eventSession, EventDB eventDB, Set<UserDB> participants) {
        return new EventSessionDB(
                eventSession.getId() == null ? null : eventSession.getId().value(),
                eventSession.getStart(),
                eventSession.getEnd(),
                eventDB,
                eventSession.getMaxParticipants(),
                participants
        );
    }

    public EventSessionDB toEventSessionDB(EventSession eventSession, EventDB eventDB) {
        return toEventSessionDB(eventSession,
                eventDB,
                eventSession.getParticipants().stream()
                        .map(id -> userDBRepository.findById(id).orElseThrow())
                        .map(userDBRepository::toUserDB)
                        .collect(Collectors.toSet())
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
            eventSessionDBRepository.save(toEventSessionDB(session, eventDB));
        }

        return id;
    }

    @Override
    public void delete(Event object) {
        var eventDB = toEventDB(object);
        var sessions = eventSessionDBRepository.findByEventDB_Id(eventDB.getId());
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
                .map(eventDB -> toEvent(eventDB, List.of(sessions.map(this::toEventSession).get())));
    }

    @Override
    public List<Event> findByLocalUnitId(ID localUnitId) {
        return eventDBRepository.findByLocalUnitDB_LocalUnitID(localUnitId.value()).stream().map(this::toEvent).toList();
    }

    @Override
    public List<EventSession> findByLocalUnitIdOver12Month(ID localUnitId) {
        return eventSessionDBRepository.
                findByEventDB_LocalUnitDB_LocalUnitIDAndStartTimeAfter(localUnitId.value(), ZonedDateTime.now().minusMonths(12))
                .stream().map(this::toEventSession).toList();
    }

    @Override
    public List<Event> findByLocalUnitIdAndMonth(ID localUnitId, int month, int year) {
        ZonedDateTime start = ZonedDateTime.of(year, month, 1, 0, 0, 0, 0, ZonedDateTime.now().getZone());
        ZonedDateTime end = start.plusMonths(1);

        return eventDBRepository
                .findByLocalUnitDB_LocalUnitIDAndStartTimeAfterOrEndTimeBefore(localUnitId.value(), start, end)
                .stream().map(this::toEvent).toList();
    }

    @Override
    public void updateEventSession(EventSession eventSession, Event event) {
        eventSessionDBRepository.save(toEventSessionDB(eventSession, toEventDB(event)));
    }

    @Override
    public boolean updateSingleEvent(ID eventId, ID sessionId, Event event) {
        Event eventToUpdate = this.findById(eventId).orElse(null);
        if (eventToUpdate == null) {
            return false;
        }

        List<EventSession> updatedSessions = new ArrayList<>();
        for (EventSession session : eventToUpdate.getSessions()) {
            if (session.getId().equals(sessionId)) {
                updatedSessions.add(new EventSession(session.getId(), event.getSessions().get(0).getStart(), event.getSessions().get(0).getEnd(), event.getSessions().get(0).getMaxParticipants(), session.getParticipants()));
            } else {
                updatedSessions.add(session);
            }
        }
        Event updatedEvent = new Event(eventId, event.getName(), event.getDescription(), event.getReferrer(), event.getLocalUnit(), event.getFirstStart(), event.getLastEnd(), updatedSessions, eventToUpdate.getOccurrences());

        this.save(updatedEvent);
        return true;
    }

    @Override
    public boolean updateEventSessions(ID eventId, ID sessionId, Event event) {
        Event eventToUpdate = this.findById(eventId).orElse(null);
        if (eventToUpdate == null) {
            return false;
        }

        List<EventSession> updatedSessions = new ArrayList<>();
        for (EventSession session : eventToUpdate.getSessions()) {
            if (session.getId().equals(sessionId)) {
                updatedSessions.add(new EventSession(session.getId(), event.getSessions().get(0).getStart(), event.getSessions().get(0).getEnd(), event.getSessions().get(0).getMaxParticipants(), session.getParticipants()));
            } else {
                updatedSessions.add(new EventSession(session.getId(), session.getStart(), session.getEnd(), event.getSessions().get(0).getMaxParticipants(), session.getParticipants()));
            }
        }
        Event updatedEvent = new Event(eventId, event.getName(), event.getDescription(), event.getReferrer(), event.getLocalUnit(), event.getFirstStart(), event.getLastEnd(), updatedSessions, eventToUpdate.getOccurrences());

        this.save(updatedEvent);
        return true;
    }

    @Override
    public boolean deleteEventSession(ID eventId, ID sessionId) {
        eventSessionDBRepository.deleteById(sessionId.value());
        return true;
    }
}
