package fr.croixrouge.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository {

    Optional<Event> findById(ID eventId);

    Optional<Event> findByEventIdSessionId(ID eventId, ID sessionId);

    List<Event> findByLocalUnitId(String localUnitId);

    List<Event> findByLocalUnitIdAndMonth(String localUnitId, int month, int year);

    ID save(Event event);

    void delete(Event event);

    boolean registerParticipant(ID eventId, ID sessionId, String participantId);
}
