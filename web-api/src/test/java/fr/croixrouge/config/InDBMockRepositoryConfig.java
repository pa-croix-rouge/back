package fr.croixrouge.config;

import fr.croixrouge.domain.model.*;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;
import fr.croixrouge.model.EventTimeWindow;
import fr.croixrouge.repository.EventRepository;
import fr.croixrouge.repository.db.event.EventDBRepository;
import fr.croixrouge.repository.db.event.EventSessionDBRepository;
import fr.croixrouge.repository.db.event.EventTimeWindowDBRepository;
import fr.croixrouge.repository.db.event.InDBEventRepository;
import fr.croixrouge.repository.db.localunit.InDBLocalUnitRepository;
import fr.croixrouge.repository.db.localunit.LocalUnitDBRepository;
import fr.croixrouge.repository.db.product.*;
import fr.croixrouge.repository.db.product_limit.InDBProductLimitRepository;
import fr.croixrouge.repository.db.product_limit.ProductLimitDBRepository;
import fr.croixrouge.repository.db.role.InDBRoleRepository;
import fr.croixrouge.repository.db.role.RoleDBRepository;
import fr.croixrouge.repository.db.role.RoleResourceDBRepository;
import fr.croixrouge.repository.db.storage.InDBStorageRepository;
import fr.croixrouge.repository.db.storage.StorageDBRepository;
import fr.croixrouge.repository.db.storage_product.InDBStorageProductRepository;
import fr.croixrouge.repository.db.storage_product.StorageProductDBRepository;
import fr.croixrouge.repository.db.user.InDBUserRepository;
import fr.croixrouge.repository.db.user.UserDBRepository;
import fr.croixrouge.repository.db.user_product.InDBUserProductRepository;
import fr.croixrouge.repository.db.user_product.UserProductDBRepository;
import fr.croixrouge.repository.db.volunteer.InDBVolunteerRepository;
import fr.croixrouge.repository.db.volunteer.VolunteerDBRepository;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.*;
import fr.croixrouge.storage.model.quantifier.*;
import fr.croixrouge.storage.repository.StorageProductRepository;
import fr.croixrouge.storage.repository.UserProductRepository;
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
    private final User managerUser, defaultUser, southernManagerUser;

    private final Volunteer volunteer1, southernVolunteer1;
    private final Address address = new Address(Department.getDepartmentFromPostalCode("91"), "91240", "St Michel sur Orge", "76 rue des Liers");

    private final Address address2 = new Address(Department.getDepartmentFromPostalCode("83"), "83000", "Toulon", "62 Boulevard de Strasbourg");
    private final LocalUnit localUnit, southernLocalUnit;

    private final Product product1, product2, cloth1, cloth2, cloth3, cloth4, cloth5, food1, food2;

    public InDBMockRepositoryConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;

        localUnit = new LocalUnit(new ID(1L),
                "Unite Local du Val d'Orge",
                address,
                "LUManager",
                address.getPostalCode() + "-000");

        managerRole = new Role(new ID(1L),
                "Val d'Orge default role",
                "Default role for Val d'Orge",
                Map.of(Resources.RESOURCE, List.of(Operations.READ)),
                localUnit,
                List.of());

        defaultUser = new User(new ID(1L), "defaultUser", passwordEncoder.encode("defaultPassword"), localUnit, List.of());

        managerUser = new User(new ID(2L), "LUManager", passwordEncoder.encode("LUPassword"), localUnit, List.of(managerRole));

        volunteer1 = new Volunteer(new ID(1L), managerUser, "volunteerFirstName", "volunteerLastName", "+33 6 00 00 00 00", true);

        southernLocalUnit = new LocalUnit(new ID("2"), "Unite Local du Sud", address2, "SLUManager", address2.getPostalCode() + "-000");

        southernManagerUser = new User(new ID("3"), "SLUManager", passwordEncoder.encode("SLUPassword"), southernLocalUnit, List.of(managerRole));

        southernVolunteer1 = new Volunteer(null, southernManagerUser, "southernVolunteer", "southernVolunteerName", "+33 6 83 83 83 83", true);

        product1 = new Product(new ID(1L), "Product 1", new WeightQuantifier(1, WeightUnit.KILOGRAM), null);
        product2 = new Product(new ID(2L), "Product 2", new VolumeQuantifier(1, VolumeUnit.LITER), null);
        cloth1 = new Product(new ID(3L), "Chemises blanches", new Quantifier(20, NumberedUnit.NUMBER), ProductLimit.NO_LIMIT);
        cloth2 = new Product(new ID(4L), "Chemises blanches", new Quantifier(20, NumberedUnit.NUMBER), ProductLimit.NO_LIMIT);
        cloth3 = new Product(new ID(5L), "Chemises blanches", new Quantifier(20, NumberedUnit.NUMBER), ProductLimit.NO_LIMIT);
        cloth4 = new Product(new ID(6L), "Chemises blanches", new Quantifier(20, NumberedUnit.NUMBER), ProductLimit.NO_LIMIT);
        cloth5 = new Product(new ID(7L), "Chemises blanches", new Quantifier(20, NumberedUnit.NUMBER), ProductLimit.NO_LIMIT);
        food1 = new Product(new ID(8L), "Pommes", new WeightQuantifier(1, WeightUnit.KILOGRAM), ProductLimit.NO_LIMIT);
        food2 = new Product(new ID(9L), "Pates", new WeightQuantifier(1, WeightUnit.KILOGRAM), ProductLimit.NO_LIMIT);
    }

    @Bean
    @Primary
    public InDBLocalUnitRepository localTestInDBUnitRepository(LocalUnitDBRepository localUnitDBRepository) {
        InDBLocalUnitRepository localUnitRepository = new InDBLocalUnitRepository(localUnitDBRepository);
        localUnitRepository.save(localUnit);
        localUnitRepository.save(southernLocalUnit);
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
    public InDBUserRepository userTestRepository(UserDBRepository userDBRepository, InDBRoleRepository roleDBRepository, InDBLocalUnitRepository localUnitDBRepository) {
        InDBUserRepository inDBUserRepository = new InDBUserRepository(userDBRepository, roleDBRepository, localUnitDBRepository);

        inDBUserRepository.save(defaultUser);
        inDBUserRepository.save(managerUser);
        inDBUserRepository.save(southernManagerUser);

        return inDBUserRepository;
    }

    @Bean
    @Primary
    public InDBVolunteerRepository volunteerTestRepository(VolunteerDBRepository volunteerDBRepository, UserDBRepository userDBRepository, InDBUserRepository inDBUserRepository) {
        var volunteerRepository = new InDBVolunteerRepository(volunteerDBRepository, userDBRepository, inDBUserRepository);

        ID volunteerId2 = new ID(2L);
        String firstName2 = "newVolunteer";
        String lastName2 = "newVolunteerName";
        String phoneNumber2 = "+33 6 00 11 22 33";
        boolean isValidated2 = false;
        Volunteer volunteer2 = new Volunteer(volunteerId2, defaultUser, firstName2, lastName2, phoneNumber2, isValidated2);

        volunteerRepository.save(volunteer1);
        volunteerRepository.save(volunteer2);
        volunteerRepository.save(southernVolunteer1);

        return volunteerRepository;
    }

    @Bean
    @Primary
    public EventRepository eventTestRepository(EventDBRepository eventDBRepository, EventSessionDBRepository eventSessionDBRepository, EventTimeWindowDBRepository eventTimeWindowDBRepository, InDBUserRepository userDBRepository, InDBVolunteerRepository inDBVolunteerRepository, InDBLocalUnitRepository inDBLocalUnitRepository) {
        var eventRepository = new InDBEventRepository(eventDBRepository, eventSessionDBRepository, eventTimeWindowDBRepository, userDBRepository, inDBVolunteerRepository, inDBLocalUnitRepository);

        String eventName1 = "Formation PSC1";
        String eventDescription1 = "Formation au PSC1";
        ZonedDateTime eventStartDate1 = ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate1 = ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 12, 0), ZoneId.of("Europe/Paris"));

        int maxParticipants1 = 2;
        List<ID> participants1 = new ArrayList<>();
        EventTimeWindow eventTimeWindow1 = new EventTimeWindow(null, eventStartDate1, eventEndDate1, maxParticipants1, participants1);
        EventSession eventSession1 = new EventSession(null, List.of(eventTimeWindow1));
        Event event1 = new Event(null, eventName1, eventDescription1, volunteer1, localUnit, List.of(eventSession1), 1);
        eventRepository.save(event1);

        String eventName2 = "Distribution alimentaire";
        String eventDescription2 = "Distribution alimentaire gratuite";
        ZonedDateTime eventStartDate2 = ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 10, 0), ZoneId.of("Europe/Paris"));

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
        ZonedDateTime eventStartDate3 = ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate3 = ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 12, 0), ZoneId.of("Europe/Paris"));

        int maxParticipants3 = 30;
        List<ID> participants3 = new ArrayList<>();
        EventTimeWindow eventTimeWindow3 = new EventTimeWindow(null, eventStartDate3, eventEndDate3, maxParticipants3, participants3);
        EventSession eventSession3 = new EventSession(null, List.of(eventTimeWindow3));
        Event event3 = new Event(null, eventName3, eventDescription3, volunteer1, localUnit, List.of(eventSession3), 1);
        eventRepository.save(event3);

        String eventName4 = "EPISOL";
        String eventDescription4 = "Ouverture de l'EPISOL";
        ZonedDateTime eventStartDate4 = ZonedDateTime.of(LocalDateTime.of(2002, 1, 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate4 = ZonedDateTime.of(LocalDateTime.of(2002, 2, 1, 12, 0), ZoneId.of("Europe/Paris"));

        int maxParticipants4 = 32;
        List<EventSession> eventSessions4 = new ArrayList<>();
        AtomicInteger sessionCounter = new AtomicInteger(0);
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
        Event event4 = new Event(null, eventName4, eventDescription4, volunteer1, localUnit, eventSessions4, sessionCounter.get());
        eventRepository.save(event4);

        return eventRepository;
    }

    @Bean
    @Primary
    public InDBProductLimitRepository productLimitTestRepository(ProductLimitDBRepository productLimitDBRepository) {
        return new InDBProductLimitRepository(productLimitDBRepository);
    }

    @Bean
    @Primary
    public InDBProductRepository productTestRepository(ProductDBRepository productDBRepository) {
        var storageRepository = new InDBProductRepository(productDBRepository);

        storageRepository.save(product1);
        storageRepository.save(product2);
        storageRepository.save(cloth1);
        storageRepository.save(cloth2);
        storageRepository.save(cloth3);
        storageRepository.save(cloth4);
        storageRepository.save(cloth5);
        storageRepository.save(food1);
        storageRepository.save(food2);

        return storageRepository;
    }

    @Bean
    @Primary
    public InDBClothProductRepository clothProductRepository(ClothProductDBRepository clothProductDBRepository, InDBProductRepository productRepository) {
        InDBClothProductRepository repository = new InDBClothProductRepository(clothProductDBRepository, productRepository);

        repository.save(new ClothProduct(new ID(1L), cloth1, ClothSize.S));
        repository.save(new ClothProduct(new ID(2L), cloth2, ClothSize.M));
        repository.save(new ClothProduct(new ID(3L), cloth3, ClothSize.L));
        repository.save(new ClothProduct(new ID(4L), cloth4, ClothSize.XL));
        repository.save(new ClothProduct(new ID(5L), cloth5, ClothSize.XXL));

        return repository;
    }

    @Bean
    @Primary
    public InDBFoodProductRepository foodProductTestRepository(FoodProductDBRepository foodProductDBRepository, InDBProductRepository productRepository) {
        var repository = new InDBFoodProductRepository(foodProductDBRepository, productRepository);

        repository.save(new FoodProduct(new ID(1L),
                food1,
                FoodConservation.ROOM_TEMPERATURE,
                LocalDateTime.of(2023, 5, 1, 15, 14, 1),
                LocalDateTime.of(2023, 4, 10, 15, 14, 1),
                1));

        repository.save(new FoodProduct(new ID(2L),
                food2,
                FoodConservation.ROOM_TEMPERATURE,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now(),
                1));


        return repository;
    }

    @Bean
    @Primary
    public InDBStorageRepository storageTestRepository(StorageDBRepository storageDBRepository, InDBLocalUnitRepository inDBLocalUnitRepository) {
        var storageRepository = new InDBStorageRepository(storageDBRepository, inDBLocalUnitRepository);

        storageRepository.save(new Storage(new ID(1L), "defaultStorage", localUnit, address));
        storageRepository.save(new Storage(new ID(2L), "secondStorage", localUnit, address));

        return storageRepository;
    }

    @Bean
    public UserProductRepository storageUserProductRepository(UserProductDBRepository userProductDBRepository, InDBUserRepository userRepository, InDBProductRepository productRepository, InDBStorageRepository storageRepository) {
        return new InDBUserProductRepository(userProductDBRepository, userRepository, productRepository, storageRepository);
    }

    @Bean
    public StorageProductRepository storageProductRepository(StorageProductDBRepository storageProductDBRepository, InDBProductRepository productRepository, InDBStorageRepository storageRepository) {
        StorageProductRepository storageProductRepository = new InDBStorageProductRepository(storageProductDBRepository, productRepository, storageRepository);

        storageProductRepository.save(new StorageProduct(new ID(1L), storageRepository.findById(new ID(1L)).get(), productRepository.findById(new ID(1L)).get(), 10));
        storageProductRepository.save(new StorageProduct(new ID(2L), storageRepository.findById(new ID(1L)).get(), productRepository.findById(new ID(2L)).get(), 10));

        storageProductRepository.save(new StorageProduct(new ID(3L), storageRepository.findById(new ID(1L)).get(), productRepository.findById(new ID(3L)).get(), 10));
        storageProductRepository.save(new StorageProduct(new ID(4L), storageRepository.findById(new ID(1L)).get(), productRepository.findById(new ID(4L)).get(), 10));
        storageProductRepository.save(new StorageProduct(new ID(5L), storageRepository.findById(new ID(1L)).get(), productRepository.findById(new ID(5L)).get(), 10));
        storageProductRepository.save(new StorageProduct(new ID(6L), storageRepository.findById(new ID(1L)).get(), productRepository.findById(new ID(6L)).get(), 10));
        storageProductRepository.save(new StorageProduct(new ID(7L), storageRepository.findById(new ID(1L)).get(), productRepository.findById(new ID(7L)).get(), 10));

        return new InDBStorageProductRepository(storageProductDBRepository, productRepository, storageRepository);
    }

    @Bean
    public StorageProductService storageProductServiceCore(StorageProductRepository storageProductRepository) {
        return new StorageProductService(storageProductRepository);
    }
}
