package fr.croixrouge.config;

import fr.croixrouge.domain.model.*;
import fr.croixrouge.domain.repository.LocalUnitRepository;
import fr.croixrouge.domain.repository.RoleRepository;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;
import fr.croixrouge.model.EventTimeWindow;
import fr.croixrouge.repository.EventRepository;
import fr.croixrouge.repository.db.user.UserDBRepository;
import fr.croixrouge.service.BeneficiaryService;
import fr.croixrouge.service.VolunteerService;
import fr.croixrouge.storage.model.BeneficiaryProduct;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.*;
import fr.croixrouge.storage.model.quantifier.*;
import fr.croixrouge.storage.repository.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.*;
import java.util.*;

@Configuration
@Profile("fixtures-prod")
public class FixturesConfig {
    private final Role managerRoleValOrge, defaultRoleValOrge, beneficiaryRoleValOrge, managerRoleESGI, defaultRoleESGI, beneficiaryRoleESGI;
    private final User managerValOrge, volunteerUserValOrge1, volunteerUserValOrge2, volunteerUserValOrge3, volunteerUserValOrge4, volunteerUserValOrge5, volunteerUserValOrge6, volunteerUserValOrge7, volunteerUserValOrge8, volunteerUserValOrge9, volunteerUserValOrge10, beneficiaryUserValOrge1, beneficiaryUserValOrge2, beneficiaryUserValOrge3, beneficiaryUserValOrge4, beneficiaryUserValOrge5, beneficiaryUserValOrge6, beneficiaryUserValOrge7, beneficiaryUserValOrge8, beneficiaryUserValOrge9, beneficiaryUserValOrge10, beneficiaryUserValOrge11, beneficiaryUserValOrge12, beneficiaryUserValOrge13, beneficiaryUserValOrge14, beneficiaryUserValOrge15, beneficiaryUserValOrge16, beneficiaryUserValOrge17, beneficiaryUserValOrge18, beneficiaryUserValOrge19, beneficiaryUserValOrge20, beneficiaryUserValOrge21, beneficiaryUserValOrge22, beneficiaryUserValOrge23, beneficiaryUserValOrge24, beneficiaryUserValOrge25, beneficiaryUserValOrge26, beneficiaryUserValOrge27, beneficiaryUserValOrge28, beneficiaryUserValOrge29, beneficiaryUserValOrge30, beneficiaryUserValOrge31, beneficiaryUserValOrge32, beneficiaryUserValOrge33, beneficiaryUserValOrge34, beneficiaryUserValOrge35, beneficiaryUserValOrge36, beneficiaryUserValOrge37, beneficiaryUserValOrge38, beneficiaryUserValOrge39, beneficiaryUserValOrge40, beneficiaryUserValOrge41, beneficiaryUserValOrge42, beneficiaryUserValOrge43, beneficiaryUserValOrge44, beneficiaryUserValOrge45, beneficiaryUserValOrge46, beneficiaryUserValOrge47, managerESGI, volunteerUserESGI1, volunteerUserESGI2, volunteerUserESGI3, volunteerUserESGI4, volunteerUserESGI5, beneficiaryUserESGI1, beneficiaryUserESGI2, beneficiaryUserESGI3, beneficiaryUserESGI4, beneficiaryUserESGI5, beneficiaryUserESGI6, beneficiaryUserESGI7, beneficiaryUserESGI8, beneficiaryUserESGI9, beneficiaryUserESGI10;
    private final Volunteer volunteerManagerValOrge, volunteerValOrge1, volunteerValOrge2, volunteerValOrge3, volunteerValOrge4, volunteerValOrge5, volunteerValOrge6, volunteerValOrge7, volunteerValOrge8, volunteerValOrge9, volunteerValOrge10, volunteerManagerESGI, volunteerESGI1, volunteerESGI2, volunteerESGI3, volunteerESGI4, volunteerESGI5;
    private final Beneficiary beneficiary1, beneficiary2, beneficiary3, beneficiary4, beneficiary5, beneficiary6, beneficiary7, beneficiary8, beneficiary9, beneficiary10, beneficiary11, beneficiary12, beneficiary13, beneficiary14, beneficiary15, beneficiary16, beneficiary17, beneficiary18, beneficiary19, beneficiary20, beneficiary21, beneficiary22, beneficiary23, beneficiary24, beneficiary25, beneficiary26, beneficiary27, beneficiary28, beneficiary29, beneficiary30, beneficiary31, beneficiary32, beneficiary33, beneficiary34, beneficiary35, beneficiary36, beneficiary37, beneficiary38, beneficiary39, beneficiary40, beneficiary41, beneficiary42, beneficiary43, beneficiary44, beneficiary45, beneficiary46, beneficiary47, beneficiaryESGI1, beneficiaryESGI2, beneficiaryESGI3, beneficiaryESGI4, beneficiaryESGI5, beneficiaryESGI6, beneficiaryESGI7, beneficiaryESGI8, beneficiaryESGI9, beneficiaryESGI10;
    private final Address address = new Address(Department.getDepartmentFromPostalCode("91"), "91240", "St Michel sur Orge", "76 rue des Liers");

    private final Address addressESGI = new Address(Department.getDepartmentFromPostalCode("75"), "75012", "Paris", "242 rue du faubourg Saint Antoine");
    private final LocalUnit localUnit;

    private final LocalUnit localUnitESGI;
    private final Product cloth1, cloth2, cloth3, cloth4, cloth5, cloth6, cloth7, cloth8, cloth9, cloth10, cloth11, cloth12, cloth13, cloth14, cloth15, food1, food2, food3, food4, food5, food6, food7, food8, food9, food10, food11, food12, food13, food14, food15;
    private final Product cloth1ESGI, cloth2ESGI, cloth3ESGI, cloth4ESGI, cloth5ESGI, cloth6ESGI, cloth7ESGI, cloth8ESGI, cloth9ESGI, cloth10ESGI, cloth11ESGI, cloth12ESGI, cloth13ESGI, cloth14ESGI, cloth15ESGI, food1ESGI, food2ESGI, food3ESGI, food4ESGI, food5ESGI, food6ESGI, food7ESGI, food8ESGI, food9ESGI, food10ESGI, food11ESGI, food12ESGI, food13ESGI, food14ESGI, food15ESGI;
    private final List<ProductLimit> productLimits;
    private final Storage storageValOrge, storageESGI;

    private Map<Beneficiary, List<LocalDateTime>> beneficiaryFoodProductDates = new HashMap<>();
    private Map<Beneficiary, List<LocalDateTime>> beneficiaryClothProductDates = new HashMap<>();

    private Map<Beneficiary, List<LocalDateTime>> beneficiaryFoodProductDatesESGI = new HashMap<>();
    private Map<Beneficiary, List<LocalDateTime>> beneficiaryClothProductDatesESGI = new HashMap<>();
    private final UserDBRepository userDBRepository;

    public FixturesConfig(RoleConfig roleConfig,
                          LocalUnitRepository localUnitRepository,
                          RoleRepository roleRepository,
                          VolunteerService volunteerService,
                          BeneficiaryService beneficiaryService,
                          EventRepository eventRepository,
                          StorageProductRepository storageProductRepository,
                          ProductLimitRepository productLimitRepository,
                          ClothProductRepository clothProductRepository,
                          FoodProductRepository foodProductRepository,
                          StorageRepository storageRepository,
                          BeneficiaryProductRepository storageUserProductRepository,
                          UserDBRepository userDBRepository) {

        var random = new Random();

        localUnit = new LocalUnit(new ID(1L),
                "Unité Local du Val d'Orge",
                address,
                "bernard.lhuillier@crx.fr",
                address.getPostalCode() + "-181");

        localUnitESGI = new LocalUnit(new ID(2L),
                "Unité Local de l'ESGI",
                addressESGI,
                "pierre.dumont@esgi.fr",
                addressESGI.getPostalCode() + "-666");


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

        managerRoleESGI = new Role(null,
                "ESGI gestionnaire",
                "Role pour les gestionnaires de l'ESGI",
                roleResources,
                localUnitESGI,
                List.of());

        defaultRoleESGI = new Role(null,
                "ESGI volontaires",
                "Role par défaut pour les volontaires de l'ESGI",
                defaultRoleResources,
                localUnitESGI,
                List.of());

        beneficiaryRoleESGI = new Role(null,
                "ESGI bénéficiaires",
                "Role par défaut pour les bénéficiaires de l'ESGI",
                defaultRoleResources,
                localUnitESGI,
                List.of());

        managerValOrge = new User(null, "bernard.lhuillier@crx.fr", "Password.123", localUnit, List.of(managerRoleValOrge), true, null);

        volunteerUserValOrge1 = new User(null, "valerie.leroux@crx.fr", "Password.123", localUnit, List.of(defaultRoleValOrge), true, null);
        volunteerUserValOrge2 = new User(null, "jerome.piat@crx.fr", "Password.123", localUnit, List.of(defaultRoleValOrge), true, null);
        volunteerUserValOrge3 = new User(null, "elodie.lechervy@crx.fr", "Password.123", localUnit, List.of(defaultRoleValOrge), true, null);
        volunteerUserValOrge4 = new User(null, "hugo.jean@crx.fr", "Password.123", localUnit, List.of(defaultRoleValOrge), true, null);
        volunteerUserValOrge5 = new User(null, "sebastien.joubert@crx.fr", "Password.123", localUnit, List.of(defaultRoleValOrge), true, null);
        volunteerUserValOrge6 = new User(null, "thomas.georget@crx.fr", "Password.123", localUnit, List.of(defaultRoleValOrge), true, null);
        volunteerUserValOrge7 = new User(null, "gael.germain@crx.fr", "Password.123", localUnit, List.of(defaultRoleValOrge), true, null);
        volunteerUserValOrge8 = new User(null, "charles.lefeuvre@crx.fr", "Password.123", localUnit, List.of(defaultRoleValOrge), true, null);
        volunteerUserValOrge9 = new User(null, "emilie.lassalas@crx.fr", "Password.123", localUnit, List.of(defaultRoleValOrge), true, null);
        volunteerUserValOrge10 = new User(null, "anne.ozanne@crx.fr", "Password.123", localUnit, List.of(defaultRoleValOrge), false, "d0f8c8a0-9f9a-4e9a-9b1a-2b7a3d1c0a0a");

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

        managerESGI = new User(null, "pierre.dumont@esgi.fr", "ESGI.2023", localUnitESGI, List.of(managerRoleESGI), true, null);

        volunteerUserESGI1 = new User(null, "lucie.martin@esgi.fr", "ESGI.2023", localUnitESGI, List.of(defaultRoleESGI), true, null);
        volunteerUserESGI2 = new User(null, "benjamin.durand@esgi.fr", "ESGI.2023", localUnitESGI, List.of(defaultRoleESGI), true, null);
        volunteerUserESGI3 = new User(null, "claire.lemoine@esgi.fr", "ESGI.2023", localUnitESGI, List.of(defaultRoleESGI), true, null);
        volunteerUserESGI4 = new User(null, "alexis.dupont@esgi.fr", "ESGI.2023", localUnitESGI, List.of(defaultRoleESGI), true, null);
        volunteerUserESGI5 = new User(null, "sophie.girard@esgi.fr", "ESGI.2023", localUnitESGI, List.of(defaultRoleESGI), true, null);

        volunteerManagerESGI = new Volunteer(null, managerESGI, "Pierre", "DUMONT", "+33 6 40 50 60 70", true);
        volunteerESGI1 = new Volunteer(null, volunteerUserESGI1, "Lucie", "MARTIN", "+33 6 80 90 10 20", true);
        volunteerESGI2 = new Volunteer(null, volunteerUserESGI2, "Benjamin", "DURAND", "+33 6 70 80 90 10", true);
        volunteerESGI3 = new Volunteer(null, volunteerUserESGI3, "Claire", "LEMOINE", "+33 6 60 70 80 90", true);
        volunteerESGI4 = new Volunteer(null, volunteerUserESGI4, "Alexis", "DUPONT", "+33 6 50 60 70 80", true);
        volunteerESGI5 = new Volunteer(null, volunteerUserESGI5, "Sophie", "GIRARD", "+33 6 40 50 60 70", true);

        beneficiaryUserValOrge1 = new User(null, "dubois.elise92@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge2 = new User(null, "m.martin-explorer@inlook.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge3 = new User(null, "lea.lambert91@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge4 = new User(null, "jerome.girard.aventure@orange.fr", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge5 = new User(null, "camille.roux@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge6 = new User(null, "alexandre.bernard@inlook.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge7 = new User(null, "margot.dupont@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge8 = new User(null, "antoine.lefebvre@orange.fr", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge9 = new User(null, "juliette.moreau@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge10 = new User(null, "theo.thomas@inlook.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge11 = new User(null, "fernando.gomez92@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge12 = new User(null, "ana.martinez-explorer@inlook.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge13 = new User(null, "jose.sanchez91@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge14 = new User(null, "maria.hernandez.aventure@orange.fr", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge15 = new User(null, "juan.rodriguez@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge16 = new User(null, "carmen.gonzalez@inlook.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge17 = new User(null, "manuel.fernandez@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge18 = new User(null, "isabel.lopez@orange.fr", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge19 = new User(null, "francisco.torres@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge20 = new User(null, "rosa.garcia@inlook.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge21 = new User(null, "ali.hassan92@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge22 = new User(null, "amina.mahmoud-explorer@inlook.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge23 = new User(null, "omar.farouk91@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge24 = new User(null, "sarah.abdelrahman@orange.fr", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge25 = new User(null, "yusuf.alamoudi@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge26 = new User(null, "huda.fawzy@inlook.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge27 = new User(null, "kareem.elshamy@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge28 = new User(null, "asmaa.abdelfattah@orange.fr", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge29 = new User(null, "mostafa.kamal@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge30 = new User(null, "samar.ahmed@inlook.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge31 = new User(null, "khaled.sayed@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge32 = new User(null, "heba.elmohandes@inlook.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge33 = new User(null, "mohammed.ali@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge34 = new User(null, "fatma.hosny@inlook.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge35 = new User(null, "tamer.hosny@inlook.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge36 = new User(null, "jan.kowalski92@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge37 = new User(null, "anna.nowak-explorer@inlook.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge38 = new User(null, "piotr.wisniewski@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge39 = new User(null, "ewa.dabrowska@orange.fr", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge40 = new User(null, "tomasz.zielinski@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge41 = new User(null, "amara.ndiaye@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge42 = new User(null, "nneka.osei@inlook.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge43 = new User(null, "kwame.mensah@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge44 = new User(null, "aminata.diallo@orange.fr", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), true, null);
        beneficiaryUserValOrge45 = new User(null, "tendai.mutasa@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), false, "a83e4cc4-1bc0-11ee-be56-0242ac120002");
        beneficiaryUserValOrge46 = new User(null, "zola.ndlovu@inlook.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), false, "b1fc63d6-1bc0-11ee-be56-0242ac120002");
        beneficiaryUserValOrge47 = new User(null, "sibusiso.khumalo@gail.com", "Password.123", localUnit, List.of(beneficiaryRoleValOrge), false, "bb550dca-1bc0-11ee-be56-0242ac120002");

        beneficiary1 = new Beneficiary(null, beneficiaryUserValOrge1, "Eloise", "DEBOIS", "+33 6 72 51 39 84", true, LocalDate.of(2000, 6, 1), "2 00 06 01 2A 122 019", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary2 = new Beneficiary(null, beneficiaryUserValOrge2, "Mathieu", "MARTIN", "+33 6 28 93 75 46", true, LocalDate.of(1992, 3, 3), "1 92 03 03 75 113 557", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary3 = new Beneficiary(null, beneficiaryUserValOrge3, "Léa", "LAMBERT", "+33 7 57 84 21 36", true, LocalDate.of(1988, 7, 19), "2 88 07 19 91 190 349", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary4 = new Beneficiary(null, beneficiaryUserValOrge4, "Jerome", "GIRARD", "+33 7 63 18 47 92", true, LocalDate.of(1983, 11, 7), "1 83 11 07 91 125 789", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary5 = new Beneficiary(null, beneficiaryUserValOrge5, "Camille", "ROUX", "+33 6 41 75 69 23", true, LocalDate.of(1990, 5, 28), "2 90 05 28 93 102 901", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary6 = new Beneficiary(null, beneficiaryUserValOrge6, "Alexandre", "BERNARD", "+33 7 89 36 54 17", true, LocalDate.of(1987, 1, 15), "1 87 01 15 92 105 302", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary7 = new Beneficiary(null, beneficiaryUserValOrge7, "Margaux", "DUPONT", "+33 6 73 95 28 41", true, LocalDate.of(1994, 8, 9), "2 94 08 09 75 120 270", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary8 = new Beneficiary(null, beneficiaryUserValOrge8, "Antoine", "LEFEBVRE", "+33 6 48 21 67 93", true, LocalDate.of(1991, 6, 25), "1 91 06 25 91 157 633", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary9 = new Beneficiary(null, beneficiaryUserValOrge9, "Juliette", "MOREAU", "+33 7 37 84 52 19", true, LocalDate.of(1989, 12, 17), "2 89 12 17 91 143 751", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary10 = new Beneficiary(null, beneficiaryUserValOrge10, "Théo", "THOMAS", "+33 7 92 57 13 48", true, LocalDate.of(1993, 2, 6), "1 93 02 06 91 109 669", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary11 = new Beneficiary(null, beneficiaryUserValOrge11, "Fernando", "GOMEZ", "+34 6 72 51 39 84", true, LocalDate.of(1980, 6, 1), "1 80 06 01 99 999 019", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary12 = new Beneficiary(null, beneficiaryUserValOrge12, "Ana", "MARTINEZ", "+34 6 28 93 75 46", true, LocalDate.of(1985, 3, 3), "2 85 03 03 99 999 557", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary13 = new Beneficiary(null, beneficiaryUserValOrge13, "José", "SANCHEZ", "+34 7 57 84 21 36", true, LocalDate.of(1988, 7, 19), "1 88 07 19 99 999 349", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary14 = new Beneficiary(null, beneficiaryUserValOrge14, "María", "HERNANDEZ", "+34 7 63 18 47 92", true, LocalDate.of(1978, 11, 7), "2 78 11 07 99 999 789", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary15 = new Beneficiary(null, beneficiaryUserValOrge15, "Juan", "RODRIGUEZ", "+34 6 41 75 69 23", true, LocalDate.of(1990, 5, 28), "1 90 05 28 99 999 901", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary16 = new Beneficiary(null, beneficiaryUserValOrge16, "Carmen", "GONZALEZ", "+34 7 89 36 54 17", true, LocalDate.of(1987, 1, 15), "2 87 01 15 99 999 302", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary17 = new Beneficiary(null, beneficiaryUserValOrge17, "Manuel", "FERNANDEZ", "+34 6 73 95 28 41", true, LocalDate.of(1994, 8, 9), "1 94 08 09 99 999 270", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary18 = new Beneficiary(null, beneficiaryUserValOrge18, "Isabel", "LOPEZ", "+34 6 48 21 67 93", true, LocalDate.of(1991, 6, 25), "2 91 06 25 99 999 633", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary19 = new Beneficiary(null, beneficiaryUserValOrge19, "Francisco", "TORRES", "+34 7 37 84 52 19", true, LocalDate.of(1989, 12, 17), "1 89 12 17 99 999 751", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary20 = new Beneficiary(null, beneficiaryUserValOrge20, "Rosa", "GARCIA", "+34 7 92 57 13 48", true, LocalDate.of(1993, 2, 6), "2 93 02 06 99 999 669", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary21 = new Beneficiary(null, beneficiaryUserValOrge21, "Ali", "HASSAN", "+212 6 72 51 39 84", true, LocalDate.of(1980, 6, 1), "1 80 06 01 99 999 019", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary22 = new Beneficiary(null, beneficiaryUserValOrge22, "Amina", "MAHMOUD", "+20 6 28 93 75 46", true, LocalDate.of(1985, 3, 3), "2 85 03 03 99 999 557", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary23 = new Beneficiary(null, beneficiaryUserValOrge23, "Omar", "FAROUK", "+971 7 57 84 21 36", true, LocalDate.of(1988, 7, 19), "1 88 07 19 99 999 349", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary24 = new Beneficiary(null, beneficiaryUserValOrge24, "Sarah", "ABDELRAHMAN", "+962 7 63 18 47 92", true, LocalDate.of(1978, 11, 7), "2 78 11 07 99 999 789", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary25 = new Beneficiary(null, beneficiaryUserValOrge25, "Yusuf", "ALAMOUDI", "+966 6 41 75 69 23", true, LocalDate.of(1990, 5, 28), "1 90 05 28 99 999 901", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary26 = new Beneficiary(null, beneficiaryUserValOrge26, "Huda", "FAWZY", "+20 7 89 36 54 17", true, LocalDate.of(1987, 1, 15), "2 87 01 15 99 999 302", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary27 = new Beneficiary(null, beneficiaryUserValOrge27, "Kareem", "ELSHAMY", "+213 6 73 95 28 41", true, LocalDate.of(1994, 8, 9), "1 94 08 09 99 999 270", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary28 = new Beneficiary(null, beneficiaryUserValOrge28, "Asmaa", "ABDELFATTAH", "+20 6 48 21 67 93", true, LocalDate.of(1991, 6, 25), "2 91 06 25 99 999 633", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary29 = new Beneficiary(null, beneficiaryUserValOrge29, "Mostafa", "KAMAL", "+20 7 37 84 52 19", true, LocalDate.of(1989, 12, 17), "1 89 12 17 99 999 751", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary30 = new Beneficiary(null, beneficiaryUserValOrge30, "Samar", "AHMED", "+20 7 92 57 13 48", true, LocalDate.of(1993, 2, 6), "2 93 02 06 99 999 669", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary31 = new Beneficiary(null, beneficiaryUserValOrge31, "Khaled", "SAYED", "+20 7 92 57 13 48", true, LocalDate.of(1980, 4, 25), "1 80 04 25 99 999 019", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary32 = new Beneficiary(null, beneficiaryUserValOrge32, "Heba", "ELMOHANDES", "+20 6 28 93 75 46", true, LocalDate.of(1975, 7, 17), "2 75 07 17 99 999 557", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary33 = new Beneficiary(null, beneficiaryUserValOrge33, "Mohammed", "ALI", "+20 7 57 84 21 36", true, LocalDate.of(1980, 1, 1), "1 80 01 01 99 999 349", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary34 = new Beneficiary(null, beneficiaryUserValOrge34, "Fatma", "HOSNY", "+20 7 63 18 47 92", true, LocalDate.of(1978, 5, 15), "2 78 05 15 99 999 789", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary35 = new Beneficiary(null, beneficiaryUserValOrge35, "Tamer", "HOSNY", "+20 6 41 75 69 23", true, LocalDate.of(1985, 6, 10), "1 85 06 10 99 999 901", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary36 = new Beneficiary(null, beneficiaryUserValOrge36, "Jan", "KOWALSKI", "+48 672 513 984", true, LocalDate.of(1982, 1, 13), "1 82 01 13 2A 122 019", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary37 = new Beneficiary(null, beneficiaryUserValOrge37, "Anna", "NOWAK", "+48 628 937 546", true, LocalDate.of(1987, 7, 5), "2 87 07 05 75 113 557", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary38 = new Beneficiary(null, beneficiaryUserValOrge38, "Piotr", "WISNIEWSKI", "+48 757 842 136", true, LocalDate.of(1978, 9, 19), "1 78 09 19 91 190 349", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary39 = new Beneficiary(null, beneficiaryUserValOrge39, "Ewa", "DĄBROWSKA", "+48 763 184 792", true, LocalDate.of(1980, 3, 7), "2 80 03 07 91 125 789", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary40 = new Beneficiary(null, beneficiaryUserValOrge40, "Tomasz", "ZIELINSKI", "+48 417 569 233", true, LocalDate.of(1985, 5, 28), "1 85 05 28 93 102 901", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary41 = new Beneficiary(null, beneficiaryUserValOrge41, "Amara", "Ndiaye", "+221 672 513 984", true, LocalDate.of(1982, 2, 13), "1 82 02 13 2A 122 019", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary42 = new Beneficiary(null, beneficiaryUserValOrge42, "Nneka", "Osei", "+233 628 937 546", true, LocalDate.of(1987, 7, 5), "2 87 07 05 75 113 557", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary43 = new Beneficiary(null, beneficiaryUserValOrge43, "Kwame", "Mensah", "+233 757 842 136", true, LocalDate.of(1978, 10, 19), "1 78 10 19 91 190 349", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary44 = new Beneficiary(null, beneficiaryUserValOrge44, "Aminata", "Diallo", "+221 763 184 792", true, LocalDate.of(1980, 6, 7), "2 80 06 07 91 125 789", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary45 = new Beneficiary(null, beneficiaryUserValOrge45, "Tendai", "Mutasa", "+263 417 569 233", true, LocalDate.of(1985, 8, 28), "1 85 08 28 93 102 901", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary46 = new Beneficiary(null, beneficiaryUserValOrge46, "Zola", "Ndlovu", "+263 753 628 841", true, LocalDate.of(1994, 5, 25), "2 94 05 25 91 157 633", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiary47 = new Beneficiary(null, beneficiaryUserValOrge47, "Sibusiso", "Khumalo", "+263 437 845 219", true, LocalDate.of(1989, 12, 15), "1 89 12 15 91 143 751", List.of(), (long) (random.nextDouble() * 1000 + 2000));

        beneficiaryUserESGI1 = new User(null, "pierre.paul@gail.com", "ESGI.2023", localUnitESGI, List.of(beneficiaryRoleESGI), true, null);
        beneficiaryUserESGI2 = new User(null, "jean.dupont@inlook.com", "ESGI.2023", localUnitESGI, List.of(beneficiaryRoleESGI), true, null);
        beneficiaryUserESGI3 = new User(null, "marie.durand@gail.com", "ESGI.2023", localUnitESGI, List.of(beneficiaryRoleESGI), true, null);
        beneficiaryUserESGI4 = new User(null, "louis.bernard@orange.fr", "ESGI.2023", localUnitESGI, List.of(beneficiaryRoleESGI), true, null);
        beneficiaryUserESGI5 = new User(null, "claire.lefebvre@gail.com", "ESGI.2023", localUnitESGI, List.of(beneficiaryRoleESGI), true, null);
        beneficiaryUserESGI6 = new User(null, "alain.moreau@inlook.com", "ESGI.2023", localUnitESGI, List.of(beneficiaryRoleESGI), true, null);
        beneficiaryUserESGI7 = new User(null, "sophie.martin@gail.com", "ESGI.2023", localUnitESGI, List.of(beneficiaryRoleESGI), true, null);
        beneficiaryUserESGI8 = new User(null, "bernard.leroy@orange.fr", "ESGI.2023", localUnitESGI, List.of(beneficiaryRoleESGI), true, null);
        beneficiaryUserESGI9 = new User(null, "chantal.dumont@gail.com", "ESGI.2023", localUnitESGI, List.of(beneficiaryRoleESGI), true, null);
        beneficiaryUserESGI10 = new User(null, "francois.rousseau@inlook.com", "ESGI.2023", localUnitESGI, List.of(beneficiaryRoleESGI), true, null);

        beneficiaryESGI1 = new Beneficiary(null, beneficiaryUserESGI1, "Pierre", "PAUL", "0672513984", true, LocalDate.of(1990, 5, 5), "1 90 05 05 2A 122 019", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiaryESGI2 = new Beneficiary(null, beneficiaryUserESGI2, "Jean", "DUPONT", "0628937546", true, LocalDate.of(1992, 3, 6), "1 92 03 06 75 113 557", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiaryESGI3 = new Beneficiary(null, beneficiaryUserESGI3, "Marie", "DURAND", "0757842136", true, LocalDate.of(1988, 7, 19), "2 88 07 19 91 190 349", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiaryESGI4 = new Beneficiary(null, beneficiaryUserESGI4, "Louis", "BERNARD", "0763184792", true, LocalDate.of(1983, 11, 9), "1 83 11 09 91 125 789", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiaryESGI5 = new Beneficiary(null, beneficiaryUserESGI5, "Claire", "LEFEBVRE", "0641756923", true, LocalDate.of(1990, 5, 27), "2 90 05 27 93 102 901", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiaryESGI6 = new Beneficiary(null, beneficiaryUserESGI6, "Alain", "MOREAU", "0789365417", true, LocalDate.of(1987, 1, 18), "1 87 01 18 92 105 302", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiaryESGI7 = new Beneficiary(null, beneficiaryUserESGI7, "Sophie", "MARTIN", "0673952841", true, LocalDate.of(1994, 8, 10), "2 94 08 10 75 120 270", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiaryESGI8 = new Beneficiary(null, beneficiaryUserESGI8, "Bernard", "LEROY", "0648216793", true, LocalDate.of(1991, 6, 25), "1 91 06 25 91 157 633", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiaryESGI9 = new Beneficiary(null, beneficiaryUserESGI9, "Chantal", "DUMONT", "0737845219", true, LocalDate.of(1989, 12, 18), "2 89 12 18 91 143 751", List.of(), (long) (random.nextDouble() * 1000 + 2000));
        beneficiaryESGI10 = new Beneficiary(null, beneficiaryUserESGI10, "Francois", "ROUSSEAU", "0792571348", true, LocalDate.of(1993, 2, 8), "1 93 02 08 91 109 669", List.of(), (long) (random.nextDouble() * 1000 + 2000));


        var productLimit = new ProductLimit(null, "Farine", Duration.ofDays(15), new Quantifier(1, WeightUnit.KILOGRAM), localUnit.getId());
        var productLimit1 = new ProductLimit(null, "Sucre", Duration.ofDays(15), new Quantifier(1, WeightUnit.KILOGRAM), localUnit.getId());
        var productLimit2 = new ProductLimit(null, "Couche", Duration.ofDays(15), new Quantifier(1, NumberedUnit.NUMBER), localUnit.getId());
        var productLimit3 = new ProductLimit(null, "Lessive", Duration.ofDays(30), new Quantifier(1, VolumeUnit.LITER), localUnit.getId());
        var productLimit14 = new ProductLimit(null, "laitier", Duration.ofDays(30), new Quantifier(1, VolumeUnit.LITER), localUnit.getId());
        var productLimit4 = new ProductLimit(null, "Brosse a dents", Duration.ofDays(60), new Quantifier(1, NumberedUnit.NUMBER), localUnit.getId());
        var productLimit5 = new ProductLimit(null, "dentifrice", Duration.ofDays(30), new Quantifier(1, NumberedUnit.NUMBER), localUnit.getId());
        var productLimit6 = new ProductLimit(null, "shampooing / gel douche", Duration.ofDays(15), new Quantifier(1, VolumeUnit.LITER), localUnit.getId());
        var productLimit7 = new ProductLimit(null, "féculents", Duration.ofDays(15), new Quantifier(1, WeightUnit.KILOGRAM), localUnit.getId());
        var productLimit8 = new ProductLimit(null, "légumes", Duration.ofDays(15), new Quantifier(1, WeightUnit.KILOGRAM), localUnit.getId());
        var productLimit13 = new ProductLimit(null, "viandes", Duration.ofDays(15), new Quantifier(1, WeightUnit.KILOGRAM), localUnit.getId());
        var productLimit9 = new ProductLimit(null, "fruits", Duration.ofDays(15), new Quantifier(1, WeightUnit.KILOGRAM), localUnit.getId());
        var productLimit10 = new ProductLimit(null, "sous vêtements", Duration.ofDays(30), new Quantifier(5, NumberedUnit.NUMBER), localUnit.getId());
        var productLimit11 = new ProductLimit(null, "vêtements", Duration.ofDays(30), new Quantifier(5, NumberedUnit.NUMBER), localUnit.getId());
        var productLimit12 = new ProductLimit(null, "sur vêtements", Duration.ofDays(30), new Quantifier(5, NumberedUnit.NUMBER), localUnit.getId());

        var productLimitESGI = new ProductLimit(null, "Farine", Duration.ofDays(15), new Quantifier(1, WeightUnit.KILOGRAM), localUnitESGI.getId());
        var productLimitESGI1 = new ProductLimit(null, "Sucre", Duration.ofDays(15), new Quantifier(1, WeightUnit.KILOGRAM), localUnitESGI.getId());
        var productLimitESGI2 = new ProductLimit(null, "Couche", Duration.ofDays(15), new Quantifier(1, NumberedUnit.NUMBER), localUnitESGI.getId());
        var productLimitESGI3 = new ProductLimit(null, "Lessive", Duration.ofDays(30), new Quantifier(1, VolumeUnit.LITER), localUnitESGI.getId());
        var productLimitESGI14 = new ProductLimit(null, "laitier", Duration.ofDays(30), new Quantifier(1, VolumeUnit.LITER), localUnitESGI.getId());
        var productLimitESGI4 = new ProductLimit(null, "Brosse a dents", Duration.ofDays(60), new Quantifier(1, NumberedUnit.NUMBER), localUnitESGI.getId());
        var productLimitESGI5 = new ProductLimit(null, "dentifrice", Duration.ofDays(30), new Quantifier(1, NumberedUnit.NUMBER), localUnitESGI.getId());
        var productLimitESGI6 = new ProductLimit(null, "shampooing / gel douche", Duration.ofDays(15), new Quantifier(1, VolumeUnit.LITER), localUnitESGI.getId());
        var productLimitESGI7 = new ProductLimit(null, "féculents", Duration.ofDays(15), new Quantifier(1, WeightUnit.KILOGRAM), localUnitESGI.getId());
        var productLimitESGI8 = new ProductLimit(null, "légumes", Duration.ofDays(15), new Quantifier(1, WeightUnit.KILOGRAM), localUnitESGI.getId());
        var productLimitESGI13 = new ProductLimit(null, "viandes", Duration.ofDays(15), new Quantifier(1, WeightUnit.KILOGRAM), localUnitESGI.getId());
        var productLimitESGI9 = new ProductLimit(null, "fruits", Duration.ofDays(15), new Quantifier(1, WeightUnit.KILOGRAM), localUnitESGI.getId());
        var productLimitESGI10 = new ProductLimit(null, "sous vêtements", Duration.ofDays(30), new Quantifier(5, NumberedUnit.NUMBER), localUnitESGI.getId());
        var productLimitESGI11 = new ProductLimit(null, "vêtements", Duration.ofDays(30), new Quantifier(5, NumberedUnit.NUMBER), localUnitESGI.getId());
        var productLimitESGI12 = new ProductLimit(null, "sur vêtements", Duration.ofDays(30), new Quantifier(5, NumberedUnit.NUMBER), localUnitESGI.getId());

        productLimits = List.of(productLimit, productLimit1, productLimit2, productLimit3, productLimit4, productLimit5, productLimit6, productLimit7, productLimit8, productLimit9, productLimit10, productLimit11, productLimit12, productLimit13, productLimit14,
                productLimitESGI, productLimitESGI1, productLimitESGI2, productLimitESGI3, productLimitESGI4, productLimitESGI5, productLimitESGI6, productLimitESGI7, productLimitESGI8, productLimitESGI9, productLimitESGI10, productLimitESGI11, productLimitESGI12, productLimitESGI13, productLimitESGI14);

        cloth1 = new Product(null, "Chemises blanches", new Quantifier(2, NumberedUnit.NUMBER), productLimit11, localUnit.getId());
        cloth2 = new Product(null, "Chemises blanches", new Quantifier(2, NumberedUnit.NUMBER), productLimit11, localUnit.getId());
        cloth3 = new Product(null, "Chemises blanches", new Quantifier(2, NumberedUnit.NUMBER), productLimit11, localUnit.getId());
        cloth4 = new Product(null, "Chemises blanches", new Quantifier(2, NumberedUnit.NUMBER), productLimit11, localUnit.getId());
        cloth5 = new Product(null, "Chemises blanches", new Quantifier(2, NumberedUnit.NUMBER), productLimit11, localUnit.getId());
        cloth6 = new Product(null, "T-shirts blancs", new Quantifier(2, NumberedUnit.NUMBER), productLimit11, localUnit.getId());
        cloth7 = new Product(null, "Pantalons noirs", new Quantifier(2, NumberedUnit.NUMBER), productLimit11, localUnit.getId());
        cloth8 = new Product(null, "Robes rouges", new Quantifier(1, NumberedUnit.NUMBER), productLimit11, localUnit.getId());
        cloth9 = new Product(null, "Pulls gris", new Quantifier(2, NumberedUnit.NUMBER), productLimit11, localUnit.getId());
        cloth10 = new Product(null, "Jupes bleues", new Quantifier(1, NumberedUnit.NUMBER), productLimit11, localUnit.getId());
        cloth11 = new Product(null, "Chaussures noires", new Quantifier(1, NumberedUnit.NUMBER), productLimit12, localUnit.getId());
        cloth12 = new Product(null, "Chaussures blanches", new Quantifier(1, NumberedUnit.NUMBER), productLimit12, localUnit.getId());
        cloth13 = new Product(null, "Chaussettes noires", new Quantifier(5, NumberedUnit.NUMBER), productLimit10, localUnit.getId());
        cloth14 = new Product(null, "Chaussettes blanches", new Quantifier(5, NumberedUnit.NUMBER), productLimit10, localUnit.getId());
        cloth15 = new Product(null, "Baskets rouges", new Quantifier(1, NumberedUnit.NUMBER), productLimit12, localUnit.getId());
        food1 = new Product(null, "Pommes", new WeightQuantifier(1, WeightUnit.KILOGRAM), productLimit9, localUnit.getId());
        food2 = new Product(null, "Pates", new WeightQuantifier(1, WeightUnit.KILOGRAM), productLimit7, localUnit.getId());
        food3 = new Product(null, "Bananes", new WeightQuantifier(500, WeightUnit.GRAM), productLimit9, localUnit.getId());
        food4 = new Product(null, "Oranges", new WeightQuantifier(750, WeightUnit.GRAM), productLimit9, localUnit.getId());
        food5 = new Product(null, "Tomates", new WeightQuantifier(1, WeightUnit.KILOGRAM), productLimit9, localUnit.getId());
        food6 = new Product(null, "Carottes", new WeightQuantifier(1, WeightUnit.KILOGRAM), productLimit8, localUnit.getId());
        food7 = new Product(null, "Pommes de terre", new WeightQuantifier(2, WeightUnit.KILOGRAM), productLimit7, localUnit.getId());
        food8 = new Product(null, "Fraises", new WeightQuantifier(250, WeightUnit.GRAM), productLimit9, localUnit.getId());
        food9 = new Product(null, "Blancs de poulet", new WeightQuantifier(500, WeightUnit.GRAM), productLimit13, localUnit.getId());
        food10 = new Product(null, "Filets de saumon", new WeightQuantifier(1, WeightUnit.KILOGRAM), productLimit13, localUnit.getId());
        food11 = new Product(null, "Épinards", new WeightQuantifier(200, WeightUnit.GRAM), productLimit8, localUnit.getId());
        food12 = new Product(null, "Yaourt", new WeightQuantifier(500, WeightUnit.GRAM), productLimit14, localUnit.getId());
        food13 = new Product(null, "Pain", new WeightQuantifier(800, WeightUnit.GRAM), productLimit7, localUnit.getId());
        food14 = new Product(null, "Viande hachée", new WeightQuantifier(1, WeightUnit.KILOGRAM), productLimit13, localUnit.getId());
        food15 = new Product(null, "Oignons", new WeightQuantifier(750, WeightUnit.GRAM), productLimit8, localUnit.getId());

        cloth1ESGI = new Product(null, "Chemises blanches", new Quantifier(2, NumberedUnit.NUMBER), productLimitESGI11, localUnitESGI.getId());
        cloth2ESGI = new Product(null, "Chemises blanches", new Quantifier(2, NumberedUnit.NUMBER), productLimitESGI11, localUnitESGI.getId());
        cloth3ESGI = new Product(null, "Chemises blanches", new Quantifier(2, NumberedUnit.NUMBER), productLimitESGI11, localUnitESGI.getId());
        cloth4ESGI = new Product(null, "Chemises blanches", new Quantifier(2, NumberedUnit.NUMBER), productLimitESGI11, localUnitESGI.getId());
        cloth5ESGI = new Product(null, "Chemises blanches", new Quantifier(2, NumberedUnit.NUMBER), productLimitESGI11, localUnitESGI.getId());
        cloth6ESGI = new Product(null, "T-shirts blancs", new Quantifier(2, NumberedUnit.NUMBER), productLimitESGI11, localUnitESGI.getId());
        cloth7ESGI = new Product(null, "Pantalons noirs", new Quantifier(2, NumberedUnit.NUMBER), productLimitESGI11, localUnitESGI.getId());
        cloth8ESGI = new Product(null, "Robes rouges", new Quantifier(1, NumberedUnit.NUMBER), productLimitESGI11, localUnitESGI.getId());
        cloth9ESGI = new Product(null, "Pulls gris", new Quantifier(2, NumberedUnit.NUMBER), productLimitESGI11, localUnitESGI.getId());
        cloth10ESGI = new Product(null, "Jupes bleues", new Quantifier(1, NumberedUnit.NUMBER), productLimitESGI11, localUnitESGI.getId());
        cloth11ESGI = new Product(null, "Chaussures noires", new Quantifier(1, NumberedUnit.NUMBER), productLimitESGI12, localUnitESGI.getId());
        cloth12ESGI = new Product(null, "Chaussures blanches", new Quantifier(1, NumberedUnit.NUMBER), productLimitESGI12, localUnitESGI.getId());
        cloth13ESGI = new Product(null, "Chaussettes noires", new Quantifier(5, NumberedUnit.NUMBER), productLimitESGI10, localUnitESGI.getId());
        cloth14ESGI = new Product(null, "Chaussettes blanches", new Quantifier(5, NumberedUnit.NUMBER), productLimitESGI10, localUnitESGI.getId());
        cloth15ESGI = new Product(null, "Baskets rouges", new Quantifier(1, NumberedUnit.NUMBER), productLimitESGI12, localUnitESGI.getId());
        food1ESGI = new Product(null, "Pommes", new WeightQuantifier(1, WeightUnit.KILOGRAM), productLimitESGI9, localUnitESGI.getId());
        food2ESGI = new Product(null, "Pates", new WeightQuantifier(1, WeightUnit.KILOGRAM), productLimitESGI7, localUnitESGI.getId());
        food3ESGI = new Product(null, "Bananes", new WeightQuantifier(500, WeightUnit.GRAM), productLimitESGI9, localUnitESGI.getId());
        food4ESGI = new Product(null, "Oranges", new WeightQuantifier(750, WeightUnit.GRAM), productLimitESGI9, localUnitESGI.getId());
        food5ESGI = new Product(null, "Tomates", new WeightQuantifier(1, WeightUnit.KILOGRAM), productLimitESGI9, localUnitESGI.getId());
        food6ESGI = new Product(null, "Carottes", new WeightQuantifier(1, WeightUnit.KILOGRAM), productLimitESGI8, localUnitESGI.getId());
        food7ESGI = new Product(null, "Pommes de terre", new WeightQuantifier(2, WeightUnit.KILOGRAM), productLimitESGI7, localUnitESGI.getId());
        food8ESGI = new Product(null, "Fraises", new WeightQuantifier(250, WeightUnit.GRAM), productLimitESGI9, localUnitESGI.getId());
        food9ESGI = new Product(null, "Blancs de poulet", new WeightQuantifier(500, WeightUnit.GRAM), productLimitESGI13, localUnitESGI.getId());
        food10ESGI = new Product(null, "Filets de saumon", new WeightQuantifier(1, WeightUnit.KILOGRAM), productLimitESGI13, localUnitESGI.getId());
        food11ESGI = new Product(null, "Épinards", new WeightQuantifier(200, WeightUnit.GRAM), productLimitESGI8, localUnitESGI.getId());
        food12ESGI = new Product(null, "Yaourt", new WeightQuantifier(500, WeightUnit.GRAM), productLimitESGI14, localUnitESGI.getId());
        food13ESGI = new Product(null, "Pain", new WeightQuantifier(800, WeightUnit.GRAM), productLimitESGI7, localUnitESGI.getId());
        food14ESGI = new Product(null, "Viande hachée", new WeightQuantifier(1, WeightUnit.KILOGRAM), productLimitESGI13, localUnitESGI.getId());
        food15ESGI = new Product(null, "Oignons", new WeightQuantifier(750, WeightUnit.GRAM), productLimitESGI8, localUnitESGI.getId());

        storageValOrge = new Storage(null, "Entrepot de l'unité local", localUnit, address);
        storageESGI = new Storage(null, "Placard", localUnitESGI, addressESGI);

        localUnitFixtureRepository(localUnitRepository);
        roleFixtureRepository(roleRepository);
        volunteerFixtureRepository(volunteerService);
        beneficiaryFixtureRepository(beneficiaryService);
        eventFixtureRepository(eventRepository);
        productLimitFixtureRepository(productLimitRepository);
        clothProductFixtureRepository(clothProductRepository);
        foodProductFixtureRepository(foodProductRepository);
        storageFixtureRepository(storageRepository);
        storageUserProductFixtureRepository(storageUserProductRepository);
        storageProductFixtureRepository(storageProductRepository);
        this.userDBRepository = userDBRepository;
    }

    public void localUnitFixtureRepository(LocalUnitRepository localUnitRepository) {
        localUnitRepository.save(localUnit);
        localUnitRepository.save(localUnitESGI);
    }

    public void roleFixtureRepository(RoleRepository inDBRoleRepository) {
        inDBRoleRepository.save(managerRoleValOrge);
        inDBRoleRepository.save(defaultRoleValOrge);
        inDBRoleRepository.save(beneficiaryRoleValOrge);
        inDBRoleRepository.save(managerRoleESGI);
        inDBRoleRepository.save(defaultRoleESGI);
        inDBRoleRepository.save(beneficiaryRoleESGI);
    }

    public void volunteerFixtureRepository(VolunteerService volunteerService) {
        for (Volunteer volunteer : List.of(volunteerManagerValOrge, volunteerValOrge1, volunteerValOrge2, volunteerValOrge3, volunteerValOrge4, volunteerValOrge5, volunteerValOrge6, volunteerValOrge7, volunteerValOrge8, volunteerValOrge9, volunteerValOrge10, volunteerManagerESGI, volunteerESGI1, volunteerESGI2, volunteerESGI3, volunteerESGI4, volunteerESGI5)) {
            var id = volunteerService.saveWithoutEmailSend(volunteer);
            volunteer.setId(id);
            volunteer.getUser().setId(id);
        }
    }

    public void beneficiaryFixtureRepository(BeneficiaryService beneficiaryService) {
        for (Beneficiary beneficiary : List.of(beneficiary1, beneficiary2, beneficiary3, beneficiary4, beneficiary5, beneficiary6, beneficiary7, beneficiary8, beneficiary9, beneficiary10, beneficiary11, beneficiary12, beneficiary13, beneficiary14, beneficiary15, beneficiary16, beneficiary17, beneficiary18, beneficiary19, beneficiary20, beneficiary21, beneficiary22, beneficiary23, beneficiary24, beneficiary25, beneficiary26, beneficiary27, beneficiary28, beneficiary29, beneficiary30, beneficiary31, beneficiary32, beneficiary33, beneficiary34, beneficiary35, beneficiary36, beneficiary37, beneficiary38, beneficiary39, beneficiary40, beneficiary41, beneficiary42, beneficiary43, beneficiary44, beneficiary45, beneficiary46, beneficiary47, beneficiaryESGI1, beneficiaryESGI2, beneficiaryESGI3, beneficiaryESGI4, beneficiaryESGI5, beneficiaryESGI6, beneficiaryESGI7, beneficiaryESGI8, beneficiaryESGI9, beneficiaryESGI10)) {
            var id = beneficiaryService.saveWithoutEmailSend(beneficiary);
            beneficiary.setId(id);
            beneficiary.getUser().setId(id);
        }
    }

    public void eventFixtureRepository(EventRepository eventRepository) {
        List<Beneficiary> userBeneficiariesInDB = List.of(beneficiary1, beneficiary2, beneficiary3, beneficiary4, beneficiary5, beneficiary6, beneficiary7, beneficiary8, beneficiary9, beneficiary10, beneficiary11, beneficiary12, beneficiary13, beneficiary14, beneficiary15, beneficiary16, beneficiary17, beneficiary18, beneficiary19, beneficiary20, beneficiary21, beneficiary22, beneficiary23, beneficiary24, beneficiary25, beneficiary26, beneficiary27, beneficiary28, beneficiary29, beneficiary30, beneficiary31, beneficiary32, beneficiary33, beneficiary34, beneficiary35, beneficiary36, beneficiary37, beneficiary38, beneficiary39, beneficiary40, beneficiary41, beneficiary42, beneficiary43, beneficiary44, beneficiary45, beneficiary46, beneficiary47);
        ZonedDateTime eventLimit = ZonedDateTime.of(LocalDateTime.of(2023, 8, 15, 0, 0), ZoneId.of("Europe/Paris"));

        ZonedDateTime eventStart1 = ZonedDateTime.of(LocalDateTime.of(2023, 4, 1, 10, 0), ZoneId.of("Europe/Paris"));
        List<EventSession> eventSessions1 = new ArrayList<>();
        for (; eventStart1.isBefore(ZonedDateTime.of(LocalDateTime.of(2023, 9, 1, 0, 0), ZoneId.of("Europe/Paris"))); eventStart1 = eventStart1.plusDays(14)) {
            eventSessions1.add(new EventSession(null, List.of(new EventTimeWindow(null, eventStart1, eventStart1.plusHours(8), 0, new ArrayList<>()))));
        }
        Event event1 = new Event(null, "Formation PSC1", "Acquérir les compétences essentielles pour intervenir en cas d'urgence et sauver des vies ! Formation PSC1, une session interactive dispensée par des formateurs certifiés. Apprendre les gestes de premiers secours tels que la réanimation, la position latérale de sécurité et le traitement des hémorragies. Places limitées à 12 personnes. Les stagiaires obtiennent une attestation officielle reconnue nationalement.", volunteerValOrge2, localUnit, eventSessions1, eventSessions1.size());
        eventRepository.save(event1);

        ZonedDateTime eventStartDate2 = ZonedDateTime.of(LocalDateTime.of(2023, 4, 2, 14, 0), ZoneId.of("Europe/Paris"));
        List<EventSession> eventSessions2 = new ArrayList<>();
        for (; eventStartDate2.isBefore(ZonedDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0), ZoneId.of("Europe/Paris"))); eventStartDate2 = eventStartDate2.plusDays(7)) {
            List<EventTimeWindow> eventTimeWindowList2 = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                List<ID> participants = new ArrayList<>();
                int numberOfParticipants = 0;
                if (eventStartDate2.isBefore(eventLimit)) {
                    numberOfParticipants = new Random().nextInt(4);
                }
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

        ZonedDateTime eventStartDate3 = ZonedDateTime.of(LocalDateTime.of(2023, 4, 3, 16, 0), ZoneId.of("Europe/Paris"));
        List<EventSession> eventSessions3 = new ArrayList<>();
        for (; eventStartDate3.isBefore(ZonedDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0), ZoneId.of("Europe/Paris"))); eventStartDate3 = eventStartDate3.plusDays(7)) {
            List<EventTimeWindow> eventTimeWindowList3 = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                List<ID> participants = new ArrayList<>();
                int numberOfParticipants = 0;
                if (eventStartDate3.isBefore(eventLimit)) {
                    numberOfParticipants = new Random().nextInt(6);
                }
                for (int j = 0; j < numberOfParticipants; j++) {
                    ID randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
                    while (participants.contains(randomBeneficiaryId) || eventTimeWindowList3.stream().map(EventTimeWindow::getParticipants).flatMap(Collection::stream).toList().contains(randomBeneficiaryId)) {
                        randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
                    }
                    participants.add(randomBeneficiaryId);

                    ID finalRandomBeneficiaryId = randomBeneficiaryId;
                    var beneficiary = userBeneficiariesInDB.stream().filter(b -> b.getId().equals(finalRandomBeneficiaryId)).findFirst().orElseThrow();
                    if (beneficiaryClothProductDates.containsKey(beneficiary)) {
                        beneficiaryClothProductDates.get(beneficiary).add(eventStartDate3.plusMinutes(i * 30).toLocalDateTime());
                    } else {
                        beneficiaryClothProductDates.put(beneficiary, new ArrayList<>(List.of(eventStartDate3.plusMinutes(i * 30).toLocalDateTime())));
                    }
                }
                eventTimeWindowList3.add(new EventTimeWindow(null, eventStartDate3.plusMinutes(i * 30), eventStartDate3.plusMinutes((i + 1) * 30), 6, participants));
            }
            eventSessions3.add(new EventSession(null, eventTimeWindowList3));
        }
        Event event3 = new Event(null, "EPISOL Lundi", "Chaque semaine, nous ouvrons l'épicerie sociale aux personnes dans le besoin. Distribution sur rendez-vous.", volunteerValOrge1, localUnit, eventSessions3, eventSessions3.size());
        eventRepository.save(event3);

        ZonedDateTime eventStartDate4 = ZonedDateTime.of(LocalDateTime.of(2023, 4, 5, 16, 0), ZoneId.of("Europe/Paris"));
        List<EventSession> eventSessions4 = new ArrayList<>();
        for (; eventStartDate4.isBefore(ZonedDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0), ZoneId.of("Europe/Paris"))); eventStartDate4 = eventStartDate4.plusDays(7)) {
            List<EventTimeWindow> eventTimeWindowList4 = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                List<ID> participants = new ArrayList<>();
                int numberOfParticipants = 0;
                if (eventStartDate4.isBefore(eventLimit)) {
                    numberOfParticipants = new Random().nextInt(6);
                }
                for (int j = 0; j < numberOfParticipants; j++) {
                    ID randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
                    while (participants.contains(randomBeneficiaryId) || eventTimeWindowList4.stream().map(EventTimeWindow::getParticipants).flatMap(Collection::stream).toList().contains(randomBeneficiaryId)) {
                        randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
                    }
                    participants.add(randomBeneficiaryId);

                    ID finalRandomBeneficiaryId = randomBeneficiaryId;
                    var beneficiary = userBeneficiariesInDB.stream().filter(b -> b.getId().equals(finalRandomBeneficiaryId)).findFirst().orElseThrow();
                    if (beneficiaryFoodProductDates.containsKey(beneficiary)) {
                        beneficiaryFoodProductDates.get(beneficiary).add(eventStartDate3.plusMinutes(i * 30).toLocalDateTime());
                    } else {
                        beneficiaryFoodProductDates.put(beneficiary, new ArrayList<>(List.of(eventStartDate3.plusMinutes(i * 30).toLocalDateTime())));
                    }
                }
                eventTimeWindowList4.add(new EventTimeWindow(null, eventStartDate4.plusMinutes(i * 30), eventStartDate4.plusMinutes((i + 1) * 30), 6, participants));
            }
            eventSessions4.add(new EventSession(null, eventTimeWindowList4));
        }
        Event event4 = new Event(null, "EPISOL Mercredi", "Chaque semaine, nous ouvrons l'épicerie sociale aux personnes dans le besoin. Distribution sur rendez-vous.", volunteerValOrge1, localUnit, eventSessions4, eventSessions4.size());
        eventRepository.save(event4);

        ZonedDateTime eventStartDate5 = ZonedDateTime.of(LocalDateTime.of(2023, 4, 7, 16, 0), ZoneId.of("Europe/Paris"));
        List<EventSession> eventSessions5 = new ArrayList<>();
        for (; eventStartDate5.isBefore(ZonedDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0), ZoneId.of("Europe/Paris"))); eventStartDate5 = eventStartDate5.plusDays(7)) {
            List<EventTimeWindow> eventTimeWindowList5 = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                List<ID> participants = new ArrayList<>();
                int numberOfParticipants = 0;
                if (eventStartDate5.isBefore(eventLimit)) {
                    numberOfParticipants = new Random().nextInt(6);
                }
                for (int j = 0; j < numberOfParticipants; j++) {
                    ID randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
                    while (participants.contains(randomBeneficiaryId) || eventTimeWindowList5.stream().map(EventTimeWindow::getParticipants).flatMap(Collection::stream).toList().contains(randomBeneficiaryId)) {
                        randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
                    }
                    participants.add(randomBeneficiaryId);

                    ID finalRandomBeneficiaryId = randomBeneficiaryId;
                    var beneficiary = userBeneficiariesInDB.stream().filter(b -> b.getId().equals(finalRandomBeneficiaryId)).findFirst().orElseThrow();
                    if (beneficiaryFoodProductDates.containsKey(beneficiary)) {
                        beneficiaryFoodProductDates.get(beneficiary).add(eventStartDate3.plusMinutes(i * 30).toLocalDateTime());
                    } else {
                        beneficiaryFoodProductDates.put(beneficiary, new ArrayList<>(List.of(eventStartDate3.plusMinutes(i * 30).toLocalDateTime())));
                    }
                }
                eventTimeWindowList5.add(new EventTimeWindow(null, eventStartDate5.plusMinutes(i * 30), eventStartDate5.plusMinutes((i + 1) * 30), 6, participants));
            }
            eventSessions5.add(new EventSession(null, eventTimeWindowList5));
        }
        Event event5 = new Event(null, "EPISOL Vendredi", "Chaque semaine, nous ouvrons l'épicerie sociale aux personnes dans le besoin. Distribution sur rendez-vous.", volunteerValOrge1, localUnit, eventSessions5, eventSessions5.size());
        eventRepository.save(event5);

        ZonedDateTime eventStartDate6 = ZonedDateTime.of(LocalDateTime.of(2023, 4, 6, 10, 0), ZoneId.of("Europe/Paris"));
        List<EventSession> eventSessions6 = new ArrayList<>();
        for (; eventStartDate6.isBefore(ZonedDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0), ZoneId.of("Europe/Paris"))); eventStartDate6 = eventStartDate6.plusDays(7)) {
            List<EventTimeWindow> eventTimeWindowList6 = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                List<ID> participants = new ArrayList<>();
                int numberOfParticipants = 0;
                if (eventStartDate6.isBefore(eventLimit)) {
                    numberOfParticipants = new Random().nextInt(5);
                }
                for (int j = 0; j < numberOfParticipants; j++) {
                    ID randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
                    while (participants.contains(randomBeneficiaryId) || eventTimeWindowList6.stream().map(EventTimeWindow::getParticipants).flatMap(Collection::stream).toList().contains(randomBeneficiaryId)) {
                        randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
                    }
                    participants.add(randomBeneficiaryId);

                    ID finalRandomBeneficiaryId = randomBeneficiaryId;
                    var beneficiary = userBeneficiariesInDB.stream().filter(b -> b.getId().equals(finalRandomBeneficiaryId)).findFirst().orElseThrow();
                    if (beneficiaryFoodProductDates.containsKey(beneficiary)) {
                        beneficiaryFoodProductDates.get(beneficiary).add(eventStartDate3.plusMinutes(i * 30).toLocalDateTime());
                    } else {
                        beneficiaryFoodProductDates.put(beneficiary, new ArrayList<>(List.of(eventStartDate3.plusMinutes(i * 30).toLocalDateTime())));
                    }
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


        ZonedDateTime eventStartDateESGI = ZonedDateTime.of(LocalDateTime.of(2023, 5, 15, 16, 0), ZoneId.of("Europe/Paris"));
        List<EventSession> eventSessionsESGI = new ArrayList<>();
        for (; eventStartDateESGI.isBefore(ZonedDateTime.of(LocalDateTime.of(2023, 9, 1, 0, 0), ZoneId.of("Europe/Paris"))); eventStartDateESGI = eventStartDateESGI.plusDays(1)) {
            List<EventTimeWindow> eventTimeWindowListESGI = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                List<ID> participantsESGI = new ArrayList<>();
                int numberOfParticipants = 0;
                if (eventStartDateESGI.isBefore(eventLimit)) {
                    numberOfParticipants = new Random().nextInt(4);
                }
                for (int j = 0; j < numberOfParticipants; j++) {
                    ID randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
                    while (participants.contains(randomBeneficiaryId) || eventTimeWindowListESGI.stream().map(EventTimeWindow::getParticipants).flatMap(Collection::stream).toList().contains(randomBeneficiaryId)) {
                        randomBeneficiaryId = userBeneficiariesInDB.get(new Random().nextInt(userBeneficiariesInDB.size())).getId();
                    }
                    participantsESGI.add(randomBeneficiaryId);

                    ID finalRandomBeneficiaryId = randomBeneficiaryId;
                    var beneficiary = userBeneficiariesInDB.stream().filter(b -> b.getId().equals(finalRandomBeneficiaryId)).findFirst().orElseThrow();
                    if (beneficiaryFoodProductDatesESGI.containsKey(beneficiary)) {
                        beneficiaryFoodProductDatesESGI.get(beneficiary).add(eventStartDate3.plusMinutes(i * 30).toLocalDateTime());
                    } else {
                        beneficiaryFoodProductDatesESGI.put(beneficiary, new ArrayList<>(List.of(eventStartDate3.plusMinutes(i * 30).toLocalDateTime())));
                    }
                }
                eventTimeWindowListESGI.add(new EventTimeWindow(null, eventStartDateESGI.plusMinutes(i * 15), eventStartDateESGI.plusMinutes((i + 1) * 15), 4, participantsESGI));
            }
            eventSessionsESGI.add(new EventSession(null, eventTimeWindowListESGI));
        }
        Event eventESGI = new Event(null, "Gouter", "Gouter pour les volontaires de l'ESGI", volunteerESGI1, localUnitESGI, eventSessionsESGI, eventSessionsESGI.size());
        eventRepository.save(eventESGI);
    }

    public void productLimitFixtureRepository(ProductLimitRepository repo) {
        for (var productLimit : productLimits) {
            repo.save(productLimit);
        }
    }

    public void clothProductFixtureRepository(ClothProductRepository repository) {
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
        repository.save(new ClothProduct(new ID(16L), cloth1ESGI, ClothSize.S, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(17L), cloth2ESGI, ClothSize.M, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(18L), cloth3ESGI, ClothSize.L, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(19L), cloth4ESGI, ClothSize.XL, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(20L), cloth5ESGI, ClothSize.XXL, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(21L), cloth6ESGI, ClothSize.S, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(22L), cloth7ESGI, ClothSize.M, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(23L), cloth8ESGI, ClothSize.L, ClothGender.FEMALE));
        repository.save(new ClothProduct(new ID(24L), cloth9ESGI, ClothSize.CHILD, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(25L), cloth10ESGI, ClothSize.XXL, ClothGender.FEMALE));
        repository.save(new ClothProduct(new ID(26L), cloth11ESGI, ClothSize.UNKNOWN, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(27L), cloth12ESGI, ClothSize.UNKNOWN, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(28L), cloth13ESGI, ClothSize.CHILD, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(29L), cloth14ESGI, ClothSize.XL, ClothGender.NOT_SPECIFIED));
        repository.save(new ClothProduct(new ID(30L), cloth15ESGI, ClothSize.UNKNOWN, ClothGender.NOT_SPECIFIED));
    }

    public void foodProductFixtureRepository(FoodProductRepository repository) {
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

        repository.save(new FoodProduct(null,
                food1ESGI,
                FoodConservation.ROOM_TEMPERATURE,
                ZonedDateTime.of(LocalDateTime.of(date.plusMonths(2).getYear(), date.plusMonths(2).getMonthValue(), date.plusMonths(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusMonths(1).getYear(), date.plusMonths(1).getMonthValue(), date.plusMonths(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                42));

        repository.save(new FoodProduct(null,
                food2ESGI,
                FoodConservation.ROOM_TEMPERATURE,
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(5).getYear(), date.plusDays(5).getMonthValue(), date.plusDays(5).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                27));

        repository.save(new FoodProduct(null,
                food3ESGI,
                FoodConservation.ROOM_TEMPERATURE,
                ZonedDateTime.of(LocalDateTime.of(date.plusMonths(1).getYear(), date.plusMonths(1).getMonthValue(), date.plusMonths(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusWeeks(2).getYear(), date.plusWeeks(2).getMonthValue(), date.plusWeeks(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                37));

        repository.save(new FoodProduct(null,
                food4ESGI,
                FoodConservation.FROZEN,
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(2).getYear(), date.plusDays(2).getMonthValue(), date.plusDays(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(1).getYear(), date.plusDays(1).getMonthValue(), date.plusDays(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                59));

        repository.save(new FoodProduct(null,
                food5ESGI,
                FoodConservation.REFRIGERATED,
                ZonedDateTime.of(LocalDateTime.of(date.plusWeeks(2).getYear(), date.plusWeeks(2).getMonthValue(), date.plusWeeks(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusWeeks(1).getYear(), date.plusWeeks(1).getMonthValue(), date.plusWeeks(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                41));

        repository.save(new FoodProduct(null,
                food6ESGI,
                FoodConservation.ROOM_TEMPERATURE,
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(4).getYear(), date.plusDays(4).getMonthValue(), date.plusDays(4).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(3).getYear(), date.plusDays(3).getMonthValue(), date.plusDays(3).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                56));

        repository.save(new FoodProduct(null,
                food7ESGI,
                FoodConservation.FROZEN,
                ZonedDateTime.of(LocalDateTime.of(date.plusWeeks(1).getYear(), date.plusWeeks(1).getMonthValue(), date.plusWeeks(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(5).getYear(), date.plusDays(5).getMonthValue(), date.plusDays(5).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                80));

        repository.save(new FoodProduct(null,
                food8ESGI,
                FoodConservation.REFRIGERATED,
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(3).getYear(), date.plusDays(3).getMonthValue(), date.plusDays(3).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(2).getYear(), date.plusDays(2).getMonthValue(), date.plusDays(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                71));

        repository.save(new FoodProduct(null,
                food9ESGI,
                FoodConservation.ROOM_TEMPERATURE,
                ZonedDateTime.of(LocalDateTime.of(date.plusMonths(1).getYear(), date.plusMonths(1).getMonthValue(), date.plusMonths(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusWeeks(2).getYear(), date.plusWeeks(2).getMonthValue(), date.plusWeeks(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                63));


        repository.save(new FoodProduct(null,
                food10ESGI,
                FoodConservation.FROZEN,
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(2).getYear(), date.plusDays(2).getMonthValue(), date.plusDays(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(1).getYear(), date.plusDays(1).getMonthValue(), date.plusDays(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                127));

        repository.save(new FoodProduct(null,
                food11ESGI,
                FoodConservation.REFRIGERATED,
                ZonedDateTime.of(LocalDateTime.of(date.plusWeeks(2).getYear(), date.plusWeeks(2).getMonthValue(), date.plusWeeks(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusWeeks(1).getYear(), date.plusWeeks(1).getMonthValue(), date.plusWeeks(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                11));

        repository.save(new FoodProduct(null,
                food12ESGI,
                FoodConservation.ROOM_TEMPERATURE,
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(4).getYear(), date.plusDays(4).getMonthValue(), date.plusDays(4).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(3).getYear(), date.plusDays(3).getMonthValue(), date.plusDays(3).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                35));

        repository.save(new FoodProduct(null,
                food13ESGI,
                FoodConservation.FROZEN,
                ZonedDateTime.of(LocalDateTime.of(date.plusWeeks(1).getYear(), date.plusWeeks(1).getMonthValue(), date.plusWeeks(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(5).getYear(), date.plusDays(5).getMonthValue(), date.plusDays(5).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                25));

        repository.save(new FoodProduct(null,
                food14ESGI,
                FoodConservation.REFRIGERATED,
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(3).getYear(), date.plusDays(3).getMonthValue(), date.plusDays(3).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(2).getYear(), date.plusDays(2).getMonthValue(), date.plusDays(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                134));

        repository.save(new FoodProduct(null,
                food15ESGI,
                FoodConservation.ROOM_TEMPERATURE,
                ZonedDateTime.of(LocalDateTime.of(date.plusMonths(1).getYear(), date.plusMonths(1).getMonthValue(), date.plusMonths(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusWeeks(2).getYear(), date.plusWeeks(2).getMonthValue(), date.plusWeeks(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                91));
    }

    public void storageFixtureRepository(StorageRepository storageRepository) {
        storageRepository.save(storageValOrge);
        storageRepository.save(storageESGI);
        storageRepository.save(new Storage(null, "Box de l'unité locale", localUnit, address));
    }

    public void storageUserProductFixtureRepository(BeneficiaryProductRepository repo) {
        Random random = new Random();

        var foodProductList = List.of(food1, food2, food3, food4, food5, food6, food7, food8, food9, food10, food11, food12, food13, food14, food15);
        var clothProductList = List.of(cloth1, cloth2, cloth3, cloth4, cloth5, cloth6, cloth7, cloth8, cloth9, cloth10, cloth11, cloth12, cloth13, cloth14, cloth15);

        fillBeneficiaryProductWithRandom(repo, random, foodProductList, beneficiaryFoodProductDates, storageValOrge);
        fillBeneficiaryProductWithRandom(repo, random, clothProductList, beneficiaryClothProductDates, storageValOrge);

        var foodProductListESGI = List.of(food1ESGI, food2ESGI, food3ESGI, food4ESGI, food5ESGI, food6ESGI, food7ESGI, food8ESGI, food9ESGI, food10ESGI, food11ESGI, food12ESGI, food13ESGI, food14ESGI, food15ESGI);
        var clothProductListESGI = List.of(cloth1ESGI, cloth2ESGI, cloth3ESGI, cloth4ESGI, cloth5ESGI, cloth6ESGI, cloth7ESGI, cloth8ESGI, cloth9ESGI, cloth10ESGI, cloth11ESGI, cloth12ESGI, cloth13ESGI, cloth14ESGI, cloth15ESGI);

        fillBeneficiaryProductWithRandom(repo, random, foodProductListESGI, beneficiaryFoodProductDatesESGI, storageESGI);
        fillBeneficiaryProductWithRandom(repo, random, clothProductListESGI, beneficiaryClothProductDatesESGI, storageESGI);
    }

    private void fillBeneficiaryProductWithRandom(BeneficiaryProductRepository repo, Random random, List<Product> foodProductList, Map<Beneficiary, List<LocalDateTime>> beneficiaryFoodProductDates, Storage storage) {
        for (var entry : beneficiaryFoodProductDates.entrySet()) {
            for (var date : entry.getValue()) {
                if (date.isAfter(LocalDateTime.now()))
                    continue;

                for (int i = 0; i < random.nextInt((int) (foodProductList.size() * 0.75)); i++) {
                    repo.save(
                            new BeneficiaryProduct(null,
                                    entry.getKey(),
                                    foodProductList.get(random.nextInt(foodProductList.size())),
                                    storage,
                                    date,
                                    random.nextInt(5))
                    );
                }
            }
        }
    }

    public void storageProductFixtureRepository(StorageProductRepository storageProductRepository) {
        var random = new Random();

        for(var product : List.of(cloth1, cloth2, cloth3, cloth4, cloth5, cloth6, cloth7, cloth8, cloth9, cloth10, cloth11, cloth12, cloth13, cloth14, cloth15,
                food1, food2, food3, food4, food5, food6, food7, food8, food9, food10, food11, food12, food13, food14, food15) ) {
            storageProductRepository.save(new StorageProduct(null, storageValOrge, product, random.nextInt(5, 50)));
        }
        for(var product : List.of(cloth1ESGI, cloth2ESGI, cloth3ESGI, cloth4ESGI, cloth5ESGI, cloth6ESGI, cloth7ESGI, cloth8ESGI, cloth9ESGI, cloth10ESGI, cloth11ESGI, cloth12ESGI, cloth13ESGI, cloth14ESGI, cloth15ESGI,
                food1ESGI, food2ESGI, food3ESGI, food4ESGI, food5ESGI, food6ESGI, food7ESGI, food8ESGI, food9ESGI, food10ESGI, food11ESGI, food12ESGI, food13ESGI, food14ESGI, food15ESGI) ) {
            storageProductRepository.save(new StorageProduct(null, storageESGI, product, random.nextInt(5, 50)));
        }
    }
}
