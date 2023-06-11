package fr.croixrouge.repository.db.event;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface EventSessionDBRepository extends CrudRepository<EventSessionDB, Long> {
    @Query("select e from EventSessionDB e where e.eventDB.localUnitDB.localUnitID = ?1 and e.eventDB.startTime >= ?2")
    List<EventSessionDB> findByEventDB_LocalUnitDB_LocalUnitIDAndStartTimeAfter(Long localUnitID, ZonedDateTime startTime);

    @Query("select e from EventSessionDB e where e.eventDB.id = ?1")
    List<EventSessionDB> findByEventDB_Id(Long id);

    @Query("select e from EventSessionDB e where e.id = ?1 and e.eventDB.id = ?2")
    Optional<EventSessionDB> findByIdAndEventDB_Id(Long id, Long id1);

    @Query("select e from EventSessionDB e where e.eventDB.localUnitDB.localUnitID = ?1 and e.eventDB.startTime >= ?2 and e.eventDB.endTime <= ?3")
    List<EventSessionDB> findByLocalUnitDB_LocalUnitIDAndStartTimeAfterOrEndTimeBefore(Long localUnitID, ZonedDateTime startTime, ZonedDateTime endTime);
}
