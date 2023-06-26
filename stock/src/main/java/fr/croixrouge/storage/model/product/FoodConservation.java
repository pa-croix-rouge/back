package fr.croixrouge.storage.model.product;

import java.util.List;

public enum FoodConservation {
    FROZEN("congélateur"),
    REFRIGERATED("réfrigérateur"),
    ROOM_TEMPERATURE("température ambiante");

    private final String label;

    FoodConservation(String label) {
        this.label = label;
    }

    public static List<FoodConservation> getAllConservations() {
        return List.of(FoodConservation.values());
    }

    public static FoodConservation fromLabel(String label) {
        for (FoodConservation conservation : FoodConservation.values()) {
            if (conservation.getLabel().equals(label)) {
                return conservation;
            }
        }
        throw new IllegalArgumentException("No FoodConservation with label " + label);
    }

    public String getLabel() {
        return label;
    }
}
