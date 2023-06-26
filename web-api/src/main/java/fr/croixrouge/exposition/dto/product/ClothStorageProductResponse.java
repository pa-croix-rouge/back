package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.ClothProduct;

public class ClothStorageProductResponse extends StorageProductResponse {

    private Long id;

    private String size;

    public ClothStorageProductResponse() {
    }

    public ClothStorageProductResponse(ClothProduct product, StorageProduct storageProduct) {
        super(product.getProduct(), storageProduct);
        this.id = product.getId().value();
        this.size = product.getSize().getLabel();
    }

    public ClothStorageProductResponse(Long id, Long storageProductId, Long productId, Long storageId, String productName, int quantity, String quantifierQuantity, String quantifierName, String size) {
        super(storageProductId, productId, storageId, productName, quantity, quantifierQuantity, quantifierName);
        this.id = id;
        this.size = size;
    }

    public static ClothStorageProductResponse fromClothProduct(ClothProduct product, StorageProduct storageProduct) {
        return new ClothStorageProductResponse(product, storageProduct);
    }

    public Long getId() {
        return id;
    }

    public String getSize() {
        return size;
    }
}
