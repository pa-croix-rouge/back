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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private ProductList storageProductListToProductList(List<StorageProduct> storageProducts) {
        Map<StorageProduct, ClothProduct> clothProducts = new HashMap<>();
        Map<StorageProduct, FoodProduct> foodProducts = new HashMap<>();
        for (StorageProduct storageProduct : storageProducts) {
            clothProductRepository.findByProductId(storageProduct.getProduct().getId()).ifPresent(clothProduct -> clothProducts.put(storageProduct, clothProduct));
            foodProductRepository.findByProductId(storageProduct.getProduct().getId()).ifPresent(foodProduct -> foodProducts.put(storageProduct, foodProduct));
        }
        return new ProductList(clothProducts, foodProducts);
    }

    public StorageProduct findByProduct(Product product) {
        return storageProductService.findByProduct(product);
    }

    public ID save(StorageProduct storageProduct) {
        return storageProductService.save(storageProduct);
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

    public ProductList getProductsByStorage(ID storageId) {
        Storage storage = storageRepository.findById(storageId).orElseThrow();
        List<StorageProduct> products = storageProductService.getProductsByStorage(storage);
        return storageProductListToProductList(products);
    }

    public ProductList getProductsByLocalUnit(ID localUnitId) {
        List<StorageProduct> products = storageProductService.getProductsByLocalUnit(localUnitId);
        return storageProductListToProductList(products);
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
