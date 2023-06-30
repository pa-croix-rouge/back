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

    private ProductLimitDTO limit;

    public StorageProductResponse() {
    }

    public StorageProductResponse(Long storageProductId, Long productId, Long storageId, String productName, int quantity, String quantifierQuantity, String quantifierName, ProductLimitDTO limit) {
        this.storageProductId = storageProductId;
        this.productId = productId;
        this.storageId = storageId;
        this.productName = productName;
        this.quantity = quantity;
        this.quantifierQuantity = quantifierQuantity;
        this.quantifierName = quantifierName;
        this.limit = limit;
    }

    public StorageProductResponse(Product product, StorageProduct storageProduct) {
        this(storageProduct.getId().value(),
                product.getId().value(),
                storageProduct.getStorage().getId().value(),
                product.getName(),
                storageProduct.getQuantity(),
                BigDecimal.valueOf(product.getQuantity().getQuantity()).toString(),
                product.getQuantity().getUnit().getName(),
                ProductLimitDTO.of(product.getLimit()));

    }

    public static StorageProductResponse fromStorageProduct(StorageProduct storageProduct) {
        return new StorageProductResponse(
                storageProduct.getId().value(),
                storageProduct.getProduct().getId().value(),
                storageProduct.getStorage().getId().value(),
                storageProduct.getProduct().getName(),
                storageProduct.getQuantity(),
                BigDecimal.valueOf(storageProduct.getProduct().getQuantity().getQuantity()).toString(),
                storageProduct.getProduct().getQuantity().getUnit().getName(),
                ProductLimitDTO.of(storageProduct.getProduct().getLimit()));
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

    public ProductLimitDTO getLimit() {
        return limit;
    }
}
