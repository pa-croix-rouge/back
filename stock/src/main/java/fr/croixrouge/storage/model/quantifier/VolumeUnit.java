package fr.croixrouge.storage.model.quantifier;

public class VolumeUnit extends MeasurementUnit {

    public static final VolumeUnit LITER = new VolumeUnit("LITER", 1);
    public static final VolumeUnit MILLILITER = new VolumeUnit("MILLILITER", 0.001f);
    public static final VolumeUnit DECILITER = new VolumeUnit("DECILITER", 0.01f);

    private VolumeUnit(String name, float value) {
        super(name, value);
    }

    @Override
    public MeasurementUnit getBaseUnit() {
        return LITER;
    }

}
