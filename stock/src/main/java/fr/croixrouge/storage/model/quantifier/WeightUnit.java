package fr.croixrouge.storage.model.quantifier;

public class WeightUnit extends MeasurementUnit {

    public static final String label = "poids";

    public static final WeightUnit KILOGRAM = new WeightUnit("kilogram", 1000);
    public static final WeightUnit GRAM = new WeightUnit("gram", 1);


    private WeightUnit(String name, float value) {
        super(name, value);
    }

    @Override
    public MeasurementUnit getBaseUnit() {
        return GRAM;
    }
}
