package fr.croixrouge.exposition.dto;

import fr.croixrouge.storage.model.quantifier.MeasurementUnit;
import fr.croixrouge.storage.model.quantifier.Quantifier;

public class QuantifierDTO {

    private final String measurementUnit;
    private final double value;

    public QuantifierDTO(String measurementUnit, double value) {
        this.measurementUnit = measurementUnit;
        this.value = value;
    }

    public QuantifierDTO(Quantifier quantifier) {
        this(quantifier.getUnit().getName(), quantifier.getQuantity());
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public double getValue() {
        return value;
    }

    public static QuantifierDTO fromQuantifier(Quantifier quantifier) {
        return new QuantifierDTO(quantifier.getUnit().getName(), quantifier.getQuantity());
    }

    public Quantifier toQuantifier() {
        return new Quantifier(this.value, MeasurementUnit.fromName(this.measurementUnit));
    }

}
