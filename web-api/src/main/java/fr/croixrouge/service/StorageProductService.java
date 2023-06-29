package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.ClothProduct;
import fr.croixrouge.storage.model.product.FoodProduct;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.model.product.ProductList;
import fr.croixrouge.storage.repository.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StorageProductService extends fr.croixrouge.storage.service.StorageProductService {

    private final StorageRepository storageRepository;
    private final ProductRepository productRepository;
    private final ClothProductRepository clothProductRepository;
    private final FoodProductRepository foodProductRepository;

    public StorageProductService(StorageProductRepository storageProductRepository, StorageRepository storageRepository, ProductRepository productRepository, ClothProductRepository clothProductRepository, FoodProductRepository foodProductRepository) {
        super(storageProductRepository);
        this.storageRepository = storageRepository;
        this.productRepository = productRepository;
        this.clothProductRepository = clothProductRepository;
        this.foodProductRepository = foodProductRepository;
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

    public StorageProduct findByID(ID id) {
        return storageProductRepository.findById(id).orElseThrow();
    }


    public ProductList getProductsByStorage(ID storageId) {
        Storage storage = storageRepository.findById(storageId).orElseThrow();
        List<StorageProduct> products = super.getProductsByStorage(storage);
        return storageProductListToProductList(products);
    }

    public ProductList getProductsByLocalUnit(ID localUnitId) {
        List<StorageProduct> products = super.getStorageProductsByLocalUnit(localUnitId);
        return storageProductListToProductList(products);
    }

    public Integer getProductQuantity(ID storageId, ID productId) {
        Storage storage = storageRepository.findById(storageId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();

        return getProductQuantity(storage, product);
    }

    public List<StorageProduct> findAllByLocalUnitId(ID localUnitId) {
        return super.getStorageProductsByLocalUnit(localUnitId);
    }

}
