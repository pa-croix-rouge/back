package fr.croixrouge.domain.model;

import java.util.Objects;

public record ID(Long value) {

    public ID {
        Objects.requireNonNull(value);
    }

    public ID(String value) {
        this(Long.valueOf(value));
    }

}
