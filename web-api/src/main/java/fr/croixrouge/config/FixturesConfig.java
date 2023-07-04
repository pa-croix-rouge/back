package fr.croixrouge.config;

import fr.croixrouge.domain.model.*;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;
import fr.croixrouge.model.EventTimeWindow;
import fr.croixrouge.repository.EventRepository;
import fr.croixrouge.repository.db.beneficiary.BeneficiaryDBRepository;
import fr.croixrouge.repository.db.beneficiary.FamilyMemberDBRepository;
import fr.croixrouge.repository.db.beneficiary.InDBBeneficiaryRepository;
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
import fr.croixrouge.repository.db.user_product.InDBBeneficiaryProductRepository;
import fr.croixrouge.repository.db.user_product.UserProductDBRepository;
import fr.croixrouge.repository.db.volunteer.InDBVolunteerRepository;
import fr.croixrouge.repository.db.volunteer.VolunteerDBRepository;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.*;
import fr.croixrouge.storage.model.quantifier.*;
import fr.croixrouge.storage.repository.BeneficiaryProductRepository;
import fr.croixrouge.storage.repository.StorageProductRepository;
import fr.croixrouge.storage.service.StorageProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Configuration
@Profile("fixtures-prod")
public class FixturesConfig {
    private final Role managerRoleValOrge, defaultRoleValOrge, beneficiaryRoleValOrge;
    private final User managerValOrge, volunteerUserValOrge1, volunteerUserValOrge2, volunteerUserValOrge3, volunteerUserValOrge4, volunteerUserValOrge5, volunteerUserValOrge6, volunteerUserValOrge7, volunteerUserValOrge8, volunteerUserValOrge9, volunteerUserValOrge10, beneficiaryUserValOrge1, beneficiaryUserValOrge2, beneficiaryUserValOrge3, beneficiaryUserValOrge4, beneficiaryUserValOrge5, beneficiaryUserValOrge6, beneficiaryUserValOrge7, beneficiaryUserValOrge8, beneficiaryUserValOrge9, beneficiaryUserValOrge10, beneficiaryUserValOrge11, beneficiaryUserValOrge12, beneficiaryUserValOrge13, beneficiaryUserValOrge14, beneficiaryUserValOrge15, beneficiaryUserValOrge16, beneficiaryUserValOrge17, beneficiaryUserValOrge18, beneficiaryUserValOrge19, beneficiaryUserValOrge20, beneficiaryUserValOrge21, beneficiaryUserValOrge22, beneficiaryUserValOrge23, beneficiaryUserValOrge24, beneficiaryUserValOrge25, beneficiaryUserValOrge26, beneficiaryUserValOrge27, beneficiaryUserValOrge28, beneficiaryUserValOrge29, beneficiaryUserValOrge30, beneficiaryUserValOrge31, beneficiaryUserValOrge32, beneficiaryUserValOrge33, beneficiaryUserValOrge34, beneficiaryUserValOrge35, beneficiaryUserValOrge36, beneficiaryUserValOrge37, beneficiaryUserValOrge38, beneficiaryUserValOrge39, beneficiaryUserValOrge40, beneficiaryUserValOrge41, beneficiaryUserValOrge42, beneficiaryUserValOrge43, beneficiaryUserValOrge44, beneficiaryUserValOrge45, beneficiaryUserValOrge46, beneficiaryUserValOrge47;

    private final Volunteer volunteerManagerValOrge, volunteerValOrge1, volunteerValOrge2, volunteerValOrge3, volunteerValOrge4, volunteerValOrge5, volunteerValOrge6, volunteerValOrge7, volunteerValOrge8, volunteerValOrge9, volunteerValOrge10;
    private final Beneficiary beneficiary1, beneficiary2, beneficiary3, beneficiary4, beneficiary5, beneficiary6, beneficiary7, beneficiary8, beneficiary9, beneficiary10, beneficiary11, beneficiary12, beneficiary13, beneficiary14, beneficiary15, beneficiary16, beneficiary17, beneficiary18, beneficiary19, beneficiary20, beneficiary21, beneficiary22, beneficiary23, beneficiary24, beneficiary25, beneficiary26, beneficiary27, beneficiary28, beneficiary29, beneficiary30, beneficiary31, beneficiary32, beneficiary33, beneficiary34, beneficiary35, beneficiary36, beneficiary37, beneficiary38, beneficiary39, beneficiary40, beneficiary41, beneficiary42, beneficiary43, beneficiary44, beneficiary45, beneficiary46, beneficiary47;
    private final Address address = new Address(Department.getDepartmentFromPostalCode("91"), "91240", "St Michel sur Orge", "76 rue des Liers");

    private final LocalUnit localUnit;

    private final Product cloth1, cloth2, cloth3, cloth4, cloth5, cloth6, cloth7, cloth8, cloth9, cloth10, cloth11, cloth12, cloth13, cloth14, cloth15, food1, food2, food3, food4, food5, food6, food7, food8, food9, food10, food11, food12, food13, food14, food15;

    public FixturesConfig(PasswordEncoder passwordEncoder) {
        localUnit = new LocalUnit(new ID(1L),
                "Unité Local du Val d'Orge",
                address,
                "bernard.lhuillier@croix-rouge.fr",
                address.getPostalCode() + "-181");


        HashMap<Resources, Set<Operations>> roleResources = new HashMap<>();
        for (var ressource : Resources.values()) {
            roleResources.put(ressource, Set.of(Operations.values()));
        }

        managerRoleValOrge = new Role(null,
                "Val d'Orge gestionnaire",
                "Role pour les gestionnaires du Val d'Orge",
                roleResources,
                localUnit,
                List.of());
        HashMap<Resources, Set<Operations>> defaultRoleResources = new HashMap<>(roleResources);
        defaultRoleResources.remove(Resources.RESOURCE);

        defaultRoleValOrge = new Role(null,
                "Val d'Orge volontaires",
                "Role par défaut pour les volontaires du Val d'Orge",
                defaultRoleResources,
                localUnit,
                List.of());

        beneficiaryRoleValOrge = new Role(null,
                "Val d'Orge bénéficiaires",
                "Role par défaut pour les bénéficiaires du Val d'Orge",
                defaultRoleResources,
                localUnit,
                List.of());

        managerValOrge = new User(null, "bernard.lhuillier@croix-rouge.fr", passwordEncoder.encode("Password.123"), localUnit, List.of(managerRoleValOrge));

        volunteerUserValOrge1 = new User(null, "valerie.leroux@croix-rouge.fr", passwordEncoder.encode("Password.123"), localUnit, List.of(defaultRoleValOrge));
        volunteerUserValOrge2 = new User(null, "jerome.piat@croix-rouge.fr", passwordEncoder.encode("Password.123"), localUnit, List.of(defaultRoleValOrge));
        volunteerUserValOrge3 = new User(null, "elodie.lechervy@croix-rouge.fr", passwordEncoder.encode("Password.123"), localUnit, List.of(defaultRoleValOrge));
        volunteerUserValOrge4 = new User(null, "hugo.jean@croix-rouge.fr", passwordEncoder.encode("Password.123"), localUnit, List.of(defaultRoleValOrge));
        volunteerUserValOrge5 = new User(null, "sebastien.joubert@croix-rouge.fr", passwordEncoder.encode("Password.123"), localUnit, List.of(defaultRoleValOrge));
        volunteerUserValOrge6 = new User(null, "thomas.georget@croix-rouge.fr", passwordEncoder.encode("Password.123"), localUnit, List.of(defaultRoleValOrge));
        volunteerUserValOrge7 = new User(null, "gael.germain@croix-rouge.fr", passwordEncoder.encode("Password.123"), localUnit, List.of(defaultRoleValOrge));
        volunteerUserValOrge8 = new User(null, "charles.lefeuvre@croix-rouge.fr", passwordEncoder.encode("Password.123"), localUnit, List.of(defaultRoleValOrge));
        volunteerUserValOrge9 = new User(null, "emilie.lassalas@croix-rouge.fr", passwordEncoder.encode("Password.123"), localUnit, List.of(defaultRoleValOrge));
        volunteerUserValOrge10 = new User(null, "anne.ozanne@croix-rouge.fr", passwordEncoder.encode("Password.123"), localUnit, List.of(defaultRoleValOrge));

        volunteerManagerValOrge = new Volunteer(null, managerValOrge, "Bernard", "L’HUILLIER", "+33 6 52 87 37 55", true);
        volunteerValOrge1 = new Volunteer(null, volunteerUserValOrge1, "Valerie", "LEROUX", "+33 6 78 45 23 10", true);
        volunteerValOrge2 = new Volunteer(null, volunteerUserValOrge2, "Jerome", "PIAT", "+33 6 31 92 48 76", true);
        volunteerValOrge3 = new Volunteer(null, volunteerUserValOrge3, "Elodie", "LEVERCHY", "+33 7 14 57 83 29", true);
        volunteerValOrge4 = new Volunteer(null, volunteerUserValOrge4, "Hugo", "JEAN", "+33 6 89 24 50 13", true);
        volunteerValOrge5 = new Volunteer(null, volunteerUserValOrge5, "Sebastien", "JOUBERT", "+33 6 72 36 18 94", true);
        volunteerValOrge6 = new Volunteer(null, volunteerUserValOrge6, "Thomas", "GEORGET", "+33 6 50 78 41 63", true);
        volunteerValOrge7 = new Volunteer(null, volunteerUserValOrge7, "Gael", "GERMAIN", "+33 7 29 84 15 70", true);
        volunteerValOrge8 = new Volunteer(null, volunteerUserValOrge8, "Charles", "LEFEUVRE", "+33 7 45 32 79 68", true);
        volunteerValOrge9 = new Volunteer(null, volunteerUserValOrge9, "Emilie", "LASSALAS", "+33 6 51 67 93 27", true);
        volunteerValOrge10 = new Volunteer(null, volunteerUserValOrge10, "Anne", "OZANNE", "+33 7 36 80 72 55", true);

        beneficiaryUserValOrge1 = new User(null, "dubois.elise92@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge2 = new User(null, "m.martin-explorer@outlook.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge3 = new User(null, "lea.lambert91@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge4 = new User(null, "jerome.girard.aventure@orange.fr", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge5 = new User(null, "camille.roux@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge6 = new User(null, "alexandre.bernard@outlook.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge7 = new User(null, "margot.dupont@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge8 = new User(null, "antoine.lefebvre@orange.fr", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge9 = new User(null, "juliette.moreau@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge10 = new User(null, "theo.thomas@outlook.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge11 = new User(null, "fernando.gomez92@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge12 = new User(null, "ana.martinez-explorer@outlook.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge13 = new User(null, "jose.sanchez91@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge14 = new User(null, "maria.hernandez.aventure@orange.fr", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge15 = new User(null, "juan.rodriguez@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge16 = new User(null, "carmen.gonzalez@outlook.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge17 = new User(null, "manuel.fernandez@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge18 = new User(null, "isabel.lopez@orange.fr", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge19 = new User(null, "francisco.torres@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge20 = new User(null, "rosa.garcia@outlook.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge21 = new User(null, "ali.hassan92@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge22 = new User(null, "amina.mahmoud-explorer@outlook.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge23 = new User(null, "omar.farouk91@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge24 = new User(null, "sarah.abdelrahman@orange.fr", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge25 = new User(null, "yusuf.alamoudi@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge26 = new User(null, "huda.fawzy@outlook.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge27 = new User(null, "kareem.elshamy@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge28 = new User(null, "asmaa.abdelfattah@orange.fr", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge29 = new User(null, "mostafa.kamal@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge30 = new User(null, "samar.ahmed@outlook.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge31 = new User(null, "khaled.sayed@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge32 = new User(null, "heba.elmohandes@outlook.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge33 = new User(null, "mohammed.ali@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge34 = new User(null, "fatma.hosny@outlook.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge35 = new User(null, "tamer.hosny@outlook.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge36 = new User(null, "jan.kowalski92@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge37 = new User(null, "anna.nowak-explorer@outlook.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge38 = new User(null, "piotr.wisniewski@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge39 = new User(null, "ewa.dabrowska@orange.fr", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge40 = new User(null, "tomasz.zielinski@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge41 = new User(null, "amara.ndiaye@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge42 = new User(null, "nneka.osei@outlook.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge43 = new User(null, "kwame.mensah@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge44 = new User(null, "aminata.diallo@orange.fr", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge45 = new User(null, "tendai.mutasa@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge46 = new User(null, "zola.ndlovu@outlook.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));
        beneficiaryUserValOrge47 = new User(null, "sibusiso.khumalo@gmail.com", passwordEncoder.encode("Password.123"), localUnit, List.of(beneficiaryRoleValOrge));

        beneficiary1 = new Beneficiary(null, beneficiaryUserValOrge1, "Eloise", "DEBOIS", "+33 6 72 51 39 84", true, LocalDate.of(2000, 6, 1), "2 00 06 01 2A 122 019", List.of());
        beneficiary2 = new Beneficiary(null, beneficiaryUserValOrge2, "Mathieu", "MARTIN", "+33 6 28 93 75 46", true, LocalDate.of(1992, 3, 3), "1 92 03 03 75 113 557", List.of());
        beneficiary3 = new Beneficiary(null, beneficiaryUserValOrge3, "Léa", "LAMBERT", "+33 7 57 84 21 36", true, LocalDate.of(1988, 7, 19), "2 88 07 19 91 190 349", List.of());
        beneficiary4 = new Beneficiary(null, beneficiaryUserValOrge4, "Jerome", "GIRARD", "+33 7 63 18 47 92", true, LocalDate.of(1983, 11, 7), "1 83 11 07 91 125 789", List.of());
        beneficiary5 = new Beneficiary(null, beneficiaryUserValOrge5, "Camille", "ROUX", "+33 6 41 75 69 23", true, LocalDate.of(1990, 5, 28), "2 90 05 28 93 102 901", List.of());
        beneficiary6 = new Beneficiary(null, beneficiaryUserValOrge6, "Alexandre", "BERNARD", "+33 7 89 36 54 17", true, LocalDate.of(1987, 1, 15), "1 87 01 15 92 105 302", List.of());
        beneficiary7 = new Beneficiary(null, beneficiaryUserValOrge7, "Margaux", "DUPONT", "+33 6 73 95 28 41", true, LocalDate.of(1994, 8, 9), "2 94 08 09 75 120 270", List.of());
        beneficiary8 = new Beneficiary(null, beneficiaryUserValOrge8, "Antoine", "LEFEBVRE", "+33 6 48 21 67 93", true, LocalDate.of(1991, 6, 25), "1 91 06 25 91 157 633", List.of());
        beneficiary9 = new Beneficiary(null, beneficiaryUserValOrge9, "Juliette", "MOREAU", "+33 7 37 84 52 19", true, LocalDate.of(1989, 12, 17), "2 89 12 17 91 143 751", List.of());
        beneficiary10 = new Beneficiary(null, beneficiaryUserValOrge10, "Théo", "THOMAS", "+33 7 92 57 13 48", true, LocalDate.of(1993, 2, 6), "1 93 02 06 91 109 669", List.of());
        beneficiary11 = new Beneficiary(null, beneficiaryUserValOrge11, "Fernando", "GOMEZ", "+34 6 72 51 39 84", true, LocalDate.of(1980, 6, 1), "1 80 06 01 99 999 019", List.of());
        beneficiary12 = new Beneficiary(null, beneficiaryUserValOrge12, "Ana", "MARTINEZ", "+34 6 28 93 75 46", true, LocalDate.of(1985, 3, 3), "2 85 03 03 99 999 557", List.of());
        beneficiary13 = new Beneficiary(null, beneficiaryUserValOrge13, "José", "SANCHEZ", "+34 7 57 84 21 36", true, LocalDate.of(1988, 7, 19), "1 88 07 19 99 999 349", List.of());
        beneficiary14 = new Beneficiary(null, beneficiaryUserValOrge14, "María", "HERNANDEZ", "+34 7 63 18 47 92", true, LocalDate.of(1978, 11, 7), "2 78 11 07 99 999 789", List.of());
        beneficiary15 = new Beneficiary(null, beneficiaryUserValOrge15, "Juan", "RODRIGUEZ", "+34 6 41 75 69 23", true, LocalDate.of(1990, 5, 28), "1 90 05 28 99 999 901", List.of());
        beneficiary16 = new Beneficiary(null, beneficiaryUserValOrge16, "Carmen", "GONZALEZ", "+34 7 89 36 54 17", true, LocalDate.of(1987, 1, 15), "2 87 01 15 99 999 302", List.of());
        beneficiary17 = new Beneficiary(null, beneficiaryUserValOrge17, "Manuel", "FERNANDEZ", "+34 6 73 95 28 41", true, LocalDate.of(1994, 8, 9), "1 94 08 09 99 999 270", List.of());
        beneficiary18 = new Beneficiary(null, beneficiaryUserValOrge18, "Isabel", "LOPEZ", "+34 6 48 21 67 93", true, LocalDate.of(1991, 6, 25), "2 91 06 25 99 999 633", List.of());
        beneficiary19 = new Beneficiary(null, beneficiaryUserValOrge19, "Francisco", "TORRES", "+34 7 37 84 52 19", true, LocalDate.of(1989, 12, 17), "1 89 12 17 99 999 751", List.of());
        beneficiary20 = new Beneficiary(null, beneficiaryUserValOrge20, "Rosa", "GARCIA", "+34 7 92 57 13 48", true, LocalDate.of(1993, 2, 6), "2 93 02 06 99 999 669", List.of());
        beneficiary21 = new Beneficiary(null, beneficiaryUserValOrge21, "Ali", "HASSAN", "+212 6 72 51 39 84", true, LocalDate.of(1980, 6, 1), "1 80 06 01 99 999 019", List.of());
        beneficiary22 = new Beneficiary(null, beneficiaryUserValOrge22, "Amina", "MAHMOUD", "+20 6 28 93 75 46", true, LocalDate.of(1985, 3, 3), "2 85 03 03 99 999 557", List.of());
        beneficiary23 = new Beneficiary(null, beneficiaryUserValOrge23, "Omar", "FAROUK", "+971 7 57 84 21 36", true, LocalDate.of(1988, 7, 19), "1 88 07 19 99 999 349", List.of());
        beneficiary24 = new Beneficiary(null, beneficiaryUserValOrge24, "Sarah", "ABDELRAHMAN", "+962 7 63 18 47 92", true, LocalDate.of(1978, 11, 7), "2 78 11 07 99 999 789", List.of());
        beneficiary25 = new Beneficiary(null, beneficiaryUserValOrge25, "Yusuf", "ALAMOUDI", "+966 6 41 75 69 23", true, LocalDate.of(1990, 5, 28), "1 90 05 28 99 999 901", List.of());
        beneficiary26 = new Beneficiary(null, beneficiaryUserValOrge26, "Huda", "FAWZY", "+20 7 89 36 54 17", true, LocalDate.of(1987, 1, 15), "2 87 01 15 99 999 302", List.of());
        beneficiary27 = new Beneficiary(null, beneficiaryUserValOrge27, "Kareem", "ELSHAMY", "+213 6 73 95 28 41", true, LocalDate.of(1994, 8, 9), "1 94 08 09 99 999 270", List.of());
        beneficiary28 = new Beneficiary(null, beneficiaryUserValOrge28, "Asmaa", "ABDELFATTAH", "+20 6 48 21 67 93", true, LocalDate.of(1991, 6, 25), "2 91 06 25 99 999 633", List.of());
        beneficiary29 = new Beneficiary(null, beneficiaryUserValOrge29, "Mostafa", "KAMAL", "+20 7 37 84 52 19", true, LocalDate.of(1989, 12, 17), "1 89 12 17 99 999 751", List.of());
        beneficiary30 = new Beneficiary(null, beneficiaryUserValOrge30, "Samar", "AHMED", "+20 7 92 57 13 48", true, LocalDate.of(1993, 2, 6), "2 93 02 06 99 999 669", List.of());
        beneficiary31 = new Beneficiary(null, beneficiaryUserValOrge31, "Khaled", "SAYED", "+20 7 92 57 13 48", true, LocalDate.of(1980, 4, 25), "1 80 04 25 99 999 019", List.of());
        beneficiary32 = new Beneficiary(null, beneficiaryUserValOrge32, "Heba", "ELMOHANDES", "+20 6 28 93 75 46", true, LocalDate.of(1975, 7, 17), "2 75 07 17 99 999 557", List.of());
        beneficiary33 = new Beneficiary(null, beneficiaryUserValOrge33, "Mohammed", "ALI", "+20 7 57 84 21 36", true, LocalDate.of(1980, 1, 1), "1 80 01 01 99 999 349", List.of());
        beneficiary34 = new Beneficiary(null, beneficiaryUserValOrge34, "Fatma", "HOSNY", "+20 7 63 18 47 92", true, LocalDate.of(1978, 5, 15), "2 78 05 15 99 999 789", List.of());
        beneficiary35 = new Beneficiary(null, beneficiaryUserValOrge35, "Tamer", "HOSNY", "+20 6 41 75 69 23", true, LocalDate.of(1985, 6, 10), "1 85 06 10 99 999 901", List.of());
        beneficiary36 = new Beneficiary(null, beneficiaryUserValOrge36, "Jan", "KOWALSKI", "+48 672 513 984", true, LocalDate.of(1982, 1, 13), "1 82 01 13 2A 122 019", List.of());
        beneficiary37 = new Beneficiary(null, beneficiaryUserValOrge37, "Anna", "NOWAK", "+48 628 937 546", true, LocalDate.of(1987, 7, 5), "2 87 07 05 75 113 557", List.of());
        beneficiary38 = new Beneficiary(null, beneficiaryUserValOrge38, "Piotr", "WISNIEWSKI", "+48 757 842 136", true, LocalDate.of(1978, 9, 19), "1 78 09 19 91 190 349", List.of());
        beneficiary39 = new Beneficiary(null, beneficiaryUserValOrge39, "Ewa", "DĄBROWSKA", "+48 763 184 792", true, LocalDate.of(1980, 3, 7), "2 80 03 07 91 125 789", List.of());
        beneficiary40 = new Beneficiary(null, beneficiaryUserValOrge40, "Tomasz", "ZIELINSKI", "+48 417 569 233", true, LocalDate.of(1985, 5, 28), "1 85 05 28 93 102 901", List.of());
        beneficiary41 = new Beneficiary(null, beneficiaryUserValOrge41, "Amara", "Ndiaye", "+221 672 513 984", true, LocalDate.of(1982, 2, 13), "1 82 02 13 2A 122 019", List.of());
        beneficiary42 = new Beneficiary(null, beneficiaryUserValOrge42, "Nneka", "Osei", "+233 628 937 546", true, LocalDate.of(1987, 7, 5), "2 87 07 05 75 113 557", List.of());
        beneficiary43 = new Beneficiary(null, beneficiaryUserValOrge43, "Kwame", "Mensah", "+233 757 842 136", true, LocalDate.of(1978, 10, 19), "1 78 10 19 91 190 349", List.of());
        beneficiary44 = new Beneficiary(null, beneficiaryUserValOrge44, "Aminata", "Diallo", "+221 763 184 792", true, LocalDate.of(1980, 6, 7), "2 80 06 07 91 125 789", List.of());
        beneficiary45 = new Beneficiary(null, beneficiaryUserValOrge45, "Tendai", "Mutasa", "+263 417 569 233", true, LocalDate.of(1985, 8, 28), "1 85 08 28 93 102 901", List.of());
        beneficiary46 = new Beneficiary(null, beneficiaryUserValOrge46, "Zola", "Ndlovu", "+263 753 628 841", true, LocalDate.of(1994, 5, 25), "2 94 05 25 91 157 633", List.of());
        beneficiary47 = new Beneficiary(null, beneficiaryUserValOrge47, "Sibusiso", "Khumalo", "+263 437 845 219", true, LocalDate.of(1989, 12, 15), "1 89 12 15 91 143 751", List.of());

        cloth1 = new Product(null, "Chemises blanches", new Quantifier(20, NumberedUnit.NUMBER), null);
        cloth2 = new Product(null, "Chemises blanches", new Quantifier(15, NumberedUnit.NUMBER), null);
        cloth3 = new Product(null, "Chemises blanches", new Quantifier(17, NumberedUnit.NUMBER), null);
        cloth4 = new Product(null, "Chemises blanches", new Quantifier(6, NumberedUnit.NUMBER), null);
        cloth5 = new Product(null, "Chemises blanches", new Quantifier(5, NumberedUnit.NUMBER), null);
        cloth6 = new Product(null, "T-shirts blancs", new Quantifier(15, NumberedUnit.NUMBER), null);
        cloth7 = new Product(null, "Pantalons noirs", new Quantifier(10, NumberedUnit.NUMBER), null);
        cloth8 = new Product(null, "Robes rouges", new Quantifier(8, NumberedUnit.NUMBER), null);
        cloth9 = new Product(null, "Pulls gris", new Quantifier(12, NumberedUnit.NUMBER), null);
        cloth10 = new Product(null, "Jupes bleues", new Quantifier(20, NumberedUnit.NUMBER), null);
        cloth11 = new Product(null, "Chaussures noires", new Quantifier(5, NumberedUnit.NUMBER), null);
        cloth12 = new Product(null, "Chaussures blanches", new Quantifier(8, NumberedUnit.NUMBER), null);
        cloth13 = new Product(null, "Chaussettes noires", new Quantifier(10, NumberedUnit.NUMBER), null);
        cloth14 = new Product(null, "Chaussettes blanches", new Quantifier(15, NumberedUnit.NUMBER), null);
        cloth15 = new Product(null, "Baskets rouges", new Quantifier(7, NumberedUnit.NUMBER), null);
        food1 = new Product(null, "Pommes", new WeightQuantifier(1, WeightUnit.KILOGRAM), null);
        food2 = new Product(null, "Pates", new WeightQuantifier(1, WeightUnit.KILOGRAM), null);
        food3 = new Product(null, "Bananes", new WeightQuantifier(500, WeightUnit.GRAM), null);
        food4 = new Product(null, "Oranges", new WeightQuantifier(750, WeightUnit.GRAM), null);
        food5 = new Product(null, "Tomates", new WeightQuantifier(1, WeightUnit.KILOGRAM), null);
        food6 = new Product(null, "Carottes", new WeightQuantifier(1, WeightUnit.KILOGRAM), null);
        food7 = new Product(null, "Pommes de terre", new WeightQuantifier(2, WeightUnit.KILOGRAM), null);
        food8 = new Product(null, "Fraises", new WeightQuantifier(250, WeightUnit.GRAM), null);
        food9 = new Product(null, "Blancs de poulet", new WeightQuantifier(500, WeightUnit.GRAM), null);
        food10 = new Product(null, "Filets de saumon", new WeightQuantifier(1, WeightUnit.KILOGRAM), null);
        food11 = new Product(null, "Épinards", new WeightQuantifier(200, WeightUnit.GRAM), null);
        food12 = new Product(null, "Yaourt", new WeightQuantifier(500, WeightUnit.GRAM), null);
        food13 = new Product(null, "Pain", new WeightQuantifier(800, WeightUnit.GRAM), null);
        food14 = new Product(null, "Viande hachée", new WeightQuantifier(1, WeightUnit.KILOGRAM), null);
        food15 = new Product(null, "Oignons", new WeightQuantifier(750, WeightUnit.GRAM), null);
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
    CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

    @Bean
    @Primary
    public InDBRoleRepository roleTestRepository(RoleDBRepository roleDBRepository, RoleResourceDBRepository roleResourceDBRepository, InDBLocalUnitRepository localUnitDBRepository) {
        InDBRoleRepository inDBRoleRepository = new InDBRoleRepository(roleDBRepository, roleResourceDBRepository, localUnitDBRepository);

        inDBRoleRepository.save(managerRoleValOrge);
        inDBRoleRepository.save(defaultRoleValOrge);
        inDBRoleRepository.save(beneficiaryRoleValOrge);

        return inDBRoleRepository;
    }

    @Bean
    @Primary
    public InDBUserRepository userTestRepository(UserDBRepository userDBRepository, InDBRoleRepository roleDBRepository, InDBLocalUnitRepository localUnitDBRepository) {
        return new InDBUserRepository(userDBRepository, roleDBRepository, localUnitDBRepository);
    }

    @Bean
    @Primary
    public InDBVolunteerRepository volunteerTestRepository(VolunteerDBRepository volunteerDBRepository, UserDBRepository userDBRepository, InDBUserRepository inDBUserRepository) {
        var volunteerRepository = new InDBVolunteerRepository(volunteerDBRepository, userDBRepository, inDBUserRepository);

        for (Volunteer volunteer : Arrays.asList(volunteerManagerValOrge, volunteerValOrge1, volunteerValOrge2, volunteerValOrge3, volunteerValOrge4, volunteerValOrge5, volunteerValOrge6, volunteerValOrge7, volunteerValOrge8, volunteerValOrge9, volunteerValOrge10)) {
            volunteerRepository.save(volunteer);
        }

        return volunteerRepository;
    }

    @Bean
    public InDBBeneficiaryRepository beneficiaryRepository(BeneficiaryDBRepository beneficiaryDBRepository, FamilyMemberDBRepository familyMemberDBRepository, UserDBRepository userDBRepository, InDBUserRepository inDBUserRepository) {
        var beneficiaryRepository = new InDBBeneficiaryRepository(beneficiaryDBRepository, familyMemberDBRepository, userDBRepository, inDBUserRepository);

        for (Beneficiary beneficiary : Arrays.asList(beneficiary1, beneficiary2, beneficiary3, beneficiary4, beneficiary5, beneficiary6, beneficiary7, beneficiary8, beneficiary9, beneficiary10, beneficiary11, beneficiary12, beneficiary13, beneficiary14, beneficiary15, beneficiary16, beneficiary17, beneficiary18, beneficiary19, beneficiary20, beneficiary21, beneficiary22, beneficiary23, beneficiary24, beneficiary25, beneficiary26, beneficiary27, beneficiary28, beneficiary29, beneficiary30, beneficiary31, beneficiary32, beneficiary33, beneficiary34, beneficiary35, beneficiary36, beneficiary37, beneficiary38, beneficiary39, beneficiary40, beneficiary41, beneficiary42, beneficiary43, beneficiary44, beneficiary45, beneficiary46, beneficiary47)) {
            beneficiaryRepository.save(beneficiary);
        }

        return beneficiaryRepository;
    }

    @Bean
    @Primary
    public EventRepository eventTestRepository(EventDBRepository eventDBRepository, EventSessionDBRepository eventSessionDBRepository, EventTimeWindowDBRepository eventTimeWindowDBRepository, InDBUserRepository userDBRepository, InDBVolunteerRepository inDBVolunteerRepository, InDBLocalUnitRepository inDBLocalUnitRepository) {
        var eventRepository = new InDBEventRepository(eventDBRepository, eventSessionDBRepository, eventTimeWindowDBRepository, userDBRepository, inDBVolunteerRepository, inDBLocalUnitRepository);
        List<User> userBeneficiariesInDB = userDBRepository.findAll().stream().filter(user -> !user.getUsername().contains("@croix-rouge.fr")).toList();

        ZonedDateTime eventStart1 = ZonedDateTime.of(LocalDateTime.of(2023, 3, 4, 10, 0), ZoneId.of("Europe/Paris"));
        List<EventSession> eventSessions1 = new ArrayList<>();
        for (; eventStart1.isBefore(ZonedDateTime.of(LocalDateTime.of(2023, 9, 1, 0, 0), ZoneId.of("Europe/Paris"))); eventStart1 = eventStart1.plusDays(14)) {
            eventSessions1.add(new EventSession(null, List.of(new EventTimeWindow(null, eventStart1, eventStart1.plusHours(8), 12, new ArrayList<>()))));
        }
        Event event1 = new Event(null, "Formation PSC1", "Acquérir les compétences essentielles pour intervenir en cas d'urgence et sauver des vies ! Formation PSC1, une session interactive dispensée par des formateurs certifiés. Apprendre les gestes de premiers secours tels que la réanimation, la position latérale de sécurité et le traitement des hémorragies. Places limitées à 12 personnes. Les stagiaires obtiennent une attestation officielle reconnue nationalement.", volunteerValOrge2, localUnit, eventSessions1, eventSessions1.size());
        eventRepository.save(event1);

        ZonedDateTime eventStartDate2 = ZonedDateTime.of(LocalDateTime.of(2023, 3, 5, 14, 0), ZoneId.of("Europe/Paris"));
        List<EventSession> eventSessions2 = new ArrayList<>();
        for (; eventStartDate2.isBefore(ZonedDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0), ZoneId.of("Europe/Paris"))); eventStartDate2 = eventStartDate2.plusDays(7)) {
            List<EventTimeWindow> eventTimeWindowList2 = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                List<ID> participants = new ArrayList<>();
                int numberOfParticipants = new Random().nextInt(4);
                for (int j = 0; j < numberOfParticipants; j++) {
                    ID randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
                    while (participants.contains(randomBeneficiaryId) || eventTimeWindowList2.stream().map(EventTimeWindow::getParticipants).flatMap(Collection::stream).toList().contains(randomBeneficiaryId)) {
                        randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
                    }
                    participants.add(randomBeneficiaryId);
                }
                eventTimeWindowList2.add(new EventTimeWindow(null, eventStartDate2.plusMinutes(i * 20), eventStartDate2.plusMinutes((i + 1) * 20), 4, participants));
            }
            eventSessions2.add(new EventSession(null, eventTimeWindowList2));
        }
        Event event2 = new Event(null, "Ouverture du vestiaire", "Chaque semaine, nous organisons une distribution de vêtements aux personnes dans le besoin. Distribution sur rendez-vous.", volunteerValOrge7, localUnit, eventSessions2, eventSessions2.size());
        eventRepository.save(event2);

        ZonedDateTime eventStartDate3 = ZonedDateTime.of(LocalDateTime.of(2023, 3, 6, 16, 0), ZoneId.of("Europe/Paris"));
        List<EventSession> eventSessions3 = new ArrayList<>();
        for (; eventStartDate3.isBefore(ZonedDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0), ZoneId.of("Europe/Paris"))); eventStartDate3 = eventStartDate3.plusDays(7)) {
            List<EventTimeWindow> eventTimeWindowList3 = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                List<ID> participants = new ArrayList<>();
                int numberOfParticipants = new Random().nextInt(6);
                for (int j = 0; j < numberOfParticipants; j++) {
                    ID randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
                    while (participants.contains(randomBeneficiaryId) || eventTimeWindowList3.stream().map(EventTimeWindow::getParticipants).flatMap(Collection::stream).toList().contains(randomBeneficiaryId)) {
                        randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
                    }
                    participants.add(randomBeneficiaryId);
                }
                eventTimeWindowList3.add(new EventTimeWindow(null, eventStartDate3.plusMinutes(i * 30), eventStartDate3.plusMinutes((i + 1) * 30), 6, participants));
            }
            eventSessions3.add(new EventSession(null, eventTimeWindowList3));
        }
        Event event3 = new Event(null, "EPISOL Lundi", "Chaque semaine, nous ouvrons l'épicerie sociale aux personnes dans le besoin. Distribution sur rendez-vous.", volunteerValOrge1, localUnit, eventSessions3, eventSessions3.size());
        eventRepository.save(event3);

        ZonedDateTime eventStartDate4 = ZonedDateTime.of(LocalDateTime.of(2023, 3, 8, 16, 0), ZoneId.of("Europe/Paris"));
        List<EventSession> eventSessions4 = new ArrayList<>();
        for (; eventStartDate4.isBefore(ZonedDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0), ZoneId.of("Europe/Paris"))); eventStartDate4 = eventStartDate4.plusDays(7)) {
            List<EventTimeWindow> eventTimeWindowList4 = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                List<ID> participants = new ArrayList<>();
                int numberOfParticipants = new Random().nextInt(6);
                for (int j = 0; j < numberOfParticipants; j++) {
                    ID randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
                    while (participants.contains(randomBeneficiaryId) || eventTimeWindowList4.stream().map(EventTimeWindow::getParticipants).flatMap(Collection::stream).toList().contains(randomBeneficiaryId)) {
                        randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
                    }
                    participants.add(randomBeneficiaryId);
                }
                eventTimeWindowList4.add(new EventTimeWindow(null, eventStartDate4.plusMinutes(i * 30), eventStartDate4.plusMinutes((i + 1) * 30), 6, participants));
            }
            eventSessions4.add(new EventSession(null, eventTimeWindowList4));
        }
        Event event4 = new Event(null, "EPISOL Mercredi", "Chaque semaine, nous ouvrons l'épicerie sociale aux personnes dans le besoin. Distribution sur rendez-vous.", volunteerValOrge1, localUnit, eventSessions4, eventSessions4.size());
        eventRepository.save(event4);

        ZonedDateTime eventStartDate5 = ZonedDateTime.of(LocalDateTime.of(2023, 3, 10, 16, 0), ZoneId.of("Europe/Paris"));
        List<EventSession> eventSessions5 = new ArrayList<>();
        for (; eventStartDate5.isBefore(ZonedDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0), ZoneId.of("Europe/Paris"))); eventStartDate5 = eventStartDate5.plusDays(7)) {
            List<EventTimeWindow> eventTimeWindowList5 = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                List<ID> participants = new ArrayList<>();
                int numberOfParticipants = new Random().nextInt(6);
                for (int j = 0; j < numberOfParticipants; j++) {
                    ID randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
                    while (participants.contains(randomBeneficiaryId) || eventTimeWindowList5.stream().map(EventTimeWindow::getParticipants).flatMap(Collection::stream).toList().contains(randomBeneficiaryId)) {
                        randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
                    }
                    participants.add(randomBeneficiaryId);
                }
                eventTimeWindowList5.add(new EventTimeWindow(null, eventStartDate5.plusMinutes(i * 30), eventStartDate5.plusMinutes((i + 1) * 30), 6, participants));
            }
            eventSessions5.add(new EventSession(null, eventTimeWindowList5));
        }
        Event event5 = new Event(null, "EPISOL Vendredi", "Chaque semaine, nous ouvrons l'épicerie sociale aux personnes dans le besoin. Distribution sur rendez-vous.", volunteerValOrge1, localUnit, eventSessions5, eventSessions5.size());
        eventRepository.save(event5);

        ZonedDateTime eventStartDate6 = ZonedDateTime.of(LocalDateTime.of(2023, 3, 9, 10, 0), ZoneId.of("Europe/Paris"));
        List<EventSession> eventSessions6 = new ArrayList<>();
        for (; eventStartDate6.isBefore(ZonedDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0), ZoneId.of("Europe/Paris"))); eventStartDate6 = eventStartDate6.plusDays(7)) {
            List<EventTimeWindow> eventTimeWindowList6 = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                List<ID> participants = new ArrayList<>();
                int numberOfParticipants = new Random().nextInt(5);
                for (int j = 0; j < numberOfParticipants; j++) {
                    ID randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
                    while (participants.contains(randomBeneficiaryId) || eventTimeWindowList6.stream().map(EventTimeWindow::getParticipants).flatMap(Collection::stream).toList().contains(randomBeneficiaryId)) {
                        randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
                    }
                    participants.add(randomBeneficiaryId);
                }
                eventTimeWindowList6.add(new EventTimeWindow(null, eventStartDate6.plusMinutes(i * 30), eventStartDate6.plusMinutes((i + 1) * 30), 5, participants));
            }
            eventSessions6.add(new EventSession(null, eventTimeWindowList6));
        }
        Event event6 = new Event(null, "Distribution alimentaire", "Chaque semaine, nous distribuons gratuitement de la nourriture aux personnes dans le besoin. Distribution sur rendez-vous.", volunteerValOrge1, localUnit, eventSessions6, eventSessions6.size());
        eventRepository.save(event6);

        List<ID> participants = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            ID randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
            while (participants.contains(randomBeneficiaryId)) {
                randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
            }
            participants.add(randomBeneficiaryId);
        }

        eventRepository.save(new Event(null,
                "Distribution de jouets",
                "Ponctuellement, nous distribuons gratuitement des jouets aux personnes dans le besoin. Distribution sur rendez-vous.",
                volunteerValOrge5,
                localUnit,
                List.of(new EventSession(null,
                        List.of(new EventTimeWindow(null,
                                    ZonedDateTime.of(LocalDateTime.of(2023, 7, 11, 17, 0), ZoneId.of("Europe/Paris")),
                                    ZonedDateTime.of(LocalDateTime.of(2023, 7, 11, 19, 0), ZoneId.of("Europe/Paris")),
                                10,
                                participants)))),
                1));

        return eventRepository;
    }

    @Bean
    @Primary
    public InDBProductLimitRepository productLimitTestRepository(ProductLimitDBRepository productLimitDBRepository) {
        return new InDBProductLimitRepository(productLimitDBRepository);
    }

    @Bean
    @Primary
    public InDBProductRepository productTestRepository(ProductDBRepository productDBRepository, InDBProductLimitRepository inDBProductLimitRepository) {
        return new InDBProductRepository(productDBRepository, inDBProductLimitRepository);
    }

    @Bean
    @Primary
    public InDBClothProductRepository clothProductRepository(ClothProductDBRepository clothProductDBRepository, InDBProductRepository productRepository) {
        InDBClothProductRepository repository = new InDBClothProductRepository(clothProductDBRepository, productRepository);

        repository.save(new ClothProduct(new ID(1L), cloth1, ClothSize.S, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(2L), cloth2, ClothSize.M, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(3L), cloth3, ClothSize.L, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(4L), cloth4, ClothSize.XL, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(5L), cloth5, ClothSize.XXL, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(6L), cloth6, ClothSize.S, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(7L), cloth7, ClothSize.M, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(8L), cloth8, ClothSize.L, ClothGender.FEMALE));
        repository.save(new ClothProduct(new ID(9L), cloth9, ClothSize.CHILD, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(10L), cloth10, ClothSize.XXL, ClothGender.FEMALE));
        repository.save(new ClothProduct(new ID(11L), cloth11, ClothSize.UNKNOWN, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(12L), cloth12, ClothSize.UNKNOWN, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(13L), cloth13, ClothSize.CHILD, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(14L), cloth14, ClothSize.XL, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(15L), cloth15, ClothSize.UNKNOWN, ClothGender.NOT_SPECIFIED));

        return repository;
    }

    @Bean
    @Primary
    public InDBFoodProductRepository foodProductTestRepository(FoodProductDBRepository foodProductDBRepository, InDBProductRepository productRepository) {
        var repository = new InDBFoodProductRepository(foodProductDBRepository, productRepository);

        final LocalDate date = LocalDate.now();

        repository.save(new FoodProduct(null,
                food1,
                FoodConservation.ROOM_TEMPERATURE,
                ZonedDateTime.of(LocalDateTime.of(date.plusMonths(2).getYear(), date.plusMonths(2).getMonthValue(), date.plusMonths(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusMonths(1).getYear(), date.plusMonths(1).getMonthValue(), date.plusMonths(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                42));

        repository.save(new FoodProduct(null,
                food2,
                FoodConservation.ROOM_TEMPERATURE,
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(5).getYear(), date.plusDays(5).getMonthValue(), date.plusDays(5).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                27));

        repository.save(new FoodProduct(null,
                food3,
                FoodConservation.ROOM_TEMPERATURE,
                ZonedDateTime.of(LocalDateTime.of(date.plusMonths(1).getYear(), date.plusMonths(1).getMonthValue(), date.plusMonths(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusWeeks(2).getYear(), date.plusWeeks(2).getMonthValue(), date.plusWeeks(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                37));

        repository.save(new FoodProduct(null,
                food4,
                FoodConservation.FROZEN,
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(2).getYear(), date.plusDays(2).getMonthValue(), date.plusDays(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(1).getYear(), date.plusDays(1).getMonthValue(), date.plusDays(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                59));

        repository.save(new FoodProduct(null,
                food5,
                FoodConservation.REFRIGERATED,
                ZonedDateTime.of(LocalDateTime.of(date.plusWeeks(2).getYear(), date.plusWeeks(2).getMonthValue(), date.plusWeeks(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusWeeks(1).getYear(), date.plusWeeks(1).getMonthValue(), date.plusWeeks(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                41));

        repository.save(new FoodProduct(null,
                food6,
                FoodConservation.ROOM_TEMPERATURE,
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(4).getYear(), date.plusDays(4).getMonthValue(), date.plusDays(4).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(3).getYear(), date.plusDays(3).getMonthValue(), date.plusDays(3).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                56));

        repository.save(new FoodProduct(null,
                food7,
                FoodConservation.FROZEN,
                ZonedDateTime.of(LocalDateTime.of(date.plusWeeks(1).getYear(), date.plusWeeks(1).getMonthValue(), date.plusWeeks(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(5).getYear(), date.plusDays(5).getMonthValue(), date.plusDays(5).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                80));

        repository.save(new FoodProduct(null,
                food8,
                FoodConservation.REFRIGERATED,
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(3).getYear(), date.plusDays(3).getMonthValue(), date.plusDays(3).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(2).getYear(), date.plusDays(2).getMonthValue(), date.plusDays(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                71));

        repository.save(new FoodProduct(null,
                food9,
                FoodConservation.ROOM_TEMPERATURE,
                ZonedDateTime.of(LocalDateTime.of(date.plusMonths(1).getYear(), date.plusMonths(1).getMonthValue(), date.plusMonths(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusWeeks(2).getYear(), date.plusWeeks(2).getMonthValue(), date.plusWeeks(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                63));


        repository.save(new FoodProduct(null,
                food10,
                FoodConservation.FROZEN,
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(2).getYear(), date.plusDays(2).getMonthValue(), date.plusDays(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(1).getYear(), date.plusDays(1).getMonthValue(), date.plusDays(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                127));

        repository.save(new FoodProduct(null,
                food11,
                FoodConservation.REFRIGERATED,
                ZonedDateTime.of(LocalDateTime.of(date.plusWeeks(2).getYear(), date.plusWeeks(2).getMonthValue(), date.plusWeeks(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusWeeks(1).getYear(), date.plusWeeks(1).getMonthValue(), date.plusWeeks(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                11));

        repository.save(new FoodProduct(null,
                food12,
                FoodConservation.ROOM_TEMPERATURE,
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(4).getYear(), date.plusDays(4).getMonthValue(), date.plusDays(4).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(3).getYear(), date.plusDays(3).getMonthValue(), date.plusDays(3).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                35));

        repository.save(new FoodProduct(null,
                food13,
                FoodConservation.FROZEN,
                ZonedDateTime.of(LocalDateTime.of(date.plusWeeks(1).getYear(), date.plusWeeks(1).getMonthValue(), date.plusWeeks(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(5).getYear(), date.plusDays(5).getMonthValue(), date.plusDays(5).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                25));

        repository.save(new FoodProduct(null,
                food14,
                FoodConservation.REFRIGERATED,
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(3).getYear(), date.plusDays(3).getMonthValue(), date.plusDays(3).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(2).getYear(), date.plusDays(2).getMonthValue(), date.plusDays(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                134));

        repository.save(new FoodProduct(null,
                food15,
                FoodConservation.ROOM_TEMPERATURE,
                ZonedDateTime.of(LocalDateTime.of(date.plusMonths(1).getYear(), date.plusMonths(1).getMonthValue(), date.plusMonths(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusWeeks(2).getYear(), date.plusWeeks(2).getMonthValue(), date.plusWeeks(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                91));

        return repository;
    }

    @Bean
    @Primary
    public InDBStorageRepository storageTestRepository(StorageDBRepository storageDBRepository, InDBLocalUnitRepository inDBLocalUnitRepository) {
        var storageRepository = new InDBStorageRepository(storageDBRepository, inDBLocalUnitRepository);

        storageRepository.save(new Storage(new ID(1L), "Entrepot de l'unité local", localUnit, address));
        storageRepository.save(new Storage(new ID(2L), "Box de l'unité local", localUnit, address));

        return storageRepository;
    }

    @Bean
    @Primary
    public BeneficiaryProductRepository storageUserProductRepository(UserProductDBRepository userProductDBRepository, InDBBeneficiaryRepository beneficiaryRepository, InDBProductRepository productRepository, InDBStorageRepository storageRepository) {
        return new InDBBeneficiaryProductRepository(userProductDBRepository, beneficiaryRepository, productRepository, storageRepository);
    }

    @Bean
    @Primary
    public StorageProductRepository storageProductRepository(StorageProductDBRepository storageProductDBRepository, InDBProductRepository productRepository, InDBStorageRepository storageRepository, InDBFoodProductRepository foodProductRepository, InDBClothProductRepository clothProductRepository) {
        StorageProductRepository storageProductRepository = new InDBStorageProductRepository(storageProductDBRepository, productRepository, storageRepository);

        var storage = storageRepository.findById(new ID(1L)).orElseThrow();

        storageProductRepository.save(new StorageProduct(null, storage, cloth1, 5));
        storageProductRepository.save(new StorageProduct(null, storage, cloth2, 8));
        storageProductRepository.save(new StorageProduct(null, storage, cloth3, 15));
        storageProductRepository.save(new StorageProduct(null, storage, cloth4, 12));
        storageProductRepository.save(new StorageProduct(null, storage, cloth5, 20));
        storageProductRepository.save(new StorageProduct(null, storage, cloth6, 7));
        storageProductRepository.save(new StorageProduct(null, storage, cloth7, 10));
        storageProductRepository.save(new StorageProduct(null, storage, cloth8, 6));
        storageProductRepository.save(new StorageProduct(null, storage, cloth9, 9));
        storageProductRepository.save(new StorageProduct(null, storage, cloth10, 18));
        storageProductRepository.save(new StorageProduct(null, storage, cloth11, 3));
        storageProductRepository.save(new StorageProduct(null, storage, cloth12, 5));
        storageProductRepository.save(new StorageProduct(null, storage, cloth13, 12));
        storageProductRepository.save(new StorageProduct(null, storage, cloth14, 15));
        storageProductRepository.save(new StorageProduct(null, storage, cloth15, 9));
        storageProductRepository.save(new StorageProduct(null, storage, food1, 8));
        storageProductRepository.save(new StorageProduct(null, storage, food2, 6));
        storageProductRepository.save(new StorageProduct(null, storage, food3, 4));
        storageProductRepository.save(new StorageProduct(null, storage, food4, 10));
        storageProductRepository.save(new StorageProduct(null, storage, food5, 15));
        storageProductRepository.save(new StorageProduct(null, storage, food6, 7));
        storageProductRepository.save(new StorageProduct(null, storage, food7, 12));
        storageProductRepository.save(new StorageProduct(null, storage, food8, 9));
        storageProductRepository.save(new StorageProduct(null, storage, food9, 11));
        storageProductRepository.save(new StorageProduct(null, storage, food10, 14));
        storageProductRepository.save(new StorageProduct(null, storage, food11, 8));
        storageProductRepository.save(new StorageProduct(null, storage, food12, 6));
        storageProductRepository.save(new StorageProduct(null, storage, food13, 3));
        storageProductRepository.save(new StorageProduct(null, storage, food14, 5));
        storageProductRepository.save(new StorageProduct(null, storage, food15, 10));

        return new InDBStorageProductRepository(storageProductDBRepository, productRepository, storageRepository);
    }

    @Bean
    @Primary
    public StorageProductService storageProductServiceCore(StorageProductRepository storageProductRepository) {
        return new StorageProductService(storageProductRepository);
    }
}
