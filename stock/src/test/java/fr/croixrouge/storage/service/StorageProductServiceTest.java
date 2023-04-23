package fr.croixrouge.storage.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.model.quantifier.WeightQuantifier;
import fr.croixrouge.storage.model.quantifier.WeightUnit;
import fr.croixrouge.storage.repository.ProductRepository;
import fr.croixrouge.storage.repository.StorageProductRepository;
import fr.croixrouge.storage.repository.StorageRepository;
import fr.croixrouge.storage.repository.memory.InMemoryProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryStorageProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryStorageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StorageProductServiceTest {

    StorageRepository storageRepository = new InMemoryStorageRepository();
    ProductRepository productRepository = new InMemoryProductRepository();

    StorageProductRepository storageProductRepository = new InMemoryStorageProductRepository();

    Storage storage = new Storage(new ID("1"), null, null);

    private final Product product = new Product(new ID("1"), "pr", new WeightQuantifier(1, WeightUnit.KILOGRAM), null);
    private final StorageProduct storageProduct = new StorageProduct(storage, product, 1);

    private final StorageProductService storageProductService = new StorageProductService(storageRepository, productRepository, storageProductRepository);

    @BeforeEach
    void setUp() {
        storageRepository.save(storage);
        productRepository.save(product);
        storageProductRepository.save(storageProduct);
    }

    @Test
    void should_add_one_product() {
        storageProductService.addProduct(storage.getId(), product.getId(), 1);
        assertEquals(2, storageProductService.getProductQuantity(storage, product));
    }

    @Test
    void should_remove_one_product() {
        storageProductService.removeProduct(storage, product, 1);
        assertEquals(0, storageProductService.getProductQuantity(storage, product));
    }
}