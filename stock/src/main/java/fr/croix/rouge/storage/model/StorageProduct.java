package fr.croix.rouge.storage.model;

import fr.croix.rouge.storage.model.product.Product;

public record StorageProduct(Product product, int quantity) {
}
