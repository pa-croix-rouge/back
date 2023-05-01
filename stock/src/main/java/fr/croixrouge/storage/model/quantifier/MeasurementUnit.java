package fr.croixrouge.storage.model.quantifier;

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

    public static MeasurementUnit fromName(String name) {
        if (name.equals(WeightUnit.KILOGRAM.getName())) {
            return WeightUnit.KILOGRAM;
        } else if (name.equals(WeightUnit.GRAM.getName())) {
            return WeightUnit.GRAM;
        } else if (name.equals(VolumeUnit.LITER.getName())) {
            return VolumeUnit.LITER;
        } else if (name.equals(VolumeUnit.MILLILITER.getName())) {
            return VolumeUnit.MILLILITER;
        } else if (name.equals(VolumeUnit.DECILITER.getName())) {
            return VolumeUnit.DECILITER;
        } else {
            throw new IllegalArgumentException("Unknown measurement unit: " + name);
        }
    }
}
