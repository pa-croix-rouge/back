package fr.croixrouge.repository.db.event;

import fr.croixrouge.repository.db.user.UserDB;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Table(name = "event_time_window")
@Entity
public class EventTimeWindowDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private ZonedDateTime start;

    @Column(name = "end")
    private ZonedDateTime end;

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @ManyToMany
    @JoinTable(name = "event_time_window_userdbs",
            joinColumns = @JoinColumn(name = "event_time_windowdb_id"),
            inverseJoinColumns = @JoinColumn(name = "userdbs_user_id"))
    private Set<UserDB> userDBs = new LinkedHashSet<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_session_db_id", nullable = false)
    private EventSessionDB eventSessionDB;

    public EventTimeWindowDB() {
    }

    public EventTimeWindowDB(Long id, EventSessionDB eventSessionDB,  ZonedDateTime start, ZonedDateTime end, Integer maxParticipants, Set<UserDB> userDBs) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.eventSessionDB = eventSessionDB;
        this.maxParticipants = maxParticipants;
        this.userDBs = userDBs;
    }

    public EventSessionDB getEventSessionDB() {
        return eventSessionDB;
    }

    public void setEventSessionDB(EventSessionDB eventSessionDB) {
        this.eventSessionDB = eventSessionDB;
    }

    public Set<UserDB> getUserDBs() {
        return userDBs;
    }

    public void setUserDBs(Set<UserDB> userDBs) {
        this.userDBs = userDBs;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
