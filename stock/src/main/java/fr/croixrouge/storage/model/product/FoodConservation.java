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

    public String getLabel() {
        return label;
    }
}
