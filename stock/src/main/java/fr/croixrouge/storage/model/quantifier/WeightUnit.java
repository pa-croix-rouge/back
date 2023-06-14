package fr.croixrouge.storage.model.quantifier;

public class WeightUnit extends MeasurementUnit {
    public static final WeightUnit KILOGRAM = new WeightUnit("poids", "kilogram", 1000);
    public static final WeightUnit GRAM = new WeightUnit("poids", "gram", 1);


    private WeightUnit(String label, String name, float value) {
        super(label, name, value);
    }

    @Override
    public MeasurementUnit getBaseUnit() {
        return GRAM;
    }
}
