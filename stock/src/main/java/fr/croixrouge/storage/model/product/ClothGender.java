package fr.croixrouge.storage.model.product;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ClothGender {
    MALE("Homme"),
    FEMALE("Femme"),
    NOT_SPECIFIED("Non spécifié");

    private final String label;

    ClothGender(String label) {
        this.label = label;
    }

    public static List<String> getAllGenders() {
        return Stream.of(ClothGender.values()).map(ClothGender::getLabel).collect(Collectors.toList());
    }

    public static ClothGender fromLabel(String label) {
        for (ClothGender gender : ClothGender.values()) {
            if (gender.getLabel().equals(label)) {
                return gender;
            }
        }
        return null;
    }

    public String getLabel() {
        return label;
    }
}
