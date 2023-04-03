package fr.croix.rouge.storage.model.qauntifier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeightQuantifierTest {

    @Test
    public void test_1kg_plus_1kg_should_be_2kg() {
        WeightQuantifier q1 = new WeightQuantifier(1, WeightUnit.KILOGRAM);
        WeightQuantifier q2 = new WeightQuantifier(1, WeightUnit.KILOGRAM);

        Quantifier q3 = q1.add(q2);
        assertEquals(2, q3.getQuantity());
    }

    @Test
    public void test_1kg_plus_1g_should_be_1kg1g() {
        WeightQuantifier q1 = new WeightQuantifier(1, WeightUnit.KILOGRAM);
        WeightQuantifier q2 = new WeightQuantifier(1, WeightUnit.GRAM);

        Quantifier q3 = q1.add(q2);
        assertEquals(1.001, q3.getQuantity(), 0.0001);
    }

    @Test
    public void test_1g_should_be_0_001kg() {
        WeightQuantifier q1 = new WeightQuantifier(1, WeightUnit.GRAM);
        Quantifier q2 = q1.convertTo(WeightUnit.KILOGRAM);

        assertEquals(0.001, q2.getQuantity(), 0.0001);
    }

    @Test
    public void test_1000g_should_not_be_superior_to_1kg() {
        WeightQuantifier q1 = new WeightQuantifier(1000, WeightUnit.GRAM);
        WeightQuantifier q2 = new WeightQuantifier(1, WeightUnit.KILOGRAM);

        assertFalse(q1.isGreaterThan(q2));
    }

    @Test
    public void test_1001g_should_not_be_superior_to_1kg() {
        WeightQuantifier q1 = new WeightQuantifier(1001, WeightUnit.GRAM);
        WeightQuantifier q2 = new WeightQuantifier(1, WeightUnit.KILOGRAM);

        assertTrue(q1.isGreaterThan(q2));
    }

}
