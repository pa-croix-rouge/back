package fr.croixrouge.config;

import fr.croixrouge.domain.model.*;
import fr.croixrouge.domain.repository.LocalUnitRepository;
import fr.croixrouge.domain.repository.RoleRepository;
import fr.croixrouge.domain.repository.UserRepository;
import fr.croixrouge.domain.repository.VolunteerRepository;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;
import fr.croixrouge.repository.*;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.model.quantifier.VolumeQuantifier;
import fr.croixrouge.storage.model.quantifier.VolumeUnit;
import fr.croixrouge.storage.model.quantifier.WeightQuantifier;
import fr.croixrouge.storage.model.quantifier.WeightUnit;
import fr.croixrouge.storage.repository.ProductRepository;
import fr.croixrouge.storage.repository.StorageRepository;
import fr.croixrouge.storage.repository.memory.InMemoryProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryStorageRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
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
public class MockRepositoryConfig {

    private final PasswordEncoder passwordEncoder;
    private final User managerUser;
    private final Address address = new Address(Department.getDepartmentFromPostalCode("91"), "91240", "St Michel sur Orge", "76 rue des Liers");
    private final LocalUnit localUnit;

    public MockRepositoryConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        managerUser = new User(new ID("2"), "LUManager", passwordEncoder.encode("LUPassword"), List.of("ROLE_ADMIN"));

        localUnit = new LocalUnit(new ID("1"),
                "Unite Local du Val d'Orge",
                address,
                managerUser,
                address.getPostalCode() + "-000");
    }

    @Bean
    @Primary
    public UserRepository userTestRepository() {
        ArrayList<User> users = new ArrayList<>();
        ID defaultUserId = new ID("1");
        String defaultUsername = "defaultUser";
        String defaultPassword = passwordEncoder.encode("defaultPassword");
        User defaultUser = new User(defaultUserId, defaultUsername, defaultPassword, List.of());
        users.add(defaultUser);

        users.add(managerUser);

        return new InMemoryUserRepository(users);
    }

    @Bean
    @Primary
    public VolunteerRepository volunteerTestRepository() {
        ArrayList<Volunteer> volunteers = new ArrayList<>();
        ID volunteerId = new ID("1");
        String firstName = "volunteerFirstName";
        String lastName = "volunteerLastName";
        String phoneNumber = "+33 6 00 00 00 00";
        boolean isValidated = true;
        Volunteer volunteer = new Volunteer(volunteerId, managerUser, firstName, lastName, phoneNumber, isValidated, localUnit.getId());

        volunteers.add(volunteer);

        return new InMemoryVolunteerRepository(volunteers);
    }

    @Bean
    @Primary
    public LocalUnitRepository localTestUnitRepository() {
        ArrayList<LocalUnit> localUnits = new ArrayList<>();
        localUnits.add(localUnit);
        return new InMemoryLocalUnitRepository(localUnits);
    }

    @Bean
    @Primary
    public RoleRepository roleTestRepository() {
        ArrayList<Role> roles = new ArrayList<>();
        ID roleId = new ID("1");
        String roleName = "Val d'Orge default role";
        String roleDescription = "Default role for Val d'Orge";
        Map<Resources, List<Operations>> resources = Map.of(Resources.RESOURCE, List.of(Operations.READ));

        ID localUnitId = new ID("1");
        List<ID> userIds = Collections.singletonList(new ID("2"));
        Role role = new Role(roleId, roleName, roleDescription, resources, localUnitId, userIds);
        roles.add(role);

        return new InMemoryRoleRepository(roles);
    }

    @Bean
    @Primary
    public EventRepository eventTestRepository() {
        ArrayList<Event> events = new ArrayList<>();
        ID eventId1 = new ID("1");
        String eventName1 = "Formation PSC1";
        String eventDescription1 = "Formation au PSC1";
        ZonedDateTime eventStartDate1 = ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate1 = ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 12, 0), ZoneId.of("Europe/Paris"));
        ID referrerId1 = new ID("1");
        ID localUnitId1 = new ID("1");
        List<ID> participants1 = new ArrayList<>();
        EventSession eventSession1 = new EventSession(new ID("0"), eventStartDate1, eventEndDate1, participants1);
        Event event1 = new Event(eventId1, eventName1, eventDescription1, referrerId1, localUnitId1, eventStartDate1, eventEndDate1, List.of(eventSession1), 1);
        events.add(event1);

        ID eventId2 = new ID("2");
        String eventName2 = "Distribution alimentaire";
        String eventDescription2 = "Distribution alimentaire gratuite";
        ZonedDateTime eventStartDate2 = ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate2 = ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 12, 0), ZoneId.of("Europe/Paris"));
        ID referrerId2 = new ID("1");
        ID localUnitId2 = new ID("1");
        List<ID> participants2 = new ArrayList<>();
        EventSession eventSession2 = new EventSession(new ID("0"), eventStartDate2, eventEndDate2, participants2);
        Event event2 = new Event(eventId2, eventName2, eventDescription2, referrerId2, localUnitId2, eventStartDate2, eventEndDate2, List.of(eventSession2), 1);
        events.add(event2);

        ID eventId3 = new ID("3");
        String eventName3 = "Formation PSC1";
        String eventDescription3 = "Formation au PSC1";
        ZonedDateTime eventStartDate3 = ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate3 = ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 12, 0), ZoneId.of("Europe/Paris"));
        ID referrerId3 = new ID("1");
        ID localUnitId3 = new ID("1");
        List<ID> participants3 = new ArrayList<>();
        EventSession eventSession3 = new EventSession(new ID("0"), eventStartDate3, eventEndDate3, participants3);
        Event event3 = new Event(eventId3, eventName3, eventDescription3, referrerId3, localUnitId3, eventStartDate3, eventEndDate3, List.of(eventSession3), 1);
        events.add(event3);

        ID eventId4 = new ID("4");
        String eventName4 = "EPISOL";
        String eventDescription4 = "Ouverture de l'EPISOL";
        ZonedDateTime eventStartDate4 = ZonedDateTime.of(LocalDateTime.of(2002, 1, 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate4 = ZonedDateTime.of(LocalDateTime.of(2002, 2, 1, 12, 0), ZoneId.of("Europe/Paris"));
        ID referrerId4 = new ID("1");
        ID localUnitId4 = new ID("1");
        List<EventSession> eventSessions4 = new ArrayList<>();
        AtomicInteger sessionCounter = new AtomicInteger(0);
        for (ZonedDateTime sessionTime = eventStartDate4; sessionTime.isBefore(eventEndDate4); sessionTime = sessionTime.plusDays(7)) {
            eventSessions4.add(new EventSession(
                    new ID(String.valueOf(sessionCounter.getAndIncrement())),
                    sessionTime,
                    sessionTime.plusMinutes(120),
                    new ArrayList<>()
            ));
        }
        Event event4 = new Event(eventId4, eventName4, eventDescription4, referrerId4, localUnitId4, eventStartDate4, eventEndDate4, eventSessions4, sessionCounter.get());
        events.add(event4);

        return new InMemoryEventRepository(events);
    }

    @Bean
    @Primary
    public ProductRepository productTestRepository() {
        List<Product> products = new ArrayList<>();

        products.add(new Product(new ID("1"), "Product 1", new WeightQuantifier(1, WeightUnit.KILOGRAM), null));
        products.add(new Product(new ID("2"), "Product 2", new VolumeQuantifier(1, VolumeUnit.LITER), null));

        return new InMemoryProductRepository(products);
    }

    @Bean
    @Primary
    public StorageRepository storageTestRepository() {
        List<Storage> storages = new ArrayList<>();

        storages.add(new Storage(new ID("1"), localUnit, address));
        storages.add(new Storage(new ID("2"), localUnit, address));

        return new InMemoryStorageRepository(storages);
    }

}
