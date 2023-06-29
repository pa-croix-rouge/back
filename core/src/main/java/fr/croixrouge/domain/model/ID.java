package fr.croixrouge.domain.model;

import java.util.Objects;

public record ID(Long value) {

    public ID {
        Objects.requireNonNull(value);
    }

    public static ID of(Long value) {
        return new ID(value);
    }

    public ID(String value) {
        this(Long.valueOf(value));
    }

}
