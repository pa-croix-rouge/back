package fr.croixrouge.repository.db.event;

import fr.croixrouge.repository.db.user.UserDB;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Table(name = "event-session")
@Entity
public class EventSessionDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_db_id", nullable = false)
    private EventDB eventDB;

    public EventSessionDB() {

    }

    public EventSessionDB(Long id, EventDB eventDB) {
        this.id = id;
        this.eventDB = eventDB;
    }

    public EventDB getEventDB() {
        return eventDB;
    }

    public Long getId() {
        return id;
    }
}
