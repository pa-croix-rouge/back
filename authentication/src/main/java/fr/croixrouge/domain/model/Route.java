package fr.croixrouge.domain.model;

public enum Route {
    RESOURCE("/resources", "A route to test JWT authentication"),
    LOCAL_UNIT("/localunit", "Modifier les unités locals");

    private final String path;
    private final String description;

    Route(String path, String description) {
        this.path = path;
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public String getDescription() {
        return description;
    }
}
