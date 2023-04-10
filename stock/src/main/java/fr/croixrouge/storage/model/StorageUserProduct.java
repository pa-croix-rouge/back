package fr.croixrouge.storage.model;

import fr.croixrouge.storage.model.product.Product;

import java.time.LocalDate;

public record StorageUserProduct(Product product, LocalDate date, int quantity) {

}
