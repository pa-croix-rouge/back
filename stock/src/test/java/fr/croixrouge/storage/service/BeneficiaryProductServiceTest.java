package fr.croixrouge.storage.service;

import fr.croixrouge.domain.model.Beneficiary;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.model.product.ProductLimit;
import fr.croixrouge.storage.model.quantifier.WeightQuantifier;
import fr.croixrouge.storage.model.quantifier.WeightUnit;
import fr.croixrouge.storage.repository.BeneficiaryProductRepository;
import fr.croixrouge.storage.repository.StorageProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryBeneficiaryProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryStorageProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BeneficiaryProductServiceTest {

    StorageProductRepository storageProductRepository = new InMemoryStorageProductRepository();
    BeneficiaryProductRepository beneficiaryProductRepository = new InMemoryBeneficiaryProductRepository();

    Storage storage = new Storage(new ID(1L), "defaultStorage", null, null);

    private final StorageProductService storageProductService = new StorageProductService(storageProductRepository);

    private final BeneficiaryProductService beneficiaryProductService = new BeneficiaryProductService(beneficiaryProductRepository, storageProductService);

    private final User user = new User(new ID(1L), "TEST", "TEST", null, List.of());

    private final Beneficiary beneficiary = new Beneficiary(new ID(1L), user, "TEST", "TEST", "number", true, LocalDate.now(), "", List.of());

    private final ProductLimit limit1KgFor7Days = new ProductLimit(new ID(1L), "", Duration.ofDays(7), new WeightQuantifier(1, WeightUnit.KILOGRAM));
    private final Product productWeight1KgNoLimit = new Product(new ID(1L), "pr", new WeightQuantifier(1, WeightUnit.KILOGRAM), ProductLimit.NO_LIMIT);

    private final StorageProduct storageProductWeight1KgNoLimit = new StorageProduct(new ID(1L), storage, productWeight1KgNoLimit, 1);
    private final Product productWeight1KgLimit1KgFor7Days = new Product(new ID(1L), "pr", new WeightQuantifier(1, WeightUnit.KILOGRAM), limit1KgFor7Days);
    private final StorageProduct storageProductWeight1KgLimit1KgFor7Days = new StorageProduct(new ID(1L), storage, productWeight1KgLimit1KgFor7Days, 1);
    private final Product productWeight500gLimit1KgFor7Days = new Product(new ID(2L), "pr", new WeightQuantifier(500, WeightUnit.GRAM), limit1KgFor7Days);

    private final StorageProduct storageProductWeight500gLimit1KgFor7Days = new StorageProduct(new ID(2L), storage, productWeight500gLimit1KgFor7Days, 1);

    @BeforeEach
    void setUp() {
        storageProductRepository = new InMemoryStorageProductRepository();


    }

    @Test
    void should_add_product() {
        assertTrue(beneficiaryProductService.canAddProduct(beneficiary, storage, productWeight1KgNoLimit, 1));
    }

    @Test
    void should_add_product_and_decrease_storage_quantity() {
        beneficiaryProductService.addProduct(beneficiary, storageProductWeight1KgNoLimit, 1);
        assertEquals(-1, storageProductService.getProductQuantity(storage, productWeight1KgNoLimit));
    }

    @Test
    void should_not_add_product_when_limit_is_reached() {
        beneficiaryProductService.addProduct(beneficiary, storageProductWeight1KgLimit1KgFor7Days, 1);
        assertFalse(beneficiaryProductService.canAddProduct(beneficiary, storage, productWeight1KgLimit1KgFor7Days, 1));
    }

    @Test
    void should_not_add_2product_when_limit_is_reached() {
        beneficiaryProductService.addProduct(beneficiary, storageProductWeight500gLimit1KgFor7Days, 1);
        assertFalse(beneficiaryProductService.canAddProduct(beneficiary, storage, productWeight500gLimit1KgFor7Days, 2));
    }

    @Test
    void should_add_product_when_limit_is_not_entirely_reached() {
        beneficiaryProductService.addProduct(beneficiary, storageProductWeight500gLimit1KgFor7Days, 1);
        assertTrue(beneficiaryProductService.canAddProduct(beneficiary, storage, productWeight500gLimit1KgFor7Days, 1));
    }

    @Test
    void should_add_product_if_duration() {
        beneficiaryProductService.addProduct(beneficiary, storageProductWeight500gLimit1KgFor7Days, 1, LocalDateTime.now().minusDays(8));
        assertTrue(beneficiaryProductService.canAddProduct(beneficiary, storage, productWeight500gLimit1KgFor7Days, 1));
    }
}
