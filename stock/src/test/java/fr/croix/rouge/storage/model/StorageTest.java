package fr.croix.rouge.storage.model;

import fr.croix.rouge.storage.model.product.Product;
import fr.croix.rouge.storage.model.qauntifier.WeightQuantifier;
import fr.croix.rouge.storage.model.qauntifier.WeightUnit;
import fr.croix.rouge.storage.repository.ProductRepository;
import fr.croix.rouge.storage.repository.StorageProductRepository;
import fr.croix.rouge.storage.repository.memory.InMemoryProductRepository;
import fr.croix.rouge.storage.repository.memory.InMemoryStorageProductRepository;
import fr.croixrouge.domain.model.ID;
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
    void addProduct() {
        storage.addProduct(product, 1);
        assertEquals(2, storage.getProductQuantity(product));
    }

    @Test
    void removeProduct() {
    }
}