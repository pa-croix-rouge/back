package fr.croixrouge.storage.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.model.quantifier.WeightQuantifier;
import fr.croixrouge.storage.model.quantifier.WeightUnit;
import fr.croixrouge.storage.repository.StorageProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryStorageProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StorageProductServiceTest {

    private final StorageProductRepository storageProductRepository = new InMemoryStorageProductRepository();

    private final Storage storage = new Storage(new ID(1L), null, null);

    private final Product product = new Product(new ID(1L), "pr", new WeightQuantifier(1, WeightUnit.KILOGRAM), null);
    private final StorageProduct storageProduct = new StorageProduct(storage, product, 1);

    private final StorageProductService storageProductService = new StorageProductService(storageProductRepository);

    @BeforeEach
    void setUp() {
        storageProductRepository.save(storageProduct);
    }

    @Test
    void should_add_one_product() {
        storageProductService.addProduct(storage, product, 1);
        assertEquals(2, storageProductService.getProductQuantity(storage, product));
    }

    @Test
    void should_remove_one_product() {
        storageProductService.removeProduct(storage, product, 1);
        assertEquals(0, storageProductService.getProductQuantity(storage, product));
    }
}