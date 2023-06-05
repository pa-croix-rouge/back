package fr.croixrouge.repository.db.event;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventTimeWindowDBRepository extends CrudRepository<EventTimeWindowDB, Long> {
    @Query("select e from EventTimeWindowDB e where e.eventSessionDB.id = ?1")
    List<EventTimeWindowDB> findByEventSessionDB_Id(Long id);



}
