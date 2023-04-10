package fr.croixrouge.repository;

import fr.croixrouge.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository {

    Optional<Event> findById(String eventId, String sessionId);

    List<Event> findByLocalUnitId(String localUnitId);

    List<Event> findByLocalUnitIdAndMonth(String localUnitId, int month, int year);

    void save(Event event);

    void delete(String eventId);

    boolean registerParticipant(String eventId, String sessionId, String participantId);
}
