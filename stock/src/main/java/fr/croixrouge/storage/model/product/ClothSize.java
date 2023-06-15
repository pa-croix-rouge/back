package fr.croixrouge.storage.model.product;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ClothSize {
    UNKNOWN("inconnu"),
    CHILD("enfant"),
    XS("XS"),
    S("S"),
    M("M"),
    L("L"),
    XL("XL"),
    XXL("XXL"),
    XXXL("XXXL");

    private final String label;

    ClothSize(String label) {
        this.label = label;
    }

    public static List<String> getAllSizes() {
        return Stream.of(ClothSize.values()).map(ClothSize::getLabel).collect(Collectors.toList());
    }

    public static ClothSize fromLabel(String label) {
        for (ClothSize size : ClothSize.values()) {
            if (size.getLabel().equals(label)) {
                return size;
            }
        }
        throw new IllegalArgumentException("No ClothSize with label " + label);
    }

    public String getLabel() {
        return label;
    }
}
