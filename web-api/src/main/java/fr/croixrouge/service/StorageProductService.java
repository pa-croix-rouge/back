package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.repository.ProductRepository;
import fr.croixrouge.storage.repository.StorageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorageProductService {

    private final StorageRepository storageRepository;
    private final ProductRepository productRepository;

    private final fr.croixrouge.storage.service.StorageProductService storageProductService;

    public StorageProductService(StorageRepository storageRepository, ProductRepository productRepository, fr.croixrouge.storage.service.StorageProductService storageProductService) {
        this.storageRepository = storageRepository;
        this.productRepository = productRepository;
        this.storageProductService = storageProductService;
    }

    public void addProduct(ID storageId, ID productId, int quantity) {
        Storage storage = storageRepository.findById(storageId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();

        storageProductService.addProduct(storage, product, quantity);
    }

    public void removeProduct(ID storageId, ID productId, int quantity) {
        Storage storage = storageRepository.findById(storageId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();

        storageProductService.removeProduct(storage, product, quantity);
    }

    public List<StorageProduct> getProductsByStorage(ID storageId) {
        Storage storage = storageRepository.findById(storageId).orElseThrow();

        return storageProductService.getProductsByStorage(storage);
    }

    public Integer getProductQuantity(ID storageId, ID productId) {
        Storage storage = storageRepository.findById(storageId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();

        return storageProductService.getProductQuantity(storage, product);
    }

}
