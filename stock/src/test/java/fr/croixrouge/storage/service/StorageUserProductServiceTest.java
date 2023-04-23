package fr.croixrouge.storage.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.model.product.ProductLimit;
import fr.croixrouge.storage.model.quantifier.WeightQuantifier;
import fr.croixrouge.storage.model.quantifier.WeightUnit;
import fr.croixrouge.storage.repository.ProductRepository;
import fr.croixrouge.storage.repository.StorageProductRepository;
import fr.croixrouge.storage.repository.StorageRepository;
import fr.croixrouge.storage.repository.StorageUserProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryStorageProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryStorageRepository;
import fr.croixrouge.storage.repository.memory.InMemoryStorageUserProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StorageUserProductServiceTest {

    StorageRepository storageRepository = new InMemoryStorageRepository();

    ProductRepository productRepository = new InMemoryProductRepository();

    StorageProductRepository storageProductRepository = new InMemoryStorageProductRepository();
    StorageUserProductRepository storageUserProductRepository = new InMemoryStorageUserProductRepository();

    Storage storage = new Storage(new ID("1"), null, null);

    private final StorageProductService storageProductService = new StorageProductService(storageRepository, productRepository, storageProductRepository);

    private final StorageUserProductService storageUserProductService = new StorageUserProductService(storageUserProductRepository, storageProductService);

    private final User user = new User("1", "TEST", "TEST", List.of());

    private final ProductLimit limit1KgFor7Days = new ProductLimit(Duration.ofDays(7), new WeightQuantifier(1, WeightUnit.KILOGRAM));
    private final Product productWeight1KgNoLimit = new Product(new ID("1"), "pr", new WeightQuantifier(1, WeightUnit.KILOGRAM), ProductLimit.NO_LIMIT);
    private final Product productWeight1KgLimit1KgFor7Days = new Product(new ID("1"), "pr", new WeightQuantifier(1, WeightUnit.KILOGRAM), limit1KgFor7Days);
    private final Product productWeight500gLimit1KgFor7Days = new Product(new ID("1"), "pr", new WeightQuantifier(500, WeightUnit.GRAM), limit1KgFor7Days);

    @BeforeEach
    void setUp() {
        productRepository.save(productWeight1KgNoLimit);
        productRepository.save(productWeight1KgLimit1KgFor7Days);
        productRepository.save(productWeight500gLimit1KgFor7Days);

        storageRepository.save(storage);
    }

    @Test
    void should_add_product() {
        assertTrue(storageUserProductService.canAddProduct(user, storage, productWeight1KgNoLimit, 1));
    }

    @Test
    void should_not_add_product_when_limit_is_reached() {
        storageUserProductService.addProduct(user, productWeight1KgLimit1KgFor7Days, 1);
        assertFalse(storageUserProductService.canAddProduct(user, storage, productWeight1KgLimit1KgFor7Days, 1));
    }

    @Test
    void should_not_add_2product_when_limit_is_reached() {
        storageUserProductService.addProduct(user, productWeight500gLimit1KgFor7Days, 1);
        assertFalse(storageUserProductService.canAddProduct(user, storage, productWeight500gLimit1KgFor7Days, 2));
    }

    @Test
    void should_add_product_when_limit_is_not_entirely_reached() {
        storageUserProductService.addProduct(user, productWeight500gLimit1KgFor7Days, 1);
        assertTrue(storageUserProductService.canAddProduct(user, storage, productWeight500gLimit1KgFor7Days, 1));
    }

    @Test
    void should_add_product_if_duration() {
        storageUserProductService.addProduct(user, productWeight500gLimit1KgFor7Days, 1, LocalDate.now().minusDays(8));
        assertTrue(storageUserProductService.canAddProduct(user, storage, productWeight500gLimit1KgFor7Days, 1));
    }
}