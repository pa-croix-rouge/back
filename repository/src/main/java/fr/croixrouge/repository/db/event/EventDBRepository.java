package fr.croixrouge.repository.db.event;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface EventDBRepository extends CrudRepository<EventDB, Long> {
    @Query("select e from EventDB e where e.localUnitDB.localUnitID = ?1")
    Optional<EventDB> findByLocalUnitDB_LocalUnitID(Long localUnitID);

    @Query("select e from EventDB e where e.localUnitDB.localUnitID = ?1 and e.startTime > ?2 or e.endTime < ?3")
    List<EventDB> findByLocalUnitDB_LocalUnitIDAndStartTimeAfterOrEndTimeBefore(Long localUnitID, ZonedDateTime startTime, ZonedDateTime endTime);

}
