package fr.croixrouge.domain.model;

public enum Resources {
    BENEFICIARY("Bénéficiaire"),
    RESOURCE("Ressource"),
    EVENT("Événement"),
    PRODUCT("Produit"),
    ROLE("Rôle"),
    STORAGE("Stocks"),
    VOLUNTEER("Volontaire"),
    LOCAL_UNIT("Unité locale");

    private final String name;

    public static Resources fromName(String name) {
        for (Resources resource : Resources.values()) {
            if (resource.getName().equals(name)) {
                return resource;
            }
        }
        return null;
    }

    Resources(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
