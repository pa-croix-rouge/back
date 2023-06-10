package fr.croixrouge.storage.model.product;

import java.util.List;

public class ProductList {
    protected final List<ClothProduct> clothProducts;
    protected final List<FoodProduct> foodProducts;

    public ProductList(List<ClothProduct> clothProducts, List<FoodProduct> foodProducts) {
        this.clothProducts = clothProducts;
        this.foodProducts = foodProducts;
    }

    public List<ClothProduct> getClothProducts() {
        return clothProducts;
    }

    public List<FoodProduct> getFoodProducts() {
        return foodProducts;
    }
}
