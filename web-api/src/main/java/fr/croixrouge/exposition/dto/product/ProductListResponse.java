package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.storage.model.product.ProductList;

import java.util.Comparator;
import java.util.List;

public class ProductListResponse {
    private List<ClothStorageProductResponse> clothProducts;
    private List<FoodStorageProductResponse> foodProducts;

    public ProductListResponse() {
    }

    public ProductListResponse(List<ClothStorageProductResponse> clothProducts, List<FoodStorageProductResponse> foodProducts) {
        this.clothProducts = clothProducts;
        this.foodProducts = foodProducts;
    }

    public static ProductListResponse fromProductList(ProductList productList) {
        return new ProductListResponse(
                productList.getClothProducts().keySet().stream().map(clothKey -> ClothStorageProductResponse.fromClothProduct(productList.getClothProducts().get(clothKey), clothKey)).sorted(Comparator.comparing(ClothStorageProductResponse::getId)).toList(),
                productList.getFoodProducts().keySet().stream().map(foodKey -> FoodStorageProductResponse.fromFoodProduct(productList.getFoodProducts().get(foodKey), foodKey)).sorted(Comparator.comparing(FoodStorageProductResponse::getId)).toList()
        );
    }

    public List<ClothStorageProductResponse> getClothProducts() {
        return clothProducts;
    }

    public void setClothProducts(List<ClothStorageProductResponse> clothProducts) {
        this.clothProducts = clothProducts;
    }

    public List<FoodStorageProductResponse> getFoodProducts() {
        return foodProducts;
    }

    public void setFoodProducts(List<FoodStorageProductResponse> foodProducts) {
        this.foodProducts = foodProducts;
    }
}
