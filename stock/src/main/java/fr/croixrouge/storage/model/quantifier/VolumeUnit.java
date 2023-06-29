package fr.croixrouge.storage.model.quantifier;

public class VolumeUnit extends MeasurementUnit {
    public static final VolumeUnit LITER = new VolumeUnit("volume", "litre", 1);
    public static final VolumeUnit MILLILITER = new VolumeUnit("volume", "millilitre", 0.001f);
    public static final VolumeUnit DECILITER = new VolumeUnit("volume", "decilitre", 0.01f);

    private VolumeUnit(String label, String name, float value) {
        super(label, name, value);
    }

    @Override
    public MeasurementUnit getBaseUnit() {
        return LITER;
    }

}
