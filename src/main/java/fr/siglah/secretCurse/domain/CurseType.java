package fr.siglah.secretCurse.domain;

/**
 * L'intégralité des 19 contraintes jouables en mode "Malédiction Cachée",
 * réparties dans leurs 4 catégories, telles que définies dans le cahier
 * des charges définitif.
 */
public enum CurseType {

    // --- Catégorie 1 : Déplacements & Caméra (6) ---
    ZOOLANDER(
            CurseCategory.DEPLACEMENT_CAMERA,
            "Syndrome Zoolander",
            "Impossible de tourner la caméra vers la gauche."
    ),
    FAKE_LAG(
            CurseCategory.DEPLACEMENT_CAMERA,
            "Faux Lag",
            "Toutes les 30 à 60 secondes, un freeze total de 1,5 à 2 secondes te téléporte à ta position de départ."
    ),
    COMPULSIVE_SPRINTER(
            CurseCategory.DEPLACEMENT_CAMERA,
            "Sprinteur Compulsif",
            "Impossible de marcher ou de t'accroupir : tu sprintes en permanence."
    ),
    SAINT_VITUS_DANCE(
            CurseCategory.DEPLACEMENT_CAMERA,
            "Danse de Saint-Guy",
            "Toutes les 20 à 40 secondes, un saut incontrôlable te secoue."
    ),
    PHANTOM_WEIGHT(
            CurseCategory.DEPLACEMENT_CAMERA,
            "Poids Fantôme",
            "Une chute lente automatique s'active dès que tu chutes de 3 blocs ou plus."
    ),
    SPIDER_IMPRINT(
            CurseCategory.DEPLACEMENT_CAMERA,
            "Empreinte de l'Araignée",
            "Tu peux grimper aux murs en t'y collant, comme une araignée."
    ),

    // --- Catégorie 2 : Inventaire & Objets (5) ---
    DIRTY_HANDS_ALLERGY(
            CurseCategory.INVENTAIRE_OBJETS,
            "Allergie aux Mains Sales",
            "Terre, sable, gravier ou boue en main sont éjectés instantanément au sol."
    ),
    FORCED_VEGETARIAN(
            CurseCategory.INVENTAIRE_OBJETS,
            "Végétarisme Forcé",
            "Manger de la viande t'empoisonne et te donne la nausée."
    ),
    HOLED_INVENTORY(
            CurseCategory.INVENTAIRE_OBJETS,
            "Inventaire Troué",
            "Toutes les 30 à 45 secondes, une unité d'un objet de ta hotbar tombe au sol sans bruit."
    ),
    IRRESISTIBLE_CRAVING(
            CurseCategory.INVENTAIRE_OBJETS,
            "Envie Irrépressible",
            "Un sucre magique illimité doit être consommé toutes les 60 secondes, sous peine de nausée croissante."
    ),
    FORCED_MINIMALISM(
            CurseCategory.INVENTAIRE_OBJETS,
            "Minimalisme Forcé",
            "Ton inventaire est limité à un nombre réduit d'objets ; tout surplus est éjecté au sol."
    ),

    // --- Catégorie 3 : Minage & Interactions (2) ---
    ORE_WHEEL(
            CurseCategory.MINAGE_INTERACTIONS,
            "La Roue des Minerais",
            "Charbon, Fer, Or et Diamant se transforment en le minerai suivant du cycle quand tu les mines."
    ),
    INNER_FURNACE(
            CurseCategory.MINAGE_INTERACTIONS,
            "Fournaise Intérieure",
            "Tout minerai brut et toute nourriture crue obtenus sont instantanément cuits ou raffinés."
    ),

    // --- Catégorie 4 : Environnement & Entités (6) ---
    CREEPER_SYNDROME(
            CurseCategory.ENVIRONNEMENT_ENTITES,
            "Syndrome du Creeper",
            "Rester immobile 2 secondes déclenche un sifflement puis une explosion."
    ),
    WATER_RAGE(
            CurseCategory.ENVIRONNEMENT_ENTITES,
            "Rage Hydrique",
            "Le contact avec l'eau t'empoisonne instantanément."
    ),
    VAMPIRE(
            CurseCategory.ENVIRONNEMENT_ENTITES,
            "Sang de Vampire",
            "Tu prends feu à la lumière directe du soleil en extérieur."
    ),
    MONSTER_MAGNET(
            CurseCategory.ENVIRONNEMENT_ENTITES,
            "Aimant à Monstres",
            "Tous les monstres proches te ciblent en priorité, en ignorant les autres joueurs."
    ),
    GLASS_ANKLES(
            CurseCategory.ENVIRONNEMENT_ENTITES,
            "Chevilles de Verre",
            "Toute chute, même minime, te tue instantanément."
    ),
    UNSTABLE_LOTTERY(
            CurseCategory.ENVIRONNEMENT_ENTITES,
            "Loterie Instable",
            "Un nouvel effet de potion aléatoire remplace le précédent toutes les 30 secondes."
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
