package fr.croixrouge.storage.model;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.model.product.ProductLimit;
import fr.croixrouge.storage.model.quantifier.WeightQuantifier;
import fr.croixrouge.storage.model.quantifier.WeightUnit;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StorageUserTest {

    Product get1KgProduct(ProductLimit limit) {
        return new Product(new ID("1"),
                "produit4",
                new WeightQuantifier(1, WeightUnit.KILOGRAM),
                limit);
    }

    Product get500gProduct(ProductLimit limit) {
        return new Product(new ID("1"),
                "produit4",
                new WeightQuantifier(500, WeightUnit.GRAM),
                limit);
    }

    @Test
    void should_add_product() {
        Product product = get1KgProduct(ProductLimit.NO_LIMIT);
        StorageUser user = new StorageUser("1", new ArrayList<>());
        assertTrue(user.canAddProduct(product, 1));
    }

    @Test
    void should_not_add_product_when_limit_is_reached() {
        Product product = get1KgProduct(new ProductLimit(Duration.ofDays(7), new WeightQuantifier(1, WeightUnit.KILOGRAM)));
        StorageUser user = new StorageUser("1", new ArrayList<>(List.of(new StorageUserProduct(product, LocalDate.now(), 1))));
        assertFalse(user.canAddProduct(product, 1));
    }

    @Test
    void should_not_add_2product_when_limit_is_reached() {
        Product product = get500gProduct(new ProductLimit(Duration.ofDays(7), new WeightQuantifier(1, WeightUnit.KILOGRAM)));
        StorageUser user = new StorageUser("1", new ArrayList<>(List.of(new StorageUserProduct(product, LocalDate.now(), 1))));
        assertFalse(user.canAddProduct(product, 2));
    }

    @Test
    void should_add_product_when_limit_is_not_entirely_reached() {
        Product product = get500gProduct(new ProductLimit(Duration.ofDays(7), new WeightQuantifier(1, WeightUnit.KILOGRAM)));
        StorageUser user = new StorageUser("1", new ArrayList<>(List.of(new StorageUserProduct(product, LocalDate.now(), 1))));
        assertTrue(user.canAddProduct(product, 1));
    }

    @Test
    void should_add_product_if_duration() {
        Product product = get1KgProduct(new ProductLimit(Duration.ofDays(7), new WeightQuantifier(1, WeightUnit.KILOGRAM)));
        StorageUser user = new StorageUser("1", new ArrayList<>(List.of(new StorageUserProduct(product, LocalDate.now().minusDays(8), 1))));
        assertTrue(user.canAddProduct(product, 1));
    }

}