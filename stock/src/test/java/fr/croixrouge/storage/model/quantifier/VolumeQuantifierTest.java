package fr.croixrouge.storage.model.quantifier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VolumeQuantifierTest {

    @Test
    public void test_1L_plus_1L_should_be_2L() {
        VolumeQuantifier q1 = new VolumeQuantifier(1, VolumeUnit.LITER);
        VolumeQuantifier q2 = new VolumeQuantifier(1, VolumeUnit.LITER);

        Quantifier q3 = q1.add(q2);
        assertEquals(2, q3.getQuantity());
    }

    @Test
    public void test_1L_plus_1mL_should_be_1L1ml() {
        VolumeQuantifier q1 = new VolumeQuantifier(1, VolumeUnit.LITER);
        VolumeQuantifier q2 = new VolumeQuantifier(1, VolumeUnit.MILLILITER);

        Quantifier q3 = q1.add(q2);
        assertEquals(1.001, q3.getQuantity(), 0.0001);
    }

    @Test
    public void test_1cl_should_be_0_01L() {
        VolumeQuantifier q1 = new VolumeQuantifier(1, VolumeUnit.DECILITER);
        Quantifier q2 = q1.convertTo(VolumeUnit.LITER);

        assertEquals(0.01, q2.getQuantity(), 0.0001);
    }

}