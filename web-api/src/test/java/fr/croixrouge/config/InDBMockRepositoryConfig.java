package fr.croixrouge.config;

import fr.croixrouge.domain.model.*;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;
import fr.croixrouge.repository.EventRepository;
import fr.croixrouge.repository.db.event.EventDBRepository;
import fr.croixrouge.repository.db.event.EventSessionDBRepository;
import fr.croixrouge.repository.db.event.InDBEventRepository;
import fr.croixrouge.repository.db.localunit.InDBLocalUnitRepository;
import fr.croixrouge.repository.db.localunit.LocalUnitDBRepository;
import fr.croixrouge.repository.db.role.InDBRoleRepository;
import fr.croixrouge.repository.db.role.RoleDBRepository;
import fr.croixrouge.repository.db.role.RoleResourceDBRepository;
import fr.croixrouge.repository.db.user.InDBUserRepository;
import fr.croixrouge.repository.db.user.UserDBRepository;
import fr.croixrouge.repository.db.volunteer.InDBVolunteerRepository;
import fr.croixrouge.repository.db.volunteer.VolunteerDBRepository;
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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@TestConfiguration
@Profile("test-in-db")
public class InDBMockRepositoryConfig {
    private final PasswordEncoder passwordEncoder;

    private final Role managerRole;
    private final User managerUser, defaultUser;

    private final Volunteer volunteer1;
    private final Address address = new Address(Department.getDepartmentFromPostalCode("91"), "91240", "St Michel sur Orge", "76 rue des Liers");
    private final LocalUnit localUnit;

    public InDBMockRepositoryConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;

        localUnit = new LocalUnit(new ID(1L),
                "Unite Local du Val d'Orge",
                address,
                null,
                address.getPostalCode() + "-000");

        managerRole = new Role(new ID(1L),
                "Val d'Orge default role",
                "Default role for Val d'Orge",
                Map.of(Resources.RESOURCE, List.of(Operations.READ)),
                localUnit,
                List.of());

        defaultUser = new User(new ID(1L), "defaultUser", passwordEncoder.encode("defaultPassword"), List.of());

        managerUser = new User(new ID(2L), "LUManager", passwordEncoder.encode("LUPassword"), List.of(managerRole));

        volunteer1 = new Volunteer(new ID(1L), managerUser, "volunteerFirstName", "volunteerLastName", "+33 6 00 00 00 00", true, localUnit);

    }

    @Bean
    @Primary
    public InDBLocalUnitRepository localTestInDBUnitRepository(LocalUnitDBRepository localUnitDBRepository) {
        InDBLocalUnitRepository localUnitRepository = new InDBLocalUnitRepository(localUnitDBRepository);
        localUnitRepository.save(localUnit);
        return localUnitRepository;
    }

    @Bean
    @Primary
    public InDBRoleRepository roleTestRepository(RoleDBRepository roleDBRepository, RoleResourceDBRepository roleResourceDBRepository, InDBLocalUnitRepository localUnitDBRepository) {
        InDBRoleRepository inDBRoleRepository = new InDBRoleRepository(roleDBRepository, roleResourceDBRepository, localUnitDBRepository);

        inDBRoleRepository.save(managerRole);

        return inDBRoleRepository;
    }

    @Bean
    @Primary
    public InDBUserRepository userTestRepository(UserDBRepository userDBRepository, InDBRoleRepository roleDBRepository) {
        InDBUserRepository inDBUserRepository = new InDBUserRepository(userDBRepository, roleDBRepository);

        inDBUserRepository.save(defaultUser);
        inDBUserRepository.save(managerUser);

        return inDBUserRepository;
    }

    @Bean
    @Primary
    public InDBVolunteerRepository volunteerTestRepository(VolunteerDBRepository volunteerDBRepository, InDBUserRepository inDBUserRepository, InDBLocalUnitRepository inDBLocalUnitRepository) {
        var volunteerRepository = new InDBVolunteerRepository(volunteerDBRepository, inDBUserRepository, inDBLocalUnitRepository);

        ID volunteerId2 = new ID(2L);
        String firstName2 = "newVolunteer";
        String lastName2 = "newVolunteerName";
        String phoneNumber2 = "+33 6 00 11 22 33";
        boolean isValidated2 = false;
        Volunteer volunteer2 = new Volunteer(volunteerId2, defaultUser, firstName2, lastName2, phoneNumber2, isValidated2, localUnit);

        volunteerRepository.save(volunteer1);
        volunteerRepository.save(volunteer2);

        return volunteerRepository;
    }

    @Bean
    @Primary
    public EventRepository eventTestRepository(EventDBRepository eventDBRepository, EventSessionDBRepository eventSessionDBRepository, InDBUserRepository userDBRepository, InDBVolunteerRepository inDBVolunteerRepository, InDBLocalUnitRepository inDBLocalUnitRepository) {
        var eventRepository = new InDBEventRepository(eventDBRepository, eventSessionDBRepository, userDBRepository, inDBVolunteerRepository, inDBLocalUnitRepository);

        ID eventId1 = new ID(1L);
        String eventName1 = "Formation PSC1";
        String eventDescription1 = "Formation au PSC1";
        ZonedDateTime eventStartDate1 = ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate1 = ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 12, 0), ZoneId.of("Europe/Paris"));

        int maxParticipants1 = 2;
        List<ID> participants1 = new ArrayList<>();
        EventSession eventSession1 = new EventSession(null, eventStartDate1, eventEndDate1, maxParticipants1, participants1);
        Event event1 = new Event(eventId1, eventName1, eventDescription1, volunteer1, localUnit, eventStartDate1, eventEndDate1, List.of(eventSession1), 1);
        eventRepository.save(event1);

        ID eventId2 = new ID(2L);
        String eventName2 = "Distribution alimentaire";
        String eventDescription2 = "Distribution alimentaire gratuite";
        ZonedDateTime eventStartDate2 = ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate2 = ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 12, 0), ZoneId.of("Europe/Paris"));

        int maxParticipants2 = 30;
        List<ID> participants2 = new ArrayList<>();
        EventSession eventSession2 = new EventSession(null, eventStartDate2, eventEndDate2, maxParticipants2, participants2);
        Event event2 = new Event(eventId2, eventName2, eventDescription2, volunteer1, localUnit, eventStartDate2, eventEndDate2, List.of(eventSession2), 1);
        eventRepository.save(event2);

        ID eventId3 = new ID(3L);
        String eventName3 = "Formation PSC1";
        String eventDescription3 = "Formation au PSC1";
        ZonedDateTime eventStartDate3 = ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate3 = ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 12, 0), ZoneId.of("Europe/Paris"));

        int maxParticipants3 = 30;
        List<ID> participants3 = new ArrayList<>();
        EventSession eventSession3 = new EventSession(null, eventStartDate3, eventEndDate3, maxParticipants3, participants3);
        Event event3 = new Event(eventId3, eventName3, eventDescription3, volunteer1, localUnit, eventStartDate3, eventEndDate3, List.of(eventSession3), 1);
        eventRepository.save(event3);

        ID eventId4 = new ID(4L);
        String eventName4 = "EPISOL";
        String eventDescription4 = "Ouverture de l'EPISOL";
        ZonedDateTime eventStartDate4 = ZonedDateTime.of(LocalDateTime.of(2002, 1, 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate4 = ZonedDateTime.of(LocalDateTime.of(2002, 2, 1, 12, 0), ZoneId.of("Europe/Paris"));

        int maxParticipants4 = 30;
        List<EventSession> eventSessions4 = new ArrayList<>();
        AtomicInteger sessionCounter = new AtomicInteger(1);
        for (ZonedDateTime sessionTime = eventStartDate4; sessionTime.isBefore(eventEndDate4); sessionTime = sessionTime.plusDays(7)) {
            eventSessions4.add(new EventSession(
                    null,
                    sessionTime,
                    sessionTime.plusMinutes(120),
                    maxParticipants4,
                    new ArrayList<>()
            ));
        }

        Event event4 = new Event(eventId4, eventName4, eventDescription4, volunteer1, localUnit, eventStartDate4, eventEndDate4, eventSessions4, sessionCounter.get());
        eventRepository.save(event4);

        return eventRepository;
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
