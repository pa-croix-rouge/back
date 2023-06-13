package fr.croixrouge.storage.model.quantifier;

public class NumberedUnit extends MeasurementUnit {

    public static final String label = "nombre";

    public static final NumberedUnit UNKNOWN = new NumberedUnit("", 1);
    public static final NumberedUnit NUMBER = new NumberedUnit("pièce(s)", 1);

    private NumberedUnit(String name, float value) {
        super(name, value);
    }

    @Override
    public MeasurementUnit getBaseUnit() {
        return UNKNOWN;
    }
}
