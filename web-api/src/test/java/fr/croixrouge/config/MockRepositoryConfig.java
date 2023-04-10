package fr.croixrouge.config;

import fr.croixrouge.domain.model.*;
import fr.croixrouge.domain.repository.LocalUnitRepository;
import fr.croixrouge.domain.repository.RoleRepository;
import fr.croixrouge.domain.repository.UserRepository;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;
import fr.croixrouge.repository.*;
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
import java.util.concurrent.ConcurrentHashMap;

@TestConfiguration
public class MockRepositoryConfig {

    private final PasswordEncoder passwordEncoder;

    public MockRepositoryConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    @Primary
    public UserRepository userTestRepository() {
        ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
        String defaultUserId = "1";
        String defaultUsername = "defaultUser";
        String defaultPassword = passwordEncoder.encode("defaultPassword");
        User defaultUser = new User(defaultUserId, defaultUsername, defaultPassword, List.of());
        users.put(defaultUserId, defaultUser);

        String localUnitManagerUserId = "2";
        String localUnitManagerUsername = "LUManager";
        String localUnitManagerPassword = passwordEncoder.encode("LUPassword");
        User localUnitManager = new User(localUnitManagerUserId, localUnitManagerUsername, localUnitManagerPassword, List.of("ROLE_ADMIN"));
        users.put(localUnitManagerUserId, localUnitManager);

        return new InMemoryUserRepository(users);
    }

    @Bean
    @Primary
    public LocalUnitRepository localTestUnitRepository() {
        ConcurrentHashMap<String, LocalUnit> localUnits = new ConcurrentHashMap<>();
        String localUnitId = "1";
        Department department = Department.getDepartmentFromPostalCode("91");
        String postalCode = "91240";
        String city = "St Michel sur Orge";
        String streetNumberAndName = "76 rue des Liers";
        Address address = new Address(department, postalCode, city, streetNumberAndName);
        String name = "Unite Local du Val d'Orge";
        String managerId = "2";
        LocalUnit localUnit = new LocalUnit(localUnitId, name, address, managerId);
        localUnits.put(localUnitId, localUnit);
        return new InMemoryLocalUnitRepository(localUnits);
    }

    @Bean
    @Primary
    public RoleRepository roleTestRepository() {
        ConcurrentHashMap<String, Role> roles = new ConcurrentHashMap<>();
        String roleId = "1";
        String roleName = "Val d'Orge default role";
        String roleDescription = "Default role for Val d'Orge";
        Map<Resources, List<Operations>> resources = Map.of(Resources.RESOURCE, List.of(Operations.READ));

        String localUnitId = "1";
        List<String> userIds = Collections.singletonList("2");
        Role role = new Role(roleId, roleName, roleDescription, resources, localUnitId, userIds);
        roles.put(roleId, role);

        return new InMemoryRoleRepository(roles);
    }

    @Bean
    @Primary
    public EventRepository eventTestRepository() {
        ConcurrentHashMap<String, Event> events = new ConcurrentHashMap<>();
        String eventId1 = "1";
        String eventName1 = "Formation PSC1";
        String eventDescription1 = "Formation au PSC1";
        ZonedDateTime eventStartDate1 = ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate1 = ZonedDateTime.of(LocalDateTime.of(2000, 6, 1, 12, 0), ZoneId.of("Europe/Paris"));
        String referrerId1 = "1";
        String localUnitId1 = "1";
        List<String> participants1 = new ArrayList<>();
        EventSession eventSession1 = new EventSession("0", eventStartDate1, eventEndDate1, participants1);
        Event event1 = new Event(eventId1, eventName1, eventDescription1, referrerId1, localUnitId1, eventStartDate1, eventEndDate1, List.of(eventSession1), 1);
        events.put(eventId1, event1);

        String eventId2 = "2";
        String eventName2 = "Distribution alimentaire";
        String eventDescription2 = "Distribution alimentaire gratuite";
        ZonedDateTime eventStartDate2 = ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate2 = ZonedDateTime.of(LocalDateTime.of(2000, 6, 2, 12, 0), ZoneId.of("Europe/Paris"));
        String referrerId2 = "1";
        String localUnitId2 = "1";
        List<String> participants2 = new ArrayList<>();
        EventSession eventSession2 = new EventSession("0", eventStartDate2, eventEndDate2, participants2);
        Event event2 = new Event(eventId2, eventName2, eventDescription2, referrerId2, localUnitId2, eventStartDate2, eventEndDate2, List.of(eventSession2), 1);
        events.put(eventId2, event2);

        String eventId3 = "3";
        String eventName3 = "Formation PSC1";
        String eventDescription3 = "Formation au PSC1";
        ZonedDateTime eventStartDate3 = ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 10, 0), ZoneId.of("Europe/Paris"));
        ZonedDateTime eventEndDate3 = ZonedDateTime.of(LocalDateTime.of(2000, 7, 1, 12, 0), ZoneId.of("Europe/Paris"));
        String referrerId3 = "1";
        String localUnitId3 = "1";
        List<String> participants3 = new ArrayList<>();
        EventSession eventSession3 = new EventSession("0", eventStartDate3, eventEndDate3, participants3);
        Event event3 = new Event(eventId3, eventName3, eventDescription3, referrerId3, localUnitId3, eventStartDate3, eventEndDate3, List.of(eventSession3), 1);
        events.put(eventId3, event3);

        return new InMemoryEventRepository(events);
    }
}
