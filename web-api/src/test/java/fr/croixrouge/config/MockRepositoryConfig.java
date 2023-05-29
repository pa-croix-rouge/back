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
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.model.quantifier.VolumeQuantifier;
import fr.croixrouge.storage.model.quantifier.VolumeUnit;
import fr.croixrouge.storage.model.quantifier.WeightQuantifier;
import fr.croixrouge.storage.model.quantifier.WeightUnit;
import fr.croixrouge.storage.repository.ProductRepository;
import fr.croixrouge.storage.repository.StorageProductRepository;
import fr.croixrouge.storage.repository.StorageRepository;
import fr.croixrouge.storage.repository.UserProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryStorageProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryStorageRepository;
import fr.croixrouge.storage.repository.memory.InMemoryUserProductRepository;
import fr.croixrouge.storage.service.StorageProductService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@TestConfiguration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@Profile("test-in-memory")
public class MockRepositoryConfig {

    private final PasswordEncoder passwordEncoder;
    private final User managerUser;

    private final User southernManagerUser;
    private final Address address = new Address(Department.getDepartmentFromPostalCode("91"), "91240", "St Michel sur Orge", "76 rue des Liers");

    private final Address address2 = new Address(Department.getDepartmentFromPostalCode("83"), "83000", "Toulon", "62 Boulevard de Strasbourg");
    private final LocalUnit localUnit;

    private final LocalUnit southernLocalUnit;

    public MockRepositoryConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        managerUser = new User(new ID(2L), "LUManager", passwordEncoder.encode("LUPassword"), List.of("ROLE_ADMIN"));

        localUnit = new LocalUnit(new ID(1L),
                "Unite Local du Val d'Orge",
                address,
                managerUser,
                address.getPostalCode() + "-000");
        this.southernManagerUser = new User(new ID("3"), "SLUManager", passwordEncoder.encode("SLUPassword"), List.of("ROLE_ADMIN"));
        this.southernLocalUnit = new LocalUnit(new ID("2"), "Unite Local du Sud", address2, southernManagerUser, address2.getPostalCode() + "-000");
    }

    @Bean
    @Primary
    public UserRepository userTestRepository() {
        ArrayList<User> users = new ArrayList<>();
        ID defaultUserId = new ID(1L);
        String defaultUsername = "defaultUser";
        String defaultPassword = passwordEncoder.encode("defaultPassword");
        User defaultUser = new User(defaultUserId, defaultUsername, defaultPassword, List.of());
        users.add(defaultUser);

        users.add(managerUser);
        users.add(southernManagerUser);

        return new InMemoryUserRepository(users);
    }

    @Bean
    @Primary
    public VolunteerRepository volunteerTestRepository() {
        ArrayList<Volunteer> volunteers = new ArrayList<>();

        ID volunteerId1 = new ID(1L);
        String firstName1 = "volunteerFirstName";
        String lastName1 = "volunteerLastName";
        String phoneNumber1 = "+33 6 00 00 00 00";
        boolean isValidated1 = true;
        Volunteer volunteer1 = new Volunteer(volunteerId1, managerUser, firstName1, lastName1, phoneNumber1, isValidated1, localUnit);

        ID volunteerId2 = new ID(2L);
        String firstName2 = "newVolunteer";
        String lastName2 = "newVolunteerName";
        String phoneNumber2 = "+33 6 00 11 22 33";
        boolean isValidated2 = false;
        Volunteer volunteer2 = new Volunteer(volunteerId2, managerUser, firstName2, lastName2, phoneNumber2, isValidated2, localUnit);

        ID volunteerId3 = new ID("3");
        String firstName3 = "southernVolunteer";
        String lastName3 = "southernVolunteerName";
        String phoneNumber3 = "+33 6 83 83 83 83";
        boolean isValidated3 = true;
        Volunteer volunteer3 = new Volunteer(volunteerId3, southernManagerUser, firstName3, lastName3, phoneNumber3, isValidated3, southernLocalUnit.getId());

        volunteers.add(volunteer1);
        volunteers.add(volunteer2);
        volunteers.add(volunteer3);

        return new InMemoryVolunteerRepository(volunteers);
    }

    @Bean
    @Primary
    public LocalUnitRepository localTestUnitRepository() {
        ArrayList<LocalUnit> localUnits = new ArrayList<>();
        localUnits.add(localUnit);
        localUnits.add(southernLocalUnit);
        return new InMemoryLocalUnitRepository(localUnits);
    }

    @Bean
    @Primary
    public RoleRepository roleTestRepository() {
        ArrayList<Role> roles = new ArrayList<>();
        ID roleId = new ID(1L);
        String roleName = "Val d'Orge default role";
        String roleDescription = "Default role for Val d'Orge";
        Map<Resources, List<Operations>> resources = Map.of(Resources.RESOURCE, List.of(Operations.READ));

        ID localUnitId = new ID(1L);
        List<ID> userIds = Collections.singletonList(new ID(2L));
        Role role = new Role(roleId, roleName, roleDescription, resources, localUnitId, userIds);
        roles.add(role);

        return new InMemoryRoleRepository(roles);
    }

    @Bean
    @Primary
    public EventRepository eventTestRepository() {
        ArrayList<Event> events = new ArrayList<>();
        ID eventId1 = new ID(1L);
        String eventName1 = "Formation PSC1";
        String eventDescription1 = "Formation au PSC1";
        ZonedDateTime eventStartDate1 = ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate1 = ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 12, 0), ZoneId.of("Europe/Paris"));
        ID referrerId1 = new ID(1L);
        ID localUnitId1 = new ID(1L);
        int maxParticipants1 = 2;
        List<ID> participants1 = new ArrayList<>();
        EventTimeWindow eventTimeWindow1 = new EventTimeWindow(new ID(0L), eventStartDate1, eventEndDate1, maxParticipants1, participants1);
        EventSession eventSession1 = new EventSession(new ID(0L), List.of(eventTimeWindow1));
        Event event1 = new Event(eventId1, eventName1, eventDescription1, referrerId1, localUnitId1, List.of(eventSession1), 1);
        events.add(event1);

        ID eventId2 = new ID(2L);
        String eventName2 = "Distribution alimentaire";
        String eventDescription2 = "Distribution alimentaire gratuite";
        ZonedDateTime eventStartDate2 = ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate2 = ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 12, 0), ZoneId.of("Europe/Paris"));
        ID referrerId2 = new ID(1L);
        ID localUnitId2 = new ID(1L);
        int maxParticipants2 = 30;
        List<ID> participants2 = new ArrayList<>();
        List<EventTimeWindow> eventTimeWindowList2 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            eventTimeWindowList2.add(new EventTimeWindow(new ID(i), eventStartDate2.plusMinutes(i * 40), eventStartDate2.plusMinutes((i + 1) * 40), maxParticipants2 / 3, participants2));
        }
        EventSession eventSession2 = new EventSession(new ID(0L), eventTimeWindowList2);
        Event event2 = new Event(eventId2, eventName2, eventDescription2, referrerId2, localUnitId2, List.of(eventSession2), 1);
        events.add(event2);

        ID eventId3 = new ID(3L);
        String eventName3 = "Formation PSC1";
        String eventDescription3 = "Formation au PSC1";
        ZonedDateTime eventStartDate3 = ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate3 = ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 12, 0), ZoneId.of("Europe/Paris"));
        ID referrerId3 = new ID(1L);
        ID localUnitId3 = new ID(1L);
        int maxParticipants3 = 30;
        List<ID> participants3 = new ArrayList<>();
        EventTimeWindow eventTimeWindow3 = new EventTimeWindow(new ID(0L), eventStartDate3, eventEndDate3, maxParticipants3, participants3);
        EventSession eventSession3 = new EventSession(new ID(0L), List.of(eventTimeWindow3));
        Event event3 = new Event(eventId3, eventName3, eventDescription3, referrerId3, localUnitId3, List.of(eventSession3), 1);
        events.add(event3);

        ID eventId4 = new ID(4L);
        String eventName4 = "EPISOL";
        String eventDescription4 = "Ouverture de l'EPISOL";
        ZonedDateTime eventStartDate4 = ZonedDateTime.of(LocalDateTime.of(2002, 1, 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate4 = ZonedDateTime.of(LocalDateTime.of(2002, 2, 1, 12, 0), ZoneId.of("Europe/Paris"));
        ID referrerId4 = new ID(1L);
        ID localUnitId4 = new ID(1L);
        int maxParticipants4 = 32;
        List<EventSession> eventSessions4 = new ArrayList<>();
        AtomicInteger sessionCounter = new AtomicInteger(0);
        for (ZonedDateTime sessionTime = eventStartDate4; sessionTime.isBefore(eventEndDate4); sessionTime = sessionTime.plusDays(7)) {
            List<EventTimeWindow> eventTimeWindowList4 = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                eventTimeWindowList4.add(new EventTimeWindow(new ID(String.valueOf(i)), sessionTime.plusMinutes(i * 30), sessionTime.plusMinutes((i + 1) * 30), maxParticipants4 / 4, new ArrayList<>()));
            }
            eventSessions4.add(new EventSession(
                    new ID(sessionCounter.getAndIncrement()),
                    eventTimeWindowList4
            ));
        }
        Event event4 = new Event(eventId4, eventName4, eventDescription4, referrerId4, localUnitId4, eventSessions4, sessionCounter.get());
        events.add(event4);

        return new InMemoryEventRepository(events);
    }

    @Bean
    @Primary
    public ProductRepository productTestRepository() {
        List<Product> products = new ArrayList<>();

        products.add(new Product(new ID(1L), "Product 1", new WeightQuantifier(1, WeightUnit.KILOGRAM), null));
        products.add(new Product(new ID(2L), "Product 2", new VolumeQuantifier(1, VolumeUnit.LITER), null));

        return new InMemoryProductRepository(products);
    }

    @Bean
    @Primary
    public StorageRepository storageTestRepository() {
        List<Storage> storages = new ArrayList<>();

        storages.add(new Storage(new ID(1L), localUnit, address));
        storages.add(new Storage(new ID(2L), localUnit, address));

        return new InMemoryStorageRepository(storages);
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
}
