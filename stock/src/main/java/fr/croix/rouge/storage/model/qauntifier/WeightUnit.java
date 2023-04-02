package fr.croix.rouge.storage.model.qauntifier;

public class WeightUnit extends MeasurementUnit {

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
