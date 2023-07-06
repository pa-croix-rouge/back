package fr.croixrouge.config;

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
import fr.croixrouge.repository.db.user.InDBUserRepository;
import fr.croixrouge.repository.db.user.UserDBRepository;
import fr.croixrouge.repository.db.volunteer.InDBVolunteerRepository;
import fr.croixrouge.repository.db.volunteer.VolunteerDBRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"prod", "fixtures-prod"})
public class RepositoryConfig {

    @Bean
    @Primary
    public InDBLocalUnitRepository localUnitRepository(LocalUnitDBRepository localUnitDBRepository) {
        return new InDBLocalUnitRepository(localUnitDBRepository);
    }

    @Bean
    @Primary
    public InDBRoleRepository roleRepository(RoleDBRepository roleDBRepository, RoleResourceDBRepository roleResourceDBRepository, InDBLocalUnitRepository localUnitDBRepository) {
        return new InDBRoleRepository(roleDBRepository, roleResourceDBRepository, localUnitDBRepository);
}

    @Bean
    @Primary
    public InDBUserRepository userRepository(UserDBRepository userDBRepository, InDBRoleRepository roleDBRepository, InDBLocalUnitRepository inDBLocalUnitRepository) {
        return new InDBUserRepository(userDBRepository, roleDBRepository, inDBLocalUnitRepository);
    }

    @Bean
    @Primary
    public InDBVolunteerRepository volunteerRepository(VolunteerDBRepository volunteerDBRepository, UserDBRepository userDBRepository, InDBUserRepository inDBUserRepository) {
        return new InDBVolunteerRepository(volunteerDBRepository, userDBRepository, inDBUserRepository);
    }

    @Bean
    @Primary
    public EventRepository eventRepository(EventDBRepository eventDBRepository, EventSessionDBRepository eventSessionDBRepository, EventTimeWindowDBRepository eventTimeWindowDBRepository, InDBUserRepository userDBRepository, InDBVolunteerRepository inDBVolunteerRepository, InDBLocalUnitRepository inDBLocalUnitRepository) {
        return new InDBEventRepository(eventDBRepository, eventSessionDBRepository, eventTimeWindowDBRepository, userDBRepository, inDBVolunteerRepository, inDBLocalUnitRepository);
    }


    @Bean
    @Primary
    public InDBProductRepository productRepository(ProductDBRepository productDBRepository, InDBProductLimitRepository inDBProductLimitRepository, LocalUnitDBRepository inDBLocalUnitRepository) {
        return new InDBProductRepository(productDBRepository, inDBProductLimitRepository, inDBLocalUnitRepository);
    }

    @Bean
    @Primary
    public InDBProductLimitRepository productLimitRepository(ProductLimitDBRepository productLimitDBRepository, LocalUnitDBRepository inDBLocalUnitRepository) {
        return new InDBProductLimitRepository(productLimitDBRepository, inDBLocalUnitRepository);
    }

    @Bean
    @Primary
    public InDBClothProductRepository clothProductRepository(ClothProductDBRepository clothProductDBRepository, InDBProductRepository productRepository) {
        return new InDBClothProductRepository(clothProductDBRepository, productRepository);
    }

    @Bean
    @Primary
    public InDBFoodProductRepository foodProductRepository(FoodProductDBRepository foodProductDBRepository, InDBProductRepository productRepository) {
        return new InDBFoodProductRepository(foodProductDBRepository, productRepository);
    }

    @Bean
    @Primary
    public InDBBeneficiaryRepository beneficiaryRepository(BeneficiaryDBRepository beneficiaryDBRepository, FamilyMemberDBRepository familyMemberDBRepository, UserDBRepository userDBRepository, InDBUserRepository inDBUserRepository) {
        return new InDBBeneficiaryRepository(beneficiaryDBRepository, familyMemberDBRepository, userDBRepository, inDBUserRepository);
    }

    @Bean
    @Primary
    public InDBStorageRepository storageRepository(StorageDBRepository storageDBRepository, InDBLocalUnitRepository inDBLocalUnitRepository) {
        return new InDBStorageRepository(storageDBRepository, inDBLocalUnitRepository);
    }

}
