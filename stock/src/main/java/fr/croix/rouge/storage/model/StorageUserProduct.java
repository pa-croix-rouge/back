package fr.croix.rouge.storage.model;

import java.time.LocalDate;

public record StorageUserProduct(FoodProduct product, LocalDate date, int quantity) {

}
