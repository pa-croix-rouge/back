package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.storage.model.quantifier.MeasurementUnit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnitResponse {
    private Map<String, String> units;

    public UnitResponse() {
    }

    public UnitResponse(Map<String, String> units) {
        this.units = units;
    }

    public static UnitResponse fromMeasurementUnits() {
        List<MeasurementUnit> units = MeasurementUnit.getAllUnits();
        Map<String, String> map = new HashMap<>();
        for (MeasurementUnit unit : units) {
            map.put(unit.getName(), unit.getLabel());
        }
        return new UnitResponse(map);
    }

    public Map<String, String> getUnits() {
        return units;
    }

    public void setUnits(Map<String, String> units) {
        this.units = units;
    }
}
