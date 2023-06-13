package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.ProductList;

public class StorageProductStatsResponse {
    private int totalFoodQuantity;
    private int totalClothesQuantity;

    public StorageProductStatsResponse() {
    }

    public StorageProductStatsResponse(int totalFoodQuantity, int totalClothesQuantity) {
        this.totalFoodQuantity = totalFoodQuantity;
        this.totalClothesQuantity = totalClothesQuantity;
    }

    public static StorageProductStatsResponse fromProductList(ProductList productList) {
        return new StorageProductStatsResponse(
                productList.getFoodProducts().keySet().stream().mapToInt(StorageProduct::getQuantity).sum(),
                productList.getClothProducts().keySet().stream().mapToInt(StorageProduct::getQuantity).sum());
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
}
