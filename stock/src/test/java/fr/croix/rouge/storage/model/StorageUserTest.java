package fr.croix.rouge.storage.model;

import fr.croix.rouge.storage.model.product.FoodProduct;
import fr.croix.rouge.storage.model.product.ProductLimit;
import fr.croix.rouge.storage.model.qauntifier.LiquidQuantifier;
import fr.croix.rouge.storage.model.qauntifier.LiquidUnit;
import fr.croixrouge.domain.model.ID;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StorageUserTest {

    @Test
    void canAddProduct() {
        FoodProduct product = new FoodProduct(new ID("1"),
                "Lait",
                new LiquidQuantifier(1, LiquidUnit.LITER),
                new ProductLimit(Duration.ofDays(7), new LiquidQuantifier(1, LiquidUnit.LITER)));

        StorageUser user = new StorageUser("1", "test1", "", List.of());

        assertTrue(user.canAddProduct(product, 1));
        user.addProduct(product, 1);
        assertFalse(user.canAddProduct(product, 1));
    }
}