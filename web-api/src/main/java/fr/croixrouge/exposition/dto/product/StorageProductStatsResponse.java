package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.ProductList;

public class StorageProductStatsResponse {
    private int totalFoodQuantity;
    private int totalClothesQuantity;
    private int soonExpiredFood;

    public StorageProductStatsResponse() {
    }

    public StorageProductStatsResponse(int totalFoodQuantity, int totalClothesQuantity, int soonExpiredFood) {
        this.totalFoodQuantity = totalFoodQuantity;
        this.totalClothesQuantity = totalClothesQuantity;
        this.soonExpiredFood = soonExpiredFood;
    }

    public static StorageProductStatsResponse fromProductList(ProductList productList, int soonExpiredFood) {
        return new StorageProductStatsResponse(
                productList.getFoodProducts().keySet().stream().mapToInt(StorageProduct::getQuantity).sum(),
                productList.getClothProducts().keySet().stream().mapToInt(StorageProduct::getQuantity).sum(),
                soonExpiredFood);
    }

    public int getTotalFoodQuantity() {
        return totalFoodQuantity;
    }

    public void setTotalFoodQuantity(int totalFoodQuantity) {
        this.totalFoodQuantity = totalFoodQuantity;
    }

    public int getTotalClothesQuantity() {
        return totalClothesQuantity;
    }

    public void setTotalClothesQuantity(int totalClothesQuantity) {
        this.totalClothesQuantity = totalClothesQuantity;
    }

    public int getSoonExpiredFood() {
        return soonExpiredFood;
    }

    public void setSoonExpiredFood(int soonExpiredFood) {
        this.soonExpiredFood = soonExpiredFood;
    }
}
