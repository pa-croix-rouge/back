package fr.croixrouge.storage.model.quantifier;

public class NumberedUnit extends MeasurementUnit {
    public static final NumberedUnit UNKNOWN = new NumberedUnit("nombre", "", 1);
    public static final NumberedUnit NUMBER = new NumberedUnit("nombre", "pièce", 1);

    private NumberedUnit(String label, String name, float value) {
        super(label, name, value);
    }

    @Override
    public MeasurementUnit getBaseUnit() {
        return UNKNOWN;
    }
}
