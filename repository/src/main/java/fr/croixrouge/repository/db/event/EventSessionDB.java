package fr.croixrouge.repository.db.event;

import jakarta.persistence.*;

@Table(name = "event-session")
@Entity
public class EventSessionDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
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

    public void setId(Long id) {
        this.id = id;
    }
}
