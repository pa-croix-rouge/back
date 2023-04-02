package fr.croix.rouge.storage.model;

import fr.croix.rouge.storage.model.product.FoodProduct;

import java.time.LocalDate;

public record StorageUserProduct(FoodProduct product, LocalDate date, int quantity) {

}
