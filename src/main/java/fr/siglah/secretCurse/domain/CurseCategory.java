package fr.siglah.secretCurse.domain;

/**
 * Catégorie thématique d'une {@link CurseType}, utilisée pour organiser
 * le menu "Malédiction Cachée" en onglets.
 */
public enum CurseCategory {
    DEPLACEMENT_CAMERA("Déplacements & Caméra"),
    INVENTAIRE_OBJETS("Inventaire & Objets"),
    MINAGE_INTERACTIONS("Minage & Interactions"),
    ENVIRONNEMENT_ENTITES("Environnement & Entités");

    private final String displayName;

    CurseCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
