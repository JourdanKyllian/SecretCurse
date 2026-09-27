package fr.siglah.secretCurse.domain;

/**
 * Condition de mort désignée par la roue du mode "Qui mourra le premier ?".
 * Le premier joueur qui meurt de cette façon précise remporte la manche.
 */
public enum DeathCondition {
    LAVA("Lave", "Mourir plongé dans la lave"),
    FALL_DAMAGE("Chute", "Mourir de dégâts de chute"),
    DROWNING("Noyade", "Mourir noyé"),
    ZOMBIE("Zombie", "Mourir tué par un zombie"),
    CREEPER_EXPLOSION("Explosion de Creeper", "Mourir dans l'explosion d'un creeper"),
    SKELETON_ARROW("Flèche de Squelette", "Mourir sous les flèches d'un squelette"),
    FIRE("Feu", "Mourir brûlé vif"),
    STARVATION("Faim", "Mourir de faim");

    private final String displayName;
    private final String description;

    DeathCondition(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
