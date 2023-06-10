package fr.croixrouge.exposition.dto.product;

import java.util.List;

public class ProductListResponse {
    List<ClothProductResponse> clothProducts;
    List<FoodProductResponse> foodProducts;

    public ProductListResponse() {
    }

    public ProductListResponse(List<ClothProductResponse> clothProducts, List<FoodProductResponse> foodProducts) {
        this.clothProducts = clothProducts;
        this.foodProducts = foodProducts;
    }

    public List<ClothProductResponse> getClothProducts() {
        return clothProducts;
    }

    public void setClothProducts(List<ClothProductResponse> clothProducts) {
        this.clothProducts = clothProducts;
    }
}
