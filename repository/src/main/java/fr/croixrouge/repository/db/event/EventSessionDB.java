package fr.croixrouge.repository.db.event;

import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Table(name = "event-session")
@Entity
public class EventSessionDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_time", nullable = false)
    private ZonedDateTime start;

    @Column(name = "end", nullable = false)
    private ZonedDateTime end;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "event_db_id", nullable = false)
    private EventDB eventDB;

    public EventSessionDB() {

    }

    public EventSessionDB(Long id, ZonedDateTime start, ZonedDateTime end, EventDB eventDB) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.eventDB = eventDB;
    }

    public EventDB getEventDB() {
        return eventDB;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
