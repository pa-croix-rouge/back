package fr.croix.rouge.storage.model;

import fr.croix.rouge.storage.model.product.Product;
import fr.croix.rouge.storage.repository.ProductRepository;
import fr.croix.rouge.storage.repository.StorageProductRepository;
import fr.croixrouge.domain.model.Address;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.LocalUnit;

public class Storage {

    private final ID id;
    private final LocalUnit localUnit;
    private final Address address;

    private final ProductRepository productRepository;

    private final StorageProductRepository storageProductRepository;

    public Storage(ID id, LocalUnit localUnit, Address address, ProductRepository productRepository, StorageProductRepository storageProductRepository) {
        this.id = id;
        this.localUnit = localUnit;
        this.address = address;
        this.productRepository = productRepository;
        this.storageProductRepository = storageProductRepository;
    }

    public void addProduct(Product product, int quantity) {
        if (productRepository.findById(product.getId()).isEmpty())
            productRepository.save(product);

        storageProductRepository.findById(this, product)
                .ifPresentOrElse(storageProduct -> storageProductRepository.save(this, new StorageProduct(product, storageProduct.quantity() + quantity)),
                        () -> storageProductRepository.save(this, new StorageProduct(product, quantity)));
    }

    public void removeProduct(Product foodProduct, int quantity) {
        storageProductRepository.findById(this, foodProduct)
                .ifPresentOrElse(storageProduct -> storageProductRepository.save(this, new StorageProduct(foodProduct, storageProduct.quantity() - quantity)),
                        () -> storageProductRepository.save(this, new StorageProduct(foodProduct, -quantity)));
    }

}
