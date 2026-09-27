package fr.siglah.secretCurse.presentation.gui;

import fr.siglah.secretCurse.domain.CurseCategory;
import fr.siglah.secretCurse.domain.CurseType;
import fr.siglah.secretCurse.domain.DeathCondition;
import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;

/**
 * Seul endroit du code qui sait à quel Material correspond une valeur du domaine.
 * Le domaine (CurseType, CurseCategory, DeathCondition) reste ainsi totalement
 * indépendant de Bukkit/Paper.
 */
public final class GuiIcons {

    private GuiIcons() {
    }

    private static final Map<CurseCategory, Material> CATEGORY_ICONS = new EnumMap<>(CurseCategory.class);
    private static final Map<CurseType, Material> CURSE_ICONS = new EnumMap<>(CurseType.class);
    private static final Map<DeathCondition, Material> DEATH_ICONS = new EnumMap<>(DeathCondition.class);

    static {
        CATEGORY_ICONS.put(CurseCategory.DEPLACEMENT_CAMERA, Material.FEATHER);
        CATEGORY_ICONS.put(CurseCategory.INVENTAIRE_OBJETS, Material.CHEST);
        CATEGORY_ICONS.put(CurseCategory.MINAGE_INTERACTIONS, Material.IRON_PICKAXE);
        CATEGORY_ICONS.put(CurseCategory.ENVIRONNEMENT_ENTITES, Material.ZOMBIE_HEAD);

        CURSE_ICONS.put(CurseType.ZOOLANDER, Material.COMPASS);
        CURSE_ICONS.put(CurseType.FAKE_LAG, Material.CLOCK);
        CURSE_ICONS.put(CurseType.COMPULSIVE_SPRINTER, Material.RABBIT_FOOT);
        CURSE_ICONS.put(CurseType.DIRTY_HANDS_ALLERGY, Material.SPONGE);
        CURSE_ICONS.put(CurseType.FORCED_VEGETARIAN, Material.CARROT);
        CURSE_ICONS.put(CurseType.MINER_MYOPIA, Material.STONE_PICKAXE);
        CURSE_ICONS.put(CurseType.BUTTER_FINGERS, Material.SLIME_BALL);
        CURSE_ICONS.put(CurseType.SHARK_SYNDROME, Material.TROPICAL_FISH);
        CURSE_ICONS.put(CurseType.ALTITUDE_INSOMNIA, Material.RED_BED);
        CURSE_ICONS.put(CurseType.THERMAL_VISION, Material.SPYGLASS);

        DEATH_ICONS.put(DeathCondition.LAVA, Material.LAVA_BUCKET);
        DEATH_ICONS.put(DeathCondition.FALL_DAMAGE, Material.FEATHER);
        DEATH_ICONS.put(DeathCondition.DROWNING, Material.WATER_BUCKET);
        DEATH_ICONS.put(DeathCondition.ZOMBIE, Material.ZOMBIE_HEAD);
        DEATH_ICONS.put(DeathCondition.CREEPER_EXPLOSION, Material.CREEPER_HEAD);
        DEATH_ICONS.put(DeathCondition.SKELETON_ARROW, Material.ARROW);
        DEATH_ICONS.put(DeathCondition.FIRE, Material.FLINT_AND_STEEL);
        DEATH_ICONS.put(DeathCondition.STARVATION, Material.ROTTEN_FLESH);
    }

    public static Material iconFor(CurseCategory category) {
        return CATEGORY_ICONS.getOrDefault(category, Material.PAPER);
    }

    public static Material iconFor(CurseType curse) {
        return CURSE_ICONS.getOrDefault(curse, Material.PAPER);
    }

    public static Material iconFor(DeathCondition condition) {
        return DEATH_ICONS.getOrDefault(condition, Material.PAPER);
    }
}
