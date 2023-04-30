package fr.croixrouge.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository {

    Optional<Event> findById(ID eventId, ID sessionId);

    Optional<Event> findByEventId(ID eventId);

    List<Event> findByLocalUnitId(String localUnitId);

    List<Event> findByLocalUnitIdAndMonth(String localUnitId, int month, int year);

    void save(Event event);

    void delete(ID eventId);

    boolean registerParticipant(ID eventId, ID sessionId, String participantId);
}
