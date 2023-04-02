package fr.croix.rouge.storage.model;

import fr.croix.rouge.storage.model.qauntifier.LiquidQuantifier;
import fr.croix.rouge.storage.model.qauntifier.LiquidUnit;
import fr.croix.rouge.storage.model.qauntifier.Quantifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LiquidQuantifierTest {

    @Test
    public void test_1L_plus_1L_should_be_2L() {
        LiquidQuantifier q1 = new LiquidQuantifier(1, LiquidUnit.LITER);
        LiquidQuantifier q2 = new LiquidQuantifier(1, LiquidUnit.LITER);

        Quantifier q3 = q1.add(q2);
        assertEquals(2, q3.getQuantity());
    }

    @Test
    public void test_1L_plus_1mL_should_be_1L1ml() {
        LiquidQuantifier q1 = new LiquidQuantifier(1, LiquidUnit.LITER);
        LiquidQuantifier q2 = new LiquidQuantifier(1, LiquidUnit.MILLILITER);

        Quantifier q3 = q1.add(q2);
        assertEquals(1.001, q3.getQuantity(), 0.0001);
    }

    @Test
    public void test_1cl_should_be_0_01L() {
        LiquidQuantifier q1 = new LiquidQuantifier(1, LiquidUnit.DECILITER);
        Quantifier q2 = q1.convertTo(LiquidUnit.LITER);

        assertEquals(0.01, q2.getQuantity(), 0.0001);
    }

}