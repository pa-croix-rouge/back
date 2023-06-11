package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.ClothProduct;
import fr.croixrouge.storage.model.product.FoodProduct;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.model.product.ProductList;
import fr.croixrouge.storage.repository.ClothProductRepository;
import fr.croixrouge.storage.repository.FoodProductRepository;
import fr.croixrouge.storage.repository.ProductRepository;
import fr.croixrouge.storage.repository.StorageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StorageProductService {

    private final StorageRepository storageRepository;
    private final ProductRepository productRepository;
    private final ClothProductRepository clothProductRepository;
    private final FoodProductRepository foodProductRepository;
    private final fr.croixrouge.storage.service.StorageProductService storageProductService;

    public StorageProductService(StorageRepository storageRepository, ProductRepository productRepository, ClothProductRepository clothProductRepository, FoodProductRepository foodProductRepository, fr.croixrouge.storage.service.StorageProductService storageProductService) {
        this.storageRepository = storageRepository;
        this.productRepository = productRepository;
        this.clothProductRepository = clothProductRepository;
        this.foodProductRepository = foodProductRepository;
        this.storageProductService = storageProductService;
    }

    public StorageProduct findByProduct(Product product) {
        return storageProductService.findByProduct(product);
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

    public ProductList getProductsByLocalUnit(ID localUnitId) {
        List<StorageProduct> products = storageProductService.getProductsByLocalUnit(localUnitId);
        List<ClothProduct> clothProducts = new ArrayList<>();
        List<FoodProduct> foodProducts = new ArrayList<>();
        for (StorageProduct product : products) {
            foodProductRepository.findById(product.getProduct().getId()).ifPresent(foodProducts::add);
            clothProductRepository.findById(product.getProduct().getId()).ifPresent(clothProducts::add);
        }
        return new ProductList(clothProducts, foodProducts);
    }

    public Integer getProductQuantity(ID storageId, ID productId) {
        Storage storage = storageRepository.findById(storageId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();

        return storageProductService.getProductQuantity(storage, product);
    }

    public void delete(StorageProduct storageProduct) {
        storageProductService.delete(storageProduct);
    }
}
