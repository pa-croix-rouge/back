package fr.croixrouge.storage.model.product;

import fr.croixrouge.storage.model.StorageProduct;

import java.util.Map;

public class ProductList {
    protected final Map<StorageProduct, ClothProduct> clothProducts;
    protected final Map<StorageProduct, FoodProduct> foodProducts;

    public ProductList(Map<StorageProduct, ClothProduct> clothProducts, Map<StorageProduct, FoodProduct> foodProducts) {
        this.clothProducts = clothProducts;
        this.foodProducts = foodProducts;
    }

    public Map<StorageProduct, ClothProduct> getClothProducts() {
        return clothProducts;
    }

    public Map<StorageProduct, FoodProduct> getFoodProducts() {
        return foodProducts;
    }
}
