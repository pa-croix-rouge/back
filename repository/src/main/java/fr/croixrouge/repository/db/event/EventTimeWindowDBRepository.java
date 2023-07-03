package fr.croixrouge.repository.db.event;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface EventTimeWindowDBRepository extends CrudRepository<EventTimeWindowDB, Long> {
    @Query("select e from EventTimeWindowDB e where e.eventSessionDB.id = ?1")
    List<EventTimeWindowDB> findByEventSessionDB_Id(Long id);

    @Override
    <S extends EventTimeWindowDB> S save(S entity);

    @Query("select e from EventTimeWindowDB e where e.eventSessionDB.eventDB.localUnitDB.localUnitID = ?1 and e.start >= ?2")
    List<EventTimeWindowDB> findByEventSessionDB_EventDB_LocalUnitIDAndStartTimeAfter(Long localUnitID, ZonedDateTime startTime);
}
