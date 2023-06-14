package fr.croixrouge.storage.model.quantifier;

import java.util.List;

public abstract class MeasurementUnit {

    private final String label;
    private final String name;

    private final float value;

    public MeasurementUnit(String label, String name, float value) {
        this.label = label;
        this.name = name;
        this.value = value;
    }

    public String getLabel() {
        return label;
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
        } else if (name.equals(NumberedUnit.NUMBER.getName())) {
            return NumberedUnit.NUMBER;
        } else if (name.equals(NumberedUnit.UNKNOWN.getName())) {
            return NumberedUnit.UNKNOWN;
        } else {
            throw new IllegalArgumentException("Unknown measurement unit: " + name);
        }
    }

    public static List<MeasurementUnit> getAllUnits() {
        return List.of(WeightUnit.KILOGRAM, WeightUnit.GRAM, VolumeUnit.LITER, VolumeUnit.MILLILITER, VolumeUnit.DECILITER, NumberedUnit.NUMBER, NumberedUnit.UNKNOWN);
    }
}
