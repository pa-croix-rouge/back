package fr.croix.rouge.storage.model;

public abstract class MeasurementUnit {

    private final String name;

    private final float value;

    public MeasurementUnit(String name, float value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public float getValue() {
        return value;
    }

    public abstract MeasurementUnit getBaseUnit();

    public float getConversionFactorTo(MeasurementUnit unit) {
        return this.getValue() / unit.getValue();
    }
}
