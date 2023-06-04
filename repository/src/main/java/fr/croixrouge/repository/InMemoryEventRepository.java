package fr.croixrouge.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.IDGenerator;
import fr.croixrouge.domain.repository.InMemoryCRUDRepository;
import fr.croixrouge.domain.repository.IncrementalIDGenerator;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;
import fr.croixrouge.model.EventTimeWindow;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryEventRepository extends InMemoryCRUDRepository<ID, Event> implements EventRepository {

    private final IDGenerator<ID> eventSessionIdGenerator = new IncrementalIDGenerator();

    public InMemoryEventRepository(List<Event> objects) {
        super(objects, new IncrementalIDGenerator());
    }

    public InMemoryEventRepository() {
        super(new ArrayList<>(), new IncrementalIDGenerator());
    }

    @Override
    public Optional<Event> findByEventIdSessionId(ID eventId, ID sessionId) {
        final Optional<Event> event =  this.findById(eventId);
        if (event.isEmpty()) {
            return Optional.empty();
        }
        final Optional<EventSession> session = event.get().getSessions().stream().filter(s -> s.getId().equals(sessionId)).findFirst();
        if (session.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new Event(event.get().getId(), event.get().getName(), event.get().getDescription(), event.get().getReferrer(), event.get().getLocalUnit(),  List.of(session.get()), event.get().getOccurrences()));
    }

    @Override
    public List<Event> findByLocalUnitId(ID localUnitId) {
        return this.objects.stream().filter(event -> event.getLocalUnit().getId().equals(localUnitId)).collect(Collectors.toList());
    }

    @Override
    public List<EventSession> findByLocalUnitIdOver12Month(ID localUnitId) {
        ChronoZonedDateTime<LocalDate> now = ZonedDateTime.now();
        return this.objects.stream()
                .filter(event -> event.getLocalUnit().getId().equals(localUnitId))
                .map(Event::getSessions)
                .flatMap(List::stream)
                .filter(session -> session.getStart().isAfter(now.minus(12, ChronoUnit.MONTHS)))
                .toList();
    }

    @Override
    public List<Event> findByLocalUnitIdAndMonth(ID localUnitId, int month, int year) {
        List<Event> localUnitEvents = this.objects.stream().filter(event -> event.getLocalUnit().getId().equals(localUnitId)).toList();
        List<Event> result = new ArrayList<>();
        for (Event event : localUnitEvents) {
            List<EventSession> sessionsInMonth = event.getSessions().stream().filter(session -> session.getStart().getMonthValue() == month && session.getStart().getYear() == year || session.getEnd().getMonthValue() == month && session.getEnd().getYear() == year).toList();
            if (!sessionsInMonth.isEmpty()) {
                result.add(new Event(event.getId(), event.getName(), event.getDescription(), event.getReferrer(), event.getLocalUnit(), sessionsInMonth, event.getOccurrences()));
            }
        }
        return result;
    }

    @Override
    public List<Event> findByLocalUnitIdAndTrimester(ID localUnitId, int month, int year) {
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        List<Event> localUnitEvents = this.objects.stream().filter(event -> event.getLocalUnitId().equals(localUnitId)).toList();
        List<Event> result = new ArrayList<>();
        for (Event event : localUnitEvents) {
            List<EventSession> sessionsInMonth = event.getSessions().stream().filter(session ->
                    (session.getStart().getMonthValue() == start.getMonthValue() && session.getStart().getYear() == start.getYear()) ||
                    (session.getStart().getMonthValue() == start.plusMonths(1).getMonthValue() && session.getStart().getYear() == start.plusMonths(1).getYear()) ||
                    (session.getStart().getMonthValue() == start.plusMonths(2).getMonthValue() && session.getStart().getYear() == start.plusMonths(2).getYear())).toList();
            if (!sessionsInMonth.isEmpty()) {
                result.add(new Event(event.getId(), event.getName(), event.getDescription(), event.getReferrerId(), event.getLocalUnitId(), sessionsInMonth, event.getOccurrences()));
            }
        }
        return result;
    }

    @Override
    public void updateEventSession(EventSession eventSession, Event event) {
        Event eventToUpdate = this.findById(event.getId()).orElse(null);
        if (eventToUpdate == null) {
            return;
        }

        eventToUpdate.getSessions().remove(eventSession);
        eventToUpdate.getSessions().add(eventSession);
    }

    @Override
    public ID save(Event event) {
        ID eventId = event.getId() == null ? idGenerator.generate() : event.getId();
        Event eventToSave = new Event(eventId, event.getName(), event.getDescription(), event.getReferrer(), event.getLocalUnit(),new ArrayList<>(), event.getOccurrences());
        for (EventSession session : event.getSessions()) {
            EventSession sessionToSave = new EventSession(new ID( (long) eventToSave.getSessions().size()), new ArrayList<>());
            for (EventTimeWindow timeWindow : session.getTimeWindows()) {
                sessionToSave.getTimeWindows().add(new EventTimeWindow(new ID( (long) sessionToSave.getTimeWindows().size()), timeWindow.getStart(), timeWindow.getEnd(), timeWindow.getMaxParticipants(), new ArrayList<>()));
            }
            eventToSave.getSessions().add(sessionToSave);
        }

        this.objects.add(eventToSave);
        return eventId;
    }

    @Override
    public boolean registerParticipant(ID eventId, ID sessionId, ID timeWindowId, ID participantId) {
        Event event = this.findById(eventId).orElse(null);
        if (event == null) {
            return false;
        }
        EventSession session = event.getSessions().stream().filter(s -> s.getId().equals(sessionId)).findFirst().orElse(null);
        if (session == null) {
            return false;
        }
        EventTimeWindow timeWindow = session.getTimeWindows().stream().filter(t -> t.getId().equals(timeWindowId)).findFirst().orElse(null);
        if (timeWindow == null) {
            return false;
        }
        if (timeWindow.getParticipants().size() >= timeWindow.getMaxParticipants()) {
            return false;
        }
        if (timeWindow.getParticipants().contains(participantId)) {
            return false;
        }
        timeWindow.getParticipants().add(participantId);
        this.objects.remove(event);
        this.objects.add(event);
        return true;
    }

    @Override
    public boolean updateSingleEvent(ID eventId, ID sessionId, Event event) {
        Event eventToUpdate = this.findById(eventId).orElse(null);
        if (eventToUpdate == null) {
            return false;
        }

        List<EventSession> updatedSessions = new ArrayList<>();
        for (EventSession session : eventToUpdate.getSessions()) {
            if (session.getId().equals(sessionId)) {
                if (sessionToUpdate.getParticipants() > event.getSessions().get(0).getMaxParticipants()) {
                    return false;
                }
                List<EventTimeWindow> updatedTimeWindows = new ArrayList<>();
                for (EventTimeWindow timeWindow : event.getSessions().get(0).getTimeWindows()) {
                    updatedTimeWindows.add(new EventTimeWindow(new ID(String.valueOf(updatedTimeWindows.size())), timeWindow.getStart(), timeWindow.getEnd(), timeWindow.getMaxParticipants(), new ArrayList<>()));
                }
                List<ID> participants = sessionToUpdate.getTimeWindows().stream().map(EventTimeWindow::getParticipants).flatMap(List::stream).toList();
                int currentTimeWindowIndex = 0;
                for (ID participant : participants) {
                    EventTimeWindow timeWindow = updatedTimeWindows.get(currentTimeWindowIndex);
                    if (timeWindow.getParticipants().size() >= timeWindow.getMaxParticipants()) {
                        currentTimeWindowIndex++;
                        timeWindow = updatedTimeWindows.get(currentTimeWindowIndex);
                    }
                    timeWindow.getParticipants().add(participant);
                }
                updatedSessions.add(new EventSession(session.getId(), updatedTimeWindows));
            } else {
                updatedSessions.add(session);
            }
        }
        Event updatedEvent = new Event(eventId, event.getName(), event.getDescription(), event.getReferrer(), event.getLocalUnit(), updatedSessions, eventToUpdate.getOccurrences());
        this.objects.remove(eventToUpdate);
        this.objects.add(updatedEvent);
        return true;
    }

    @Override
    public boolean updateEventSessions(ID eventId, ID sessionId, Event event, int eventTimeWindowDuration, int eventTimeWindowOccurrence, int eventTimeWindowMaxParticipants) {
        Event eventToUpdate = this.findById(eventId).orElse(null);
        if (eventToUpdate == null) {
            return false;
        }

        List<EventSession> updatedSessions = new ArrayList<>();
        for (EventSession session : eventToUpdate.getSessions()) {
            if (session.getId().equals(sessionId)) {
                if (sessionToUpdate.getParticipants() > event.getSessions().get(0).getMaxParticipants()) {
                    return false;
                }
                List<EventTimeWindow> updatedTimeWindows = new ArrayList<>();
                for (int i = 0; i < eventTimeWindowOccurrence; i++) {
                    updatedTimeWindows.add(new EventTimeWindow(
                            new ID(String.valueOf(updatedTimeWindows.size())),
                            session.getStart().plusMinutes((long) i * eventTimeWindowDuration),
                            session.getStart().plusMinutes((long) (i + 1) * eventTimeWindowDuration),
                            eventTimeWindowMaxParticipants,
                            new ArrayList<>()
                    ));
                }
                List<ID> participants = sessionToUpdate.getTimeWindows().stream().map(EventTimeWindow::getParticipants).flatMap(List::stream).toList();
                int currentTimeWindowIndex = 0;
                for (ID participant : participants) {
                    EventTimeWindow timeWindow = updatedTimeWindows.get(currentTimeWindowIndex);
                    if (timeWindow.getParticipants().size() >= timeWindow.getMaxParticipants()) {
                        currentTimeWindowIndex++;
                        timeWindow = updatedTimeWindows.get(currentTimeWindowIndex);
                    }
                    timeWindow.getParticipants().add(participant);
                }
                updatedSessions.add(new EventSession(session.getId(), updatedTimeWindows));
            } else {
                if (sessionToUpdate.getParticipants() > event.getSessions().get(0).getMaxParticipants()) {
                    return false;
                }
                List<EventTimeWindow> updatedTimeWindows = new ArrayList<>();
                for (int i = 0; i < eventTimeWindowOccurrence; i++) {
                    updatedTimeWindows.add(new EventTimeWindow(
                            new ID(String.valueOf(updatedTimeWindows.size())),
                            session.getStart().plusMinutes((long) i * eventTimeWindowDuration),
                            session.getStart().plusMinutes((long) (i + 1) * eventTimeWindowDuration),
                            eventTimeWindowMaxParticipants,
                            new ArrayList<>()
                    ));
                }
                List<ID> participants = sessionToUpdate.getTimeWindows().stream().map(EventTimeWindow::getParticipants).flatMap(List::stream).toList();
                int currentTimeWindowIndex = 0;
                for (ID participant : participants) {
                    EventTimeWindow timeWindow = updatedTimeWindows.get(currentTimeWindowIndex);
                    if (timeWindow.getParticipants().size() >= timeWindow.getMaxParticipants()) {
                        currentTimeWindowIndex++;
                        timeWindow = updatedTimeWindows.get(currentTimeWindowIndex);
                    }
                    timeWindow.getParticipants().add(participant);
                }
                updatedSessions.add(new EventSession(session.getId(), updatedTimeWindows));
            }
        }
        Event updatedEvent = new Event(eventId, event.getName(), event.getDescription(), event.getReferrer(), event.getLocalUnit(), updatedSessions, eventToUpdate.getOccurrences());
        this.objects.remove(eventToUpdate);
        this.objects.add(updatedEvent);
        return true;
    }

    @Override
    public boolean deleteEventSession(ID eventId, ID sessionId) {
        Event event = this.findById(eventId).orElse(null);
        if (event == null) {
            return false;
        }
        EventSession session = event.getSessions().stream().filter(s -> s.getId().equals(sessionId)).findFirst().orElse(null);
        if (session == null) {
            return false;
        }
        this.objects.remove(event);
        event.getSessions().remove(session);
        this.objects.add(event);
        return true;
    }
}
