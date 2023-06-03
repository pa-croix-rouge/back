package fr.croixrouge.repository.db.event;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface EventSessionDBRepository extends CrudRepository<EventSessionDB, Long> {
    @Query("select e from EventSessionDB e where e.eventDB.localUnitDB.localUnitID = ?1 and e.startTime > ?2")
    List<EventSessionDB> findByEventDB_LocalUnitDB_LocalUnitIDAndStartTimeAfter(Long localUnitID, ZonedDateTime startTime);

}
