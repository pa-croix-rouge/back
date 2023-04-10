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
                .ifPresentOrElse(storageProduct -> storageProductRepository.save(new StorageProduct(storageProduct.getId(), this, product, storageProduct.getQuantity() + quantity)),
                        () -> storageProductRepository.save(new StorageProduct(this, product, quantity)));
    }

    public int getProductQuantity(Product product) {
        return storageProductRepository.findById(this, product)
                .map(StorageProduct::getQuantity)
                .orElse(0);
    }

    public void removeProduct(Product foodProduct, int quantity) {
        storageProductRepository.findById(this, foodProduct)
                .ifPresentOrElse(storageProduct -> storageProductRepository.save(new StorageProduct(storageProduct.getId(), this, foodProduct, storageProduct.getQuantity() - quantity)),
                        () -> storageProductRepository.save(new StorageProduct(this, foodProduct, -quantity)));
    }

}
