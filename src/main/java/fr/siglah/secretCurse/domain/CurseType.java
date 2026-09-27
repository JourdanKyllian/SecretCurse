package fr.siglah.secretCurse.domain;

/**
 * Ensemble des contraintes ("malédictions") jouables en mode "Malédiction Cachée".
 * Reste volontairement dépourvu de toute dépendance Bukkit/Paper : le mapping
 * vers les Material d'icônes se fait côté présentation (voir GuiIcons).
 */
public enum CurseType {
    
    // --- Catégorie 1 : Déplacements & Caméra ---
    ZOOLANDER(
            CurseCategory.DEPLACEMENT_CAMERA,
            "Syndrome Zoolander",
            "Impossible de tourner la caméra vers la gauche."
    ),
    FAKE_LAG(
            CurseCategory.DEPLACEMENT_CAMERA,
            "Faux Lag",
            "Un décalage artificiel s'invite dans tes mouvements."
    ),
    COMPULSIVE_SPRINTER(
            CurseCategory.DEPLACEMENT_CAMERA,
            "Sprinteur Compulsif",
            "Impossible de marcher : tu sprintes en permanence."
    ),

    // --- Catégorie 2 : Inventaire & Objets ---
    DIRTY_HANDS_ALLERGY(
            CurseCategory.INVENTAIRE_OBJETS,
            "Allergie aux Mains Sales",
            "Tu ne peux tenir un objet en main que les mains \"propres\"."
    ),
    FORCED_VEGETARIAN(
            CurseCategory.INVENTAIRE_OBJETS,
            "Végétarisme Forcé",
            "Impossible de manger de la viande, sous peine de malus."
    ),

    // --- Catégorie 3 : Minage & Interactions ---
    MINER_MYOPIA(
            CurseCategory.MINAGE_INTERACTIONS,
            "Myopie du Mineur",
            "Ta vue se brouille dès que tu creuses sous le niveau de la mer."
    ),
    BUTTER_FINGERS(
            CurseCategory.MINAGE_INTERACTIONS,
            "Doigts de Beurre",
            "Un bloc sur trois te glisse des mains lors du minage."
    ),

    // --- Catégorie 4 : Environnement & Entités ---
    SHARK_SYNDROME(
            CurseCategory.ENVIRONNEMENT_ENTITES,
            "Syndrome du Requin",
            "Tu dois rester en mouvement permanent, sous peine de dégâts."
    ),
    ALTITUDE_INSOMNIA(
            CurseCategory.ENVIRONNEMENT_ENTITES,
            "Insomnie de l'Altitude",
            "Impossible de dormir dès que tu es en hauteur."
    ),
    THERMAL_VISION(
            CurseCategory.ENVIRONNEMENT_ENTITES,
            "Vision Thermique",
            "Ta vision est perturbée façon caméra thermique."
    );

    private final CurseCategory category;
    private final String displayName;
    private final String description;

    CurseType(CurseCategory category, String displayName, String description) {
        this.category = category;
        this.displayName = displayName;
        this.description = description;
    }

    public CurseCategory getCategory() {
        return category;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
