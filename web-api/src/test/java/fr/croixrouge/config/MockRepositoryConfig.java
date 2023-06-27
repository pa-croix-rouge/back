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
import fr.croixrouge.storage.model.product.FoodConservation;
import fr.croixrouge.storage.model.product.FoodProduct;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.model.quantifier.VolumeQuantifier;
import fr.croixrouge.storage.model.quantifier.VolumeUnit;
import fr.croixrouge.storage.model.quantifier.WeightQuantifier;
import fr.croixrouge.storage.model.quantifier.WeightUnit;
import fr.croixrouge.storage.repository.*;
import fr.croixrouge.storage.repository.memory.*;
import fr.croixrouge.storage.service.StorageProductService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@TestConfiguration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@Profile("test-in-memory")
public class MockRepositoryConfig {

    private final PasswordEncoder passwordEncoder;
    private final User managerUser, defaultUser;

    private final Volunteer volunteer1;

    private final User southernManagerUser;

    private final Role managerRole;
    private final Address address = new Address(Department.getDepartmentFromPostalCode("91"), "91240", "St Michel sur Orge", "76 rue des Liers");

    private final Address address2 = new Address(Department.getDepartmentFromPostalCode("83"), "83000", "Toulon", "62 Boulevard de Strasbourg");
    private final LocalUnit localUnit;

    private final LocalUnit southernLocalUnit;

    public MockRepositoryConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;

        localUnit = new LocalUnit(new ID(1L),
                "Unite Local du Val d'Orge",
                address,
                null,
                address.getPostalCode() + "-000");

        managerRole = new Role(new ID(1L),
                "Val d'Orge default role",
                "Default role for Val d'Orge",
                Map.of(Resources.RESOURCE, Set.of(Operations.READ)),
                localUnit,
                List.of());


        defaultUser = new User(new ID(1L), "defaultUser", passwordEncoder.encode("defaultPassword"), localUnit, List.of());

        managerUser = new User(new ID(2L), "LUManager", passwordEncoder.encode("LUPassword"), localUnit, List.of(managerRole));

        volunteer1 = new Volunteer(new ID(1L), managerUser, "volunteerFirstName", "volunteerLastName", "+33 6 00 00 00 00", true);

        this.southernLocalUnit = new LocalUnit(new ID("2"), "Unite Local du Sud", address2, "SLUManager", address2.getPostalCode() + "-000");
        this.southernManagerUser = new User(new ID("3"), "SLUManager", passwordEncoder.encode("SLUPassword"), southernLocalUnit, List.of(managerRole));
    }

    @Bean
    @Primary
    public UserRepository userTestRepository() {
        ArrayList<User> users = new ArrayList<>();

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
        Volunteer volunteer1 = new Volunteer(volunteerId1, managerUser, firstName1, lastName1, phoneNumber1, isValidated1);

        ID volunteerId2 = new ID(2L);
        String firstName2 = "newVolunteer";
        String lastName2 = "newVolunteerName";
        String phoneNumber2 = "+33 6 00 11 22 33";
        boolean isValidated2 = false;
        Volunteer volunteer2 = new Volunteer(volunteerId2, managerUser, firstName2, lastName2, phoneNumber2, isValidated2);

        ID volunteerId3 = new ID("3");
        String firstName3 = "southernVolunteer";
        String lastName3 = "southernVolunteerName";
        String phoneNumber3 = "+33 6 83 83 83 83";
        boolean isValidated3 = true;
        Volunteer volunteer3 = new Volunteer(volunteerId3, southernManagerUser, firstName3, lastName3, phoneNumber3, isValidated3);

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
        Map<Resources, Set<Operations>> resources = Map.of(Resources.RESOURCE, Set.of(Operations.READ));

        List<ID> userIds = Collections.singletonList(new ID(2L));
        Role role = new Role(roleId, roleName, roleDescription, resources, localUnit, userIds);
        roles.add(role);

        return new InMemoryRoleRepository(roles);
    }

    @Bean
    @Primary
    public EventRepository eventTestRepository() {
        var eventRepository = new InMemoryEventRepository();

        String eventName1 = "Formation PSC1";
        String eventDescription1 = "Formation au PSC1";
        ZonedDateTime eventStartDate1 = ZonedDateTime.of(LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth() + 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate1 = ZonedDateTime.of(LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth() + 1, 12, 0), ZoneId.of("Europe/Paris"));

        int maxParticipants1 = 2;
        List<ID> participants1 = new ArrayList<>();
        EventTimeWindow eventTimeWindow1 = new EventTimeWindow(null, eventStartDate1, eventEndDate1, maxParticipants1, participants1);
        EventSession eventSession1 = new EventSession(null, List.of(eventTimeWindow1));
        Event event1 = new Event(null, eventName1, eventDescription1, volunteer1, localUnit, List.of(eventSession1), 1);
        eventRepository.save(event1);

        String eventName2 = "Distribution alimentaire";
        String eventDescription2 = "Distribution alimentaire gratuite";
        ZonedDateTime eventStartDate2 = ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate2 = ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 12, 0), ZoneId.of("Europe/Paris"));

        int maxParticipants2 = 30;
        List<ID> participants2 = new ArrayList<>();
        List<EventTimeWindow> eventTimeWindowList2 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            eventTimeWindowList2.add(new EventTimeWindow(null, eventStartDate2.plusMinutes(i * 40), eventStartDate2.plusMinutes((i + 1) * 40), maxParticipants2 / 3, participants2));
        }
        EventSession eventSession2 = new EventSession(null, eventTimeWindowList2);
        Event event2 = new Event(null, eventName2, eventDescription2, volunteer1, localUnit, List.of(eventSession2), 1);
        eventRepository.save(event2);

        String eventName3 = "Formation PSC1";
        String eventDescription3 = "Formation au PSC1";
        ZonedDateTime eventStartDate3 = ZonedDateTime.of(LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth() + 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate3 = ZonedDateTime.of(LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth() + 1, 12, 0), ZoneId.of("Europe/Paris"));

        int maxParticipants3 = 30;
        List<ID> participants3 = new ArrayList<>();
        EventTimeWindow eventTimeWindow3 = new EventTimeWindow(null, eventStartDate3, eventEndDate3, maxParticipants3, participants3);
        EventSession eventSession3 = new EventSession(null, List.of(eventTimeWindow3));
        Event event3 = new Event(null, eventName3, eventDescription3, volunteer1, localUnit, List.of(eventSession3), 1);
        eventRepository.save(event3);

        String eventName4 = "EPISOL";
        String eventDescription4 = "Ouverture de l'EPISOL";
        ZonedDateTime eventStartDate4 = ZonedDateTime.of(LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth(), 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate4 = ZonedDateTime.of(LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue() + 1, LocalDate.now().getDayOfMonth(), 12, 0), ZoneId.of("Europe/Paris"));

        int maxParticipants4 = 32;

        List<EventSession> eventSessions4 = new ArrayList<>();
        for (ZonedDateTime sessionTime = eventStartDate4; sessionTime.isBefore(eventEndDate4); sessionTime = sessionTime.plusDays(7)) {
            List<EventTimeWindow> eventTimeWindowList4 = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                eventTimeWindowList4.add(new EventTimeWindow(null, sessionTime.plusMinutes(i * 30), sessionTime.plusMinutes((i + 1) * 30), maxParticipants4 / 4, new ArrayList<>()));
            }
            eventSessions4.add(new EventSession(
                    null,
                    eventTimeWindowList4
            ));
        }
        Event event4 = new Event(null, eventName4, eventDescription4, volunteer1, localUnit, eventSessions4, eventSessions4.size());
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
    public FoodProductRepository foodProductTestRepository() {
        List<FoodProduct> products = new ArrayList<>();

        products.add(new FoodProduct(new ID("1"), new ID(3L), "FoodProduct 1",
                new WeightQuantifier(1, WeightUnit.KILOGRAM),
                null,
                FoodConservation.ROOM_TEMPERATURE,
                ZonedDateTime.of(LocalDateTime.of(2023, 5, 1, 15, 14, 1, 1), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(2023, 4, 10, 15, 14, 1, 1), ZoneId.of("Europe/Paris")),
                1));
        products.add(new FoodProduct(new ID("2"), new ID(4L), "FoodProduct 2",
                new WeightQuantifier(1, WeightUnit.KILOGRAM),
                null,
                FoodConservation.ROOM_TEMPERATURE,
                ZonedDateTime.of(LocalDateTime.of(2023, 6, 15, 12, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(2023, 6, 14, 12, 0), ZoneId.of("Europe/Paris")),
                1));

        return new InMemoryFoodProductRepository(products);
    }

    @Bean
    @Primary
    public StorageRepository storageTestRepository() {
        List<Storage> storages = new ArrayList<>();

        storages.add(new Storage(new ID(1L), "defaultStorage", localUnit, address));
        storages.add(new Storage(new ID(2L), "secondStorage", localUnit, address));

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
