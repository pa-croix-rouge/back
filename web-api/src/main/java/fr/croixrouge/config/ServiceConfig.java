package fr.croixrouge.config;

import fr.croixrouge.repository.db.beneficiary.InDBBeneficiaryRepository;
import fr.croixrouge.repository.db.product.InDBProductRepository;
import fr.croixrouge.repository.db.storage.InDBStorageRepository;
import fr.croixrouge.repository.db.storage_product.InDBStorageProductRepository;
import fr.croixrouge.repository.db.storage_product.StorageProductDBRepository;
import fr.croixrouge.repository.db.user_product.InDBBeneficiaryProductRepository;
import fr.croixrouge.repository.db.user_product.UserProductDBRepository;
import fr.croixrouge.storage.repository.BeneficiaryProductRepository;
import fr.croixrouge.storage.repository.StorageProductRepository;
import fr.croixrouge.storage.service.StorageProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

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


