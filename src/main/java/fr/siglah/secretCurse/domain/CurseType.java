package fr.siglah.secretCurse.domain;

/**
 * L'intégralité des 25 défis jouables, répartis dans leurs 4 catégories.
 */
public enum CurseType {

    // --- Catégorie 1 : Déplacements & Caméra (6) ---
    ZOOLANDER(CurseCategory.DEPLACEMENT_CAMERA, "Syndrome Zoolander", "Impossible de tourner la caméra vers la gauche."),
    FAKE_LAG(CurseCategory.DEPLACEMENT_CAMERA, "Faux Lag", "Un décalage artificiel s'invite dans tes mouvements."),
    ANTI_RABBIT(CurseCategory.DEPLACEMENT_CAMERA, "L'Anti-Lapin", "Sauter t'épuise et t'inflige une lourde lenteur."),
    STIFF_NECK(CurseCategory.DEPLACEMENT_CAMERA, "Syndrome du Torticolis", "Impossible de regarder en haut ou en bas, ta tête est bloquée."),
    COWARDLY_FIGHTER(CurseCategory.DEPLACEMENT_CAMERA, "Le Combattant Froussard", "Frapper un ennemi te fait paniquer (Vitesse extrême et Cécité)."),
    PACIFIST_JUMPER(CurseCategory.DEPLACEMENT_CAMERA, "Le Sauteur Pacifique", "Impossible de sauter si tu tiens une arme en main."),

    // --- Catégorie 2 : Inventaire & Objets (6) ---
    DEPENDENT_LEFTY(CurseCategory.INVENTAIRE_OBJETS, "Le Gaucher Dépendant", "Tu ne peux utiliser tes outils qu'en main secondaire."),
    HOTBAR_ONLY(CurseCategory.INVENTAIRE_OBJETS, "Syndrome Hotbar Only", "Ton inventaire principal rejette les objets (barre d'action uniquement)."),
    EXCLUSIVITY_CONTRACT(CurseCategory.INVENTAIRE_OBJETS, "Contrat d'Exclusivité", "Interdit d'avoir plusieurs types d'outils différents sur toi."),
    SLIPPERY_HANDS(CurseCategory.INVENTAIRE_OBJETS, "Les Mains Glissantes", "Ton objet en main glisse et tombe aléatoirement."),
    SHATTERING_STACK(CurseCategory.INVENTAIRE_OBJETS, "Stack Brisant", "Les piles de 64 objets se divisent toutes seules."),
    CAPRICIOUS_GOURMET(CurseCategory.INVENTAIRE_OBJETS, "Gourmet Capricieux", "Impossible de manger deux fois de suite la même nourriture."),

    // --- Catégorie 3 : Minage & Interactions (7) ---
    DIRTY_HANDS(CurseCategory.MINAGE_INTERACTIONS, "Mains Sales", "Miner de la terre ou du sable te salit la vue (Cécité)."),
    ALTITUDE(CurseCategory.MINAGE_INTERACTIONS, "Altitude", "Miner en hauteur (Y > 100) te donne le vertige (Nausée)."),
    SUPERSTITION(CurseCategory.MINAGE_INTERACTIONS, "Superstition", "Casser des blocs précieux te donne de la malchance."),
    SHY_BUILDER(CurseCategory.MINAGE_INTERACTIONS, "Bâtisseur Timide", "Impossible de poser des blocs si un autre joueur te regarde."),
    FEARFUL_CRAFTER(CurseCategory.MINAGE_INTERACTIONS, "Artisan Craintif", "L'établi se ferme parfois tout seul quand tu l'utilises."),
    LOOT_GRAVITY(CurseCategory.MINAGE_INTERACTIONS, "Gravité des Loots", "Les objets minés tombent très lourdement au sol, ramasse-les vite !"),
    ORE_KARMA(CurseCategory.MINAGE_INTERACTIONS, "Karma des Minerais", "Miner des minerais a une chance d'invoquer un monstre."),

    // --- Catégorie 4 : Environnement & Entités (6) ---
    VEGETARIAN(CurseCategory.ENVIRONNEMENT_ENTITES, "Végétarien", "Manger de la viande t'empoisonne sévèrement."),
    HYDROPHOBE(CurseCategory.ENVIRONNEMENT_ENTITES, "Hydrophobe", "Toucher l'eau t'inflige des dégâts."),
    PRECARIOUS_BALANCE(CurseCategory.ENVIRONNEMENT_ENTITES, "Équilibre Précaire", "Les dégâts de chute sont accentués et te cassent les jambes."),
    EYE_CONTACT(CurseCategory.ENVIRONNEMENT_ENTITES, "Contact Visuel", "Regarder un monstre dans les yeux l'enrage instantanément vers toi."),
    VAMPIRE(CurseCategory.ENVIRONNEMENT_ENTITES, "Vampire", "Tu brûles au contact de la lumière directe du soleil."),
    AUDITORY_INSOMNIA(CurseCategory.ENVIRONNEMENT_ENTITES, "Insomnie Auditive", "Impossible de dormir s'il y a des bruits de monstres autour.");

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
