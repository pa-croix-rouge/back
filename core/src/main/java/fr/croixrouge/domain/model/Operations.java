package fr.croixrouge.domain.model;

public enum Operations {
    CREATE("Ajouter"),
    DELETE("Supprimer"),
    READ("Accéder"),
    UPDATE("Modifier");

    private final String name;

    public static Operations fromName(String name) {
        for (Operations operation : Operations.values()) {
            if (operation.getName().equals(name)) {
                return operation;
            }
        }
        return null;
    }

    Operations(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
