package fr.croix.rouge.storage.model;

import fr.croix.rouge.storage.model.product.Product;

import java.time.LocalDate;

public record StorageUserProduct(Product product, LocalDate date, int quantity) {

}
