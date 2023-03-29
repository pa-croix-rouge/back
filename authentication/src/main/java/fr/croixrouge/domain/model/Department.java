package fr.croixrouge.domain.model;

public enum Department {
    AIN("Ain", "01"),
    AISNE("Aisne", "02"),
    ALLIER("Allier", "03"),
    ALPES_DE_HAUTE_PROVENCE("Alpes-de-Haute-Provence", "04"),
    HAUTES_ALPES("Hautes-Alpes", "05"),
    ALPES_MARITIMES("Alpes-Maritimes", "06"),
    ARDECHE("Ardèche", "07"),
    ARDENNES("Ardennes", "08"),
    ARIEGE("Ariège", "09"),
    AUBE("Aube", "10"),
    AUDE("Aude", "11"),
    AVEYRON("Aveyron", "12"),
    BOUCHES_DU_RHONE("Bouches-du-Rhône", "13"),
    CALVADOS("Calvados", "14"),
    CANTAL("Cantal", "15"),
    CHARENTE("Charente", "16"),
    CHARENTE_MARITIME("Charente-Maritime", "17"),
    CHER("Cher", "18"),
    CORREZE("Corrèze", "19"),
    COTE_D_OR("Côte-d'Or", "21"),
    COTES_D_ARMOR("Côtes-d'Armor", "22"),
    CREUSE("Creuse", "23"),
    DORDOGNE("Dordogne", "24"),
    DOUBS("Doubs", "25"),
    DROME("Drôme", "26"),
    EURE("Eure", "27"),
    EURE_ET_LOIR("Eure-et-Loir", "28"),
    FINISTERE("Finistère", "29"),
    GARD("Gard", "30"),
    HAUTE_GARONNE("Haute-Garonne", "31"),
    GERS("Gers", "32"),
    GIRONDE("Gironde", "33"),
    HERAULT("Hérault", "34"),
    ILLE_ET_VILAINE("Ille-et-Vilaine", "35"),
    INDRE("Indre", "36"),
    INDRE_ET_LOIRE("Indre-et-Loire", "37"),
    ISERE("Isère", "38"),
    JURA("Jura", "39"),
    LANDES("Landes", "40"),
    LOIR_ET_CHER("Loir-et-Cher", "41"),
    LOIRE("Loire", "42"),
    HAUTE_LOIRE("Haute-Loire", "43"),
    LOIRE_ATLANTIQUE("Loire-Atlantique", "44"),
    LOIRET("Loiret", "45"),
    LOT("Lot", "46"),
    LOT_ET_GARONNE("Lot-et-Garonne", "47"),
    LOZERE("Lozère", "48"),
    MAINE_ET_LOIRE("Maine-et-Loire", "49"),
    MANCHE("Manche", "50"),
    MARNE("Marne", "51"),
    HAUTE_MARNE("Haute-Marne", "52"),
    MAYENNE("Mayenne", "53"),
    MEURTHE_ET_MOSELLE("Meurthe-et-Moselle", "54"),
    MEUSE("Meuse", "55"),
    MORBIHAN("Morbihan", "56"),
    MOSELLE("Moselle", "57"),
    NIEVRE("Nièvre", "58"),
    NORD("Nord", "59"),
    OISE("Oise", "60"),
    ORNE("Orne", "61"),
    PAS_DE_CALAIS("Pas-de-Calais", "62"),
    PUY_DE_DOME("Puy-de-Dôme", "63"),
    PYRENEES_ATLANTIQUES("Pyrénées-Atlantiques", "64"),
    HAUTES_PYRENEES("Hautes-Pyrénées", "65"),
    PYRENEES_ORIENTALES("Pyrénées-Orientales", "66"),
    BAS_RHIN("Bas-Rhin", "67"),
    HAUT_RHIN("Haut-Rhin", "68"),
    RHONE("Rhône", "69"),
    HAUTE_SAONE("Haute-Saône", "70"),
    SAONE_ET_LOIRE("Saône-et-Loire", "71"),
    SARTHE("Sarthe", "72"),
    SAVOIE("Savoie", "73"),
    HAUTE_SAVOIE("Haute-Savoie", "74"),
    PARIS("Paris", "75"),
    SEINE_MARITIME("Seine-Maritime", "76"),
    SEINE_ET_MARNE("Seine-et-Marne", "77"),
    YVELINES("Yvelines", "78"),
    DEUX_SEVRES("Deux-Sèvres", "79"),
    SOMME("Somme", "80"),
    TARN("Tarn", "81"),
    TARN_ET_GARONNE("Tarn-et-Garonne", "82"),
    VAR("Var", "83"),
    VAUCLUSE("Vaucluse", "84"),
    VENDEE("Vendée", "85"),
    VIENNE("Vienne", "86"),
    HAUTE_VIENNE("Haute-Vienne", "87"),
    VOSGES("Vosges", "88"),
    YONNE("Yonne", "89"),
    TERRITOIRE_DE_BELFORT("Territoire de Belfort", "90"),
    ESSONNE("Essonne", "91"),
    HAUTS_DE_SEINE("Hauts-de-Seine", "92"),
    SEINE_SAINT_DENIS("Seine-Saint-Denis", "93"),
    VAL_DE_MARNE("Val-de-Marne", "94"),
    VAL_D_OISE("Val-d'Oise", "95"),
    GUADELOUPE("Guadeloupe", "971"),
    MARTINIQUE("Martinique", "972"),
    GUYANE("Guyane", "973"),
    LA_REUNION("La Réunion", "974"),
    MAYOTTE("Mayotte", "976");

    private final String name;
    private final String code;

    Department(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public static Department getDepartmentFromPostalCode(String postalCode) {
        for (Department department : Department.values()) {
            if (department.getCode().equals(postalCode)) {
                return department;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
