package fr.croixrouge.config;

import fr.croixrouge.domain.model.*;
import fr.croixrouge.domain.repository.LocalUnitRepository;
import fr.croixrouge.domain.repository.RoleRepository;
import fr.croixrouge.domain.repository.UserRepository;
import fr.croixrouge.domain.repository.VolunteerRepository;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;
import fr.croixrouge.model.EventTimeWindow;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class RepositoryConfig {

    private final PasswordEncoder passwordEncoder;

    private final User managerUser;

    private final User southernManagerUser;

    private final Address address = new Address(Department.getDepartmentFromPostalCode("91"), "91240", "St Michel sur Orge", "76 rue des Liers");

    private final Address address2 = new Address(Department.getDepartmentFromPostalCode("83"), "83000", "Toulon", "62 Boulevard de Strasbourg");

    private final LocalUnit localUnit;

    private final LocalUnit southernLocalUnit;

    public RepositoryConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.managerUser = new User(new ID("2"), "LUManager", passwordEncoder.encode("LUPassword"), List.of("ROLE_ADMIN"));
        this.localUnit = new LocalUnit(new ID("1"), "Unite Local du Val d'Orge", address, managerUser, address.getPostalCode() + "-000");
        this.southernManagerUser = new User(new ID("3"), "SLUManager", passwordEncoder.encode("SLUPassword"), List.of("ROLE_ADMIN"));
        this.southernLocalUnit = new LocalUnit(new ID("2"), "Unite Local du Sud", address2, southernManagerUser, address2.getPostalCode() + "-000");
    }

    @Bean
    public UserRepository userRepository(){
        ArrayList<User> users = new ArrayList<>();
        ID defaultUserId = new ID("1");
        String defaultUsername = "defaultUser";
        String defaultPassword = passwordEncoder.encode("defaultPassword");
        User defaultUser = new User(defaultUserId, defaultUsername, defaultPassword, List.of());
        users.add(defaultUser);
        users.add(managerUser);
        users.add(southernManagerUser);
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
        localUnits.add(southernLocalUnit);
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
        ID eventId1 = new ID("1");
        String eventName1 = "Formation PSC1";
        String eventDescription1 = "Formation au PSC1";
        ZonedDateTime eventStartDate1 = ZonedDateTime.of(LocalDateTime.of(2023, 5, 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate1 = ZonedDateTime.of(LocalDateTime.of(2023, 5, 1, 12, 0), ZoneId.of("Europe/Paris"));
        ID referrerId1 = new ID("1");
        ID localUnitId1 = new ID("1");
        int maxParticipants1 = 30;
        List<ID> participants1 = new ArrayList<>();
        EventTimeWindow eventTimeWindow1 = new EventTimeWindow(new ID("0"), eventStartDate1, eventEndDate1, maxParticipants1, participants1);
        EventSession eventSession1 = new EventSession(new ID("0"), List.of(eventTimeWindow1));
        Event event1 = new Event(eventId1, eventName1, eventDescription1, referrerId1, localUnitId1, List.of(eventSession1), 1);
        events.add(event1);

        ID eventId2 = new ID("2");
        String eventName2 = "Distribution alimentaire";
        String eventDescription2 = "Distribution alimentaire gratuite";
        ZonedDateTime eventStartDate2 = ZonedDateTime.of(LocalDateTime.of(2023, 5, 2, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate2 = ZonedDateTime.of(LocalDateTime.of(2023, 5, 2, 12, 0), ZoneId.of("Europe/Paris"));
        ID referrerId2 = new ID("1");
        ID localUnitId2 = new ID("1");
        int maxParticipants2 = 30;
        List<ID> participants2 = new ArrayList<>();
        List<EventTimeWindow> eventTimeWindowList2 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            eventTimeWindowList2.add(new EventTimeWindow(new ID(String.valueOf(i)), eventStartDate2.plusMinutes(i * 40), eventEndDate2.plusMinutes((i + 1) * 40), maxParticipants2 / 3, participants2));
        }
        EventSession eventSession2 = new EventSession(new ID("0"), eventTimeWindowList2);
        Event event2 = new Event(eventId2, eventName2, eventDescription2, referrerId2, localUnitId2, List.of(eventSession2), 1);
        events.add(event2);

        ID eventId3 = new ID("3");
        String eventName3 = "Formation PSC1";
        String eventDescription3 = "Formation au PSC1";
        ZonedDateTime eventStartDate3 = ZonedDateTime.of(LocalDateTime.of(2023, 6, 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate3 = ZonedDateTime.of(LocalDateTime.of(2023, 6, 1, 12, 0), ZoneId.of("Europe/Paris"));
        ID referrerId3 = new ID("1");
        ID localUnitId3 = new ID("1");
        int maxParticipants3 = 30;
        List<ID> participants3 = new ArrayList<>();
        EventTimeWindow eventTimeWindow3 = new EventTimeWindow(new ID("0"), eventStartDate3, eventEndDate3, maxParticipants3, participants3);
        EventSession eventSession3 = new EventSession(new ID("0"), List.of(eventTimeWindow3));
        Event event3 = new Event(eventId3, eventName3, eventDescription3, referrerId3, localUnitId3, List.of(eventSession3), 1);
        events.add(event3);

        ID eventId4 = new ID("4");
        String eventName4 = "EPISOL";
        String eventDescription4 = "Ouverture de l'EPISOL";
        ZonedDateTime eventStartDate4 = ZonedDateTime.of(LocalDateTime.of(2023, 4, 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate4 = ZonedDateTime.of(LocalDateTime.of(2024, 1, 1, 12, 0), ZoneId.of("Europe/Paris"));
        ID referrerId4 = new ID("1");
        ID localUnitId4 = new ID("1");
        int maxParticipants4 = 32;
        List<EventSession> eventSessions4 = new ArrayList<>();
        AtomicInteger sessionCounter = new AtomicInteger(0);
        for (ZonedDateTime sessionTime = eventStartDate4; sessionTime.isBefore(eventEndDate4); sessionTime = sessionTime.plusDays(7)) {
            List<EventTimeWindow> eventTimeWindowList4 = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                eventTimeWindowList4.add(new EventTimeWindow(new ID(String.valueOf(i)), sessionTime.plusMinutes(i * 30), sessionTime.plusMinutes((i + 1) * 30), maxParticipants4 / 4, new ArrayList<>()));
            }
            eventSessions4.add(new EventSession(
                    new ID(String.valueOf(sessionCounter.getAndIncrement())),
                    eventTimeWindowList4
            ));
        }
        Event event4 = new Event(eventId4, eventName4, eventDescription4, referrerId4, localUnitId4, eventSessions4, sessionCounter.get());
        events.add(event4);

        return new InMemoryEventRepository(events);
    }

    @Bean
    public VolunteerRepository volunteerRepository() {
        ArrayList<Volunteer> volunteers = new ArrayList<>();
        Volunteer volunteer1 = new Volunteer(new ID("1"), managerUser, "volunteerFirstName", "volunteerLastName", "+33 6 00 00 00 00", true, localUnit.getId());
        volunteers.add(volunteer1);
        Volunteer volunteer2 = new Volunteer(new ID("2"), southernManagerUser, "volunteerFirstNameS", "volunteerLastNameS", "+33 6 00 00 00 83", true, southernLocalUnit.getId());
        volunteers.add(volunteer2);
        return new InMemoryVolunteerRepository(volunteers);
    }
}
