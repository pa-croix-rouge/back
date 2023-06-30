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
import fr.croixrouge.repository.db.storage_product.InDBStorageProductRepository;
import fr.croixrouge.repository.db.storage_product.StorageProductDBRepository;
import fr.croixrouge.repository.db.user.InDBUserRepository;
import fr.croixrouge.repository.db.user.UserDBRepository;
import fr.croixrouge.repository.db.user_product.InDBBeneficiaryProductRepository;
import fr.croixrouge.repository.db.user_product.UserProductDBRepository;
import fr.croixrouge.repository.db.volunteer.InDBVolunteerRepository;
import fr.croixrouge.repository.db.volunteer.VolunteerDBRepository;
import fr.croixrouge.storage.repository.BeneficiaryProductRepository;
import fr.croixrouge.storage.repository.StorageProductRepository;
import fr.croixrouge.storage.service.StorageProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class RepositoryConfig {

    @Bean
    @Primary
    public InDBLocalUnitRepository localTestInDBUnitRepository(LocalUnitDBRepository localUnitDBRepository) {
        return new InDBLocalUnitRepository(localUnitDBRepository);
    }

    @Bean
    @Primary
    public InDBRoleRepository roleTestRepository(RoleDBRepository roleDBRepository, RoleResourceDBRepository roleResourceDBRepository, InDBLocalUnitRepository localUnitDBRepository) {
        return new InDBRoleRepository(roleDBRepository, roleResourceDBRepository, localUnitDBRepository);
    }

    @Bean
    @Primary
    public InDBUserRepository userTestRepository(UserDBRepository userDBRepository, InDBRoleRepository roleDBRepository, InDBLocalUnitRepository inDBLocalUnitRepository) {
        return new InDBUserRepository(userDBRepository, roleDBRepository, inDBLocalUnitRepository);
    }

    @Bean
    @Primary
    public InDBVolunteerRepository volunteerTestRepository(VolunteerDBRepository volunteerDBRepository, UserDBRepository userDBRepository, InDBUserRepository inDBUserRepository) {
        return new InDBVolunteerRepository(volunteerDBRepository, userDBRepository, inDBUserRepository);
    }

    @Bean
    @Primary
    public EventRepository eventTestRepository(EventDBRepository eventDBRepository, EventSessionDBRepository eventSessionDBRepository, EventTimeWindowDBRepository eventTimeWindowDBRepository, InDBUserRepository userDBRepository, InDBVolunteerRepository inDBVolunteerRepository, InDBLocalUnitRepository inDBLocalUnitRepository) {
        return new InDBEventRepository(eventDBRepository, eventSessionDBRepository, eventTimeWindowDBRepository, userDBRepository, inDBVolunteerRepository, inDBLocalUnitRepository);
    }


    @Bean
    @Primary
    public InDBProductRepository productTestRepository(ProductDBRepository productDBRepository, InDBProductLimitRepository inDBProductLimitRepository) {
        return new InDBProductRepository(productDBRepository, inDBProductLimitRepository);
    }

    @Bean
    @Primary
    public InDBProductLimitRepository productLimitTestRepository(ProductLimitDBRepository productLimitDBRepository) {
        return new InDBProductLimitRepository(productLimitDBRepository);
    }

    @Bean
    @Primary
    public InDBClothProductRepository clothProductRepository(ClothProductDBRepository clothProductDBRepository, InDBProductRepository productRepository) {
        return new InDBClothProductRepository(clothProductDBRepository, productRepository);
    }

    @Bean
    @Primary
    public InDBFoodProductRepository foodProductTestRepository(FoodProductDBRepository foodProductDBRepository, InDBProductRepository productRepository) {
        return new InDBFoodProductRepository(foodProductDBRepository, productRepository);
    }

    @Bean
    @Primary
    public InDBBeneficiaryRepository beneficiaryRepository(BeneficiaryDBRepository beneficiaryDBRepository, FamilyMemberDBRepository familyMemberDBRepository, UserDBRepository userDBRepository, InDBUserRepository inDBUserRepository) {
        return new InDBBeneficiaryRepository(beneficiaryDBRepository, familyMemberDBRepository, userDBRepository, inDBUserRepository);
    }

    @Bean
    @Primary
    public InDBStorageRepository storageTestRepository(StorageDBRepository storageDBRepository, InDBLocalUnitRepository inDBLocalUnitRepository) {
        return new InDBStorageRepository(storageDBRepository, inDBLocalUnitRepository);
    }

    @Bean
    public BeneficiaryProductRepository storageUserProductRepository(UserProductDBRepository userProductDBRepository, InDBBeneficiaryRepository beneficiaryRepository, InDBProductRepository productRepository, InDBStorageRepository storageRepository) {
        return new InDBBeneficiaryProductRepository(userProductDBRepository, beneficiaryRepository, productRepository, storageRepository);
    }

    @Bean
    public StorageProductRepository storageProductRepository(StorageProductDBRepository storageProductDBRepository, InDBProductRepository productRepository, InDBStorageRepository storageRepository) {
        return new InDBStorageProductRepository(storageProductDBRepository, productRepository, storageRepository);
    }

    @Bean
    public StorageProductService storageProductServiceCore(StorageProductRepository storageProductRepository) {
        return new StorageProductService(storageProductRepository);
    }

}
