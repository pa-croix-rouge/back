package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.Product;

import java.math.BigDecimal;

public class StorageProductResponse {
    private Long storageProductId;
    private Long productId;
    private Long storageId;
    private String productName;
    private int quantity;
    private String quantifierQuantity;
    private String quantifierName;

    public StorageProductResponse() {
    }

    public StorageProductResponse(Long storageProductId, Long productId, Long storageId, String productName, int quantity, String quantifierQuantity, String quantifierName) {
        this.storageProductId = storageProductId;
        this.productId = productId;
        this.storageId = storageId;
        this.productName = productName;
        this.quantity = quantity;
        this.quantifierQuantity = quantifierQuantity;
        this.quantifierName = quantifierName;
    }

    public StorageProductResponse(Product product, StorageProduct storageProduct) {
        this.storageProductId = storageProduct.getId().value();
        this.productId = product.getId().value();
        this.storageId = storageProduct.getStorage().getId().value();
        this.productName = product.getName();
        this.quantity = storageProduct.getQuantity();
        this.quantifierQuantity = BigDecimal.valueOf(product.getQuantity().getQuantity()).toString();
        this.quantifierName = product.getQuantity().getUnit().getName();
    }

    public static StorageProductResponse fromStorageProduct(StorageProduct storageProduct) {
        return new StorageProductResponse(
                storageProduct.getId().value(),
                storageProduct.getProduct().getId().value(),
                storageProduct.getStorage().getId().value(),
                storageProduct.getProduct().getName(),
                storageProduct.getQuantity(),
                BigDecimal.valueOf(storageProduct.getProduct().getQuantity().getQuantity()).toString(),
                storageProduct.getProduct().getQuantity().getUnit().getName()
        );
    }

    public Long getStorageProductId() {
        return storageProductId;
    }

    public void setStorageProductId(Long storageProductId) {
        this.storageProductId = storageProductId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getQuantifierQuantity() {
        return quantifierQuantity;
    }

    public void setQuantifierQuantity(String quantifierQuantity) {
        this.quantifierQuantity = quantifierQuantity;
    }

    public String getQuantifierName() {
        return quantifierName;
    }

    public void setQuantifierName(String quantifierName) {
        this.quantifierName = quantifierName;
    }
}
