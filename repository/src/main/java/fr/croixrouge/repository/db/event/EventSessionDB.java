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
    @Column(name = "start_time", nullable = false)
    private ZonedDateTime startTime;
    @Column(name = "end_time", nullable = false)
    private ZonedDateTime endTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_db_id", nullable = false)
    private EventDB eventDB;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @ManyToMany()
    @JoinTable(name = "event-session_userdbs",
            joinColumns = @JoinColumn(name = "event_sessiondb_id"),
            inverseJoinColumns = @JoinColumn(name = "userdbs_user_id"))
    private Set<UserDB> userDBs = new LinkedHashSet<>();

    public Set<UserDB> getUserDBs() {
        return userDBs;
    }

    public void setUserDBs(Set<UserDB> userDBs) {
        this.userDBs = userDBs;
    }

    public EventSessionDB(Long id, ZonedDateTime startTime, ZonedDateTime endTime, EventDB eventDB, Integer maxParticipants, Set<UserDB> userDBs) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventDB = eventDB;
        this.maxParticipants = maxParticipants;
        this.userDBs = userDBs;
    }

    public EventDB getEventDB() {
        return eventDB;
    }

    public EventSessionDB() {
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public Long getId() {
        return id;
    }
}
