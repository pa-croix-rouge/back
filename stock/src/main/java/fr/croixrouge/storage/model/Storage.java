package fr.croixrouge.storage.model;

import fr.croixrouge.domain.model.Address;
import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.repository.ProductRepository;
import fr.croixrouge.storage.repository.StorageProductRepository;

public class Storage extends Entity<ID> {

    private final LocalUnit localUnit;
    private final Address address;

    private final ProductRepository productRepository;

    private final StorageProductRepository storageProductRepository;

    public Storage(ID id, LocalUnit localUnit, Address address, ProductRepository productRepository, StorageProductRepository storageProductRepository) {
        super(id);
        this.localUnit = localUnit;
        this.address = address;
        this.productRepository = productRepository;
        this.storageProductRepository = storageProductRepository;
    }

    public LocalUnit getLocalUnit() {
        return localUnit;
    }

    public Address getAddress() {
        return address;
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

    public void removeProduct(Product product, int quantity) {
        storageProductRepository.findById(this, product)
                .ifPresentOrElse(storageProduct -> storageProductRepository.save(new StorageProduct(storageProduct.getId(), this, product, storageProduct.getQuantity() - quantity)),
                        () -> storageProductRepository.save(new StorageProduct(this, product, -quantity)));
    }

}
