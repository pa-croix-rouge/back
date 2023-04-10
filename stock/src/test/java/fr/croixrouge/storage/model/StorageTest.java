package fr.croixrouge.storage.model;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.model.quantifier.WeightQuantifier;
import fr.croixrouge.storage.model.quantifier.WeightUnit;
import fr.croixrouge.storage.repository.ProductRepository;
import fr.croixrouge.storage.repository.StorageProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryProductRepository;
import fr.croixrouge.storage.repository.memory.InMemoryStorageProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StorageTest {

    ProductRepository productRepository = new InMemoryProductRepository();

    StorageProductRepository storageProductRepository = new InMemoryStorageProductRepository();

    Storage storage = new Storage(new ID("1"), null, null, productRepository, storageProductRepository);


    private final Product product = new Product(new ID("1"), "pr", new WeightQuantifier(1, WeightUnit.KILOGRAM), null);
    private StorageProduct storageProduct = new StorageProduct(storage, product, 1);

    @BeforeEach
    void setUp() {
        productRepository.save(product);
        storageProductRepository.save(storageProduct);
    }

    @Test
    void should_add_one_product() {
        storage.addProduct(product, 1);
        assertEquals(2, storage.getProductQuantity(product));
    }

    @Test
    void should_remove_one_product() {
        storage.removeProduct(product, 1);
        assertEquals(0, storage.getProductQuantity(product));
    }
}