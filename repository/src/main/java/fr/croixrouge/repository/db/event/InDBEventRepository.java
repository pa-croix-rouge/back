package fr.croixrouge.repository.db.event;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;
import fr.croixrouge.repository.EventRepository;
import fr.croixrouge.repository.db.localunit.InDBLocalUnitRepository;
import fr.croixrouge.repository.db.volunteer.InDBVolunteerRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class InDBEventRepository implements EventRepository {

    private final EventDBRepository eventDBRepository;

    private final EventSessionDBRepository eventSessionDBRepository;

    private final InDBVolunteerRepository inDBVolunteerRepository;

    private final InDBLocalUnitRepository inDBLocalUnitRepository;

    public InDBEventRepository(EventDBRepository eventDBRepository, EventSessionDBRepository eventSessionDBRepository, InDBVolunteerRepository inDBVolunteerRepository, InDBLocalUnitRepository inDBLocalUnitRepository) {
        this.eventDBRepository = eventDBRepository;
        this.eventSessionDBRepository = eventSessionDBRepository;
        this.inDBVolunteerRepository = inDBVolunteerRepository;
        this.inDBLocalUnitRepository = inDBLocalUnitRepository;
    }

    public Event toEvent(EventDB eventDB) {
        return new Event(
                new ID(eventDB.getId()),
                eventDB.getName(),
                eventDB.getDescription(),
                inDBVolunteerRepository.toVolunteer(eventDB.getVolunteerDB()),
                inDBLocalUnitRepository.toLocalUnit(eventDB.getLocalUnitDB()),
                eventDB.getStartTime(),
                eventDB.getEndTime(),
                new ArrayList<>(),
                0
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
                new ArrayList<>()
        );
    }

    public EventSessionDB toEventSessionDB(EventSession eventSession) {
        return new EventSessionDB(
                eventSession.getId() == null ? null : eventSession.getId().value(),
                eventSession.getStart(),
                eventSession.getEnd(),
                null,
                eventSession.getMaxParticipants()
        );
    }

    @Override
    public Optional<Event> findById(ID id) {
        return eventDBRepository.findById(id.value()).map(this::toEvent);
    }

    @Override
    public ID save(Event object) {
        return new ID(eventDBRepository.save(toEventDB(object)).getId());
    }

    @Override
    public void delete(Event object) {
        eventDBRepository.delete(toEventDB(object));
    }

    @Override
    public List<Event> findAll() {
        return StreamSupport.stream(eventDBRepository.findAll().spliterator(), false).map(this::toEvent).toList();
    }

    @Override
    public Optional<Event> findByEventIdSessionId(ID eventId, ID sessionId) {
        return Optional.empty();
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
    public void updateEventSession(EventSession eventSession) {
        eventSessionDBRepository.save(toEventSessionDB(eventSession));
    }

    @Override
    public boolean updateSingleEvent(ID eventId, ID sessionId, Event event) {
        return false;
    }

    @Override
    public boolean updateEventSessions(ID eventId, ID sessionId, Event event) {
        return false;
    }

    @Override
    public boolean deleteEventSession(ID eventId, ID sessionId) {
        return false;
    }
}
