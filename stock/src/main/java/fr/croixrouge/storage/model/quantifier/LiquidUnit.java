package fr.croixrouge.storage.model.quantifier;

public class LiquidUnit extends MeasurementUnit {

    public static final LiquidUnit LITER = new LiquidUnit("LITER", 1);
    public static final LiquidUnit MILLILITER = new LiquidUnit("MILLILITER", 0.001f);
    public static final LiquidUnit DECILITER = new LiquidUnit("DECILITER", 0.01f);

    private LiquidUnit(String name, float value) {
        super(name, value);
    }

    @Override
    public MeasurementUnit getBaseUnit() {
        return LITER;
    }

}
