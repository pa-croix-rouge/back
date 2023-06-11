package fr.croixrouge.storage.model.quantifier;

public class NumberedUnit extends MeasurementUnit {

    public static final NumberedUnit UNKNOWN = new NumberedUnit("UNKNOWN", 1);
    public static final NumberedUnit NUMBER = new NumberedUnit("NUMBER", 1);

    private NumberedUnit(String name, float value) {
        super(name, value);
    }

    @Override
    public MeasurementUnit getBaseUnit() {
        return UNKNOWN;
    }
}
