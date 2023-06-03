package fr.croixrouge.repository.db.event;

import fr.croixrouge.repository.db.localunit.LocalUnitDB;
import fr.croixrouge.repository.db.volunteer.VolunteerDB;
import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Table(name = "event")
@Entity
public class EventDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "local_unit_db_localunit_id", nullable = false)
    private LocalUnitDB localUnitDB;

    @ManyToOne(optional = false)
    @JoinColumn(name = "volunteer_db_id", nullable = false)
    private VolunteerDB volunteerDB;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "end_time", nullable = false)
    private ZonedDateTime endTime;

    @Column(name = "start_time", nullable = false)
    private ZonedDateTime startTime;

    public EventDB(Long id, LocalUnitDB localUnitDB, VolunteerDB volunteerDB, String description, String name, ZonedDateTime endTime, ZonedDateTime startTime) {
        this.id = id;
        this.localUnitDB = localUnitDB;
        this.volunteerDB = volunteerDB;
        this.description = description;
        this.name = name;
        this.endTime = endTime;
        this.startTime = startTime;
    }

    public EventDB() {
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VolunteerDB getVolunteerDB() {
        return volunteerDB;
    }

    public void setVolunteerDB(VolunteerDB volunteerDB) {
        this.volunteerDB = volunteerDB;
    }

    public LocalUnitDB getLocalUnitDB() {
        return localUnitDB;
    }

    public void setLocalUnitDB(LocalUnitDB localUnitDB) {
        this.localUnitDB = localUnitDB;
    }

    public Long getId() {
        return id;
    }
}
