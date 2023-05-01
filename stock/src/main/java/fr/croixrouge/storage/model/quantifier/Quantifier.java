package fr.croixrouge.storage.model.quantifier;

public class Quantifier {

    private final double quantity;

    private final MeasurementUnit unit;

    public Quantifier(double quantity, MeasurementUnit unit) {
        this.quantity = quantity;
        this.unit = unit;
    }

    public double getQuantity() {
        return quantity;
    }

    public MeasurementUnit getUnit() {
        return unit;
    }

    public Quantifier convertTo(MeasurementUnit unit) {
        return new Quantifier(this.quantity * this.unit.getConversionFactorTo(unit), unit);
    }

    public Quantifier add(Quantifier other) {
        return new Quantifier(this.quantity + other.quantity * other.unit.getConversionFactorTo(this.unit), this.unit);
    }

    public Quantifier multiply(double factor) {
        return new Quantifier(this.quantity * factor, this.unit);
    }

    public double getQuantityInBaseUnit() {
        return this.quantity * this.unit.getConversionFactorTo(this.unit.getBaseUnit());
    }

    public boolean isGreaterThan(Quantifier other) {
        return getQuantityInBaseUnit() > other.getQuantityInBaseUnit();
    }

    public boolean isGreaterOrEqualThan(Quantifier other) {
        return this.quantity * this.unit.getConversionFactorTo(other.unit) >= other.quantity;
    }

}
