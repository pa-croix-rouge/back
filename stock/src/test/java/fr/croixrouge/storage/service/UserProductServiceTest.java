package fr.croixrouge.storage.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.model.product.ProductLimit;
import fr.croixrouge.storage.model.quantifier.WeightQuantifier;
import fr.croixrouge.storage.model.quantifier.WeightUnit;
import fr.croixrouge.storage.repository.StorageProductRepository;
import fr.croixrouge.storage.repository.UserProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryStorageProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryUserProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserProductServiceTest {

    StorageProductRepository storageProductRepository = new InMemoryStorageProductRepository();
    UserProductRepository userProductRepository = new InMemoryUserProductRepository();

    Storage storage = new Storage(new ID(1L), null, null);

    private final StorageProductService storageProductService = new StorageProductService(storageProductRepository);

    private final UserProductService userProductService = new UserProductService(userProductRepository, storageProductService);

    private final User user = new User(new ID(1L), "TEST", "TEST", null, List.of());

    private final ProductLimit limit1KgFor7Days = new ProductLimit(new ID(1L), Duration.ofDays(7), new WeightQuantifier(1, WeightUnit.KILOGRAM));
    private final Product productWeight1KgNoLimit = new Product(new ID(1L), "pr", new WeightQuantifier(1, WeightUnit.KILOGRAM), ProductLimit.NO_LIMIT);
    private final Product productWeight1KgLimit1KgFor7Days = new Product(new ID(1L), "pr", new WeightQuantifier(1, WeightUnit.KILOGRAM), limit1KgFor7Days);
    private final Product productWeight500gLimit1KgFor7Days = new Product(new ID(1L), "pr", new WeightQuantifier(500, WeightUnit.GRAM), limit1KgFor7Days);

    @BeforeEach
    void setUp() {
    }

    @Test
    void should_add_product() {
        assertTrue(userProductService.canAddProduct(user, storage, productWeight1KgNoLimit, 1));
    }

    @Test
    void should_add_product_and_decrease_storage_quantity() {
        userProductService.addProduct(user, storage, productWeight1KgNoLimit, 1);
        assertEquals(-1, storageProductService.getProductQuantity(storage, productWeight1KgNoLimit));
    }

    @Test
    void should_not_add_product_when_limit_is_reached() {
        userProductService.addProduct(user, storage, productWeight1KgLimit1KgFor7Days, 1);
        assertFalse(userProductService.canAddProduct(user, storage, productWeight1KgLimit1KgFor7Days, 1));
    }

    @Test
    void should_not_add_2product_when_limit_is_reached() {
        userProductService.addProduct(user, storage, productWeight500gLimit1KgFor7Days, 1);
        assertFalse(userProductService.canAddProduct(user, storage, productWeight500gLimit1KgFor7Days, 2));
    }

    @Test
    void should_add_product_when_limit_is_not_entirely_reached() {
        userProductService.addProduct(user, storage, productWeight500gLimit1KgFor7Days, 1);
        assertTrue(userProductService.canAddProduct(user, storage, productWeight500gLimit1KgFor7Days, 1));
    }

    @Test
    void should_add_product_if_duration() {
        userProductService.addProduct(user, storage, productWeight500gLimit1KgFor7Days, 1, LocalDateTime.now().minusDays(8));
        assertTrue(userProductService.canAddProduct(user, storage, productWeight500gLimit1KgFor7Days, 1));
    }
}
