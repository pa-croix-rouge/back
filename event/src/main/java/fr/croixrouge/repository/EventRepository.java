package fr.croixrouge.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDRepository;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends CRUDRepository<ID, Event> {

    Optional<Event> findByEventIdSessionId(ID eventId, ID sessionId);

    List<Event> findByLocalUnitId(ID localUnitId);

    List<EventSession> findByLocalUnitIdOver12Month(ID localUnitId);

    List<Event> findByLocalUnitIdAndMonth(ID localUnitId, int month, int year);

    boolean registerParticipant(ID eventId, ID sessionId, ID participantId);

    boolean updateSingleEvent(ID eventId, ID sessionId, Event event);

    boolean updateEventSessions(ID eventId, ID sessionId, Event event);

    boolean deleteEventSession(ID eventId, ID sessionId);
}
