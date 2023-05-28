package fr.croixrouge.config;

import fr.croixrouge.domain.model.*;
import fr.croixrouge.domain.repository.LocalUnitRepository;
import fr.croixrouge.domain.repository.RoleRepository;
import fr.croixrouge.domain.repository.UserRepository;
import fr.croixrouge.domain.repository.VolunteerRepository;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;
import fr.croixrouge.repository.*;
import fr.croixrouge.storage.repository.ProductRepository;
import fr.croixrouge.storage.repository.StorageProductRepository;
import fr.croixrouge.storage.repository.StorageRepository;
import fr.croixrouge.storage.repository.UserProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryStorageProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryStorageRepository;
import fr.croixrouge.storage.repository.memory.InMemoryUserProductRepository;
import fr.croixrouge.storage.service.StorageProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@Profile("prod")
public class RepositoryConfig {

    private final PasswordEncoder passwordEncoder;

    private final User managerUser;

    private final Address address = new Address(Department.getDepartmentFromPostalCode("91"), "91240", "St Michel sur Orge", "76 rue des Liers");

    private final LocalUnit localUnit;

    public RepositoryConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.managerUser = new User(new ID(2L), "LUManager", passwordEncoder.encode("LUPassword"), List.of("ROLE_ADMIN"));
        this.localUnit = new LocalUnit(new ID(1L), "Unite Local du Val d'Orge", address, managerUser, address.getPostalCode() + "-000");
    }

    @Bean
    public UserRepository userRepository(){
        ArrayList<User> users = new ArrayList<>();
        ID defaultUserId = new ID(1L);
        String defaultUsername = "defaultUser";
        String defaultPassword = passwordEncoder.encode("defaultPassword");
        User defaultUser = new User(defaultUserId, defaultUsername, defaultPassword, List.of());
        users.add(defaultUser);
        users.add(managerUser);
        return new InMemoryUserRepository(users);
    }

    @Bean
    public RoleRepository roleRepository() {
        return new InMemoryRoleRepository();
    }

    @Bean
    public LocalUnitRepository localUnitRepository() {
        ArrayList<LocalUnit> localUnits = new ArrayList<>();
        localUnits.add(localUnit);
        return new InMemoryLocalUnitRepository(localUnits);
    }

    @Bean
    public ProductRepository productRepository() {
        return new InMemoryProductRepository();
    }

    @Bean
    public StorageRepository storageRepository() {
        return new InMemoryStorageRepository();
    }

    @Bean
    public UserProductRepository storageUserProductRepository() {
        return new InMemoryUserProductRepository();
    }

    @Bean
    public StorageProductRepository storageProductRepository() {
        return new InMemoryStorageProductRepository();
    }

    @Bean
    public StorageProductService storageProductServiceCore(StorageProductRepository storageProductRepository) {
        return new StorageProductService(storageProductRepository);
    }

    @Bean
    public EventRepository eventRepository() {
        ArrayList<Event> events = new ArrayList<>();
        ID eventId1 = new ID(1L);
        String eventName1 = "Formation PSC1";
        String eventDescription1 = "Formation au PSC1";
        ZonedDateTime eventStartDate1 = ZonedDateTime.of(LocalDateTime.of(2023, 5, 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate1 = ZonedDateTime.of(LocalDateTime.of(2023, 5, 1, 12, 0), ZoneId.of("Europe/Paris"));
        ID referrerId1 = new ID(1L);
        ID localUnitId1 = new ID(1L);
        int maxParticipants1 = 30;
        List<ID> participants1 = new ArrayList<>();
        EventSession eventSession1 = new EventSession(new ID(0L), eventStartDate1, eventEndDate1, maxParticipants1, participants1);
        Event event1 = new Event(eventId1, eventName1, eventDescription1, referrerId1, localUnitId1, eventStartDate1, eventEndDate1, List.of(eventSession1), 1);
        events.add(event1);

        ID eventId2 = new ID(2L);
        String eventName2 = "Distribution alimentaire";
        String eventDescription2 = "Distribution alimentaire gratuite";
        ZonedDateTime eventStartDate2 = ZonedDateTime.of(LocalDateTime.of(2023, 5, 2, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate2 = ZonedDateTime.of(LocalDateTime.of(2023, 5, 2, 12, 0), ZoneId.of("Europe/Paris"));
        ID referrerId2 = new ID(1L);
        ID localUnitId2 = new ID(1L);
        int maxParticipants2 = 30;
        List<ID> participants2 = new ArrayList<>();
        EventSession eventSession2 = new EventSession(new ID(0L), eventStartDate2, eventEndDate2, maxParticipants2, participants2);
        Event event2 = new Event(eventId2, eventName2, eventDescription2, referrerId2, localUnitId2, eventStartDate2, eventEndDate2, List.of(eventSession2), 1);
        events.add(event2);

        ID eventId3 = new ID(3L);
        String eventName3 = "Formation PSC1";
        String eventDescription3 = "Formation au PSC1";
        ZonedDateTime eventStartDate3 = ZonedDateTime.of(LocalDateTime.of(2023, 6, 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate3 = ZonedDateTime.of(LocalDateTime.of(2023, 6, 1, 12, 0), ZoneId.of("Europe/Paris"));
        ID referrerId3 = new ID(1L);
        ID localUnitId3 = new ID(1L);
        int maxParticipants3 = 30;
        List<ID> participants3 = new ArrayList<>();
        EventSession eventSession3 = new EventSession(new ID(0L), eventStartDate3, eventEndDate3, maxParticipants3, participants3);
        Event event3 = new Event(eventId3, eventName3, eventDescription3, referrerId3, localUnitId3, eventStartDate3, eventEndDate3, List.of(eventSession3), 1);
        events.add(event3);

        ID eventId4 = new ID(4L);
        String eventName4 = "EPISOL";
        String eventDescription4 = "Ouverture de l'EPISOL";
        ZonedDateTime eventStartDate4 = ZonedDateTime.of(LocalDateTime.of(2022, 9, 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate4 = ZonedDateTime.of(LocalDateTime.of(2024, 1, 1, 12, 0), ZoneId.of("Europe/Paris"));
        ID referrerId4 = new ID(1L);
        ID localUnitId4 = new ID(1L);
        int maxParticipants4 = 30;
        List<EventSession> eventSessions4 = new ArrayList<>();
        AtomicInteger sessionCounter = new AtomicInteger(0);
        for (ZonedDateTime sessionTime = eventStartDate4; sessionTime.isBefore(eventEndDate4); sessionTime = sessionTime.plusDays(7)) {
            eventSessions4.add(new EventSession(
                    new ID((long) sessionCounter.getAndIncrement()),
                    sessionTime,
                    sessionTime.plusMinutes(120),
                    maxParticipants4,
                    new ArrayList<>()
            ));
        }
        Event event4 = new Event(eventId4, eventName4, eventDescription4, referrerId4, localUnitId4, eventStartDate4, eventEndDate4, eventSessions4, sessionCounter.get());
        events.add(event4);

        return new InMemoryEventRepository(events);
    }

    @Bean
    public VolunteerRepository volunteerRepository() {
        ArrayList<Volunteer> volunteers = new ArrayList<>();
        Volunteer volunteer1 = new Volunteer(new ID(1L), managerUser, "volunteerFirstName", "volunteerLastName", "+33 6 00 00 00 00", true, localUnit);
        volunteers.add(volunteer1);
        return new InMemoryVolunteerRepository(volunteers);
    }
}
