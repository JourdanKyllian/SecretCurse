package fr.siglah.secretCurse.presentation.gui;

import fr.siglah.secretCurse.domain.CurseCategory;
import fr.siglah.secretCurse.domain.CurseType;
import fr.siglah.secretCurse.domain.DeathCondition;
import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;

public final class GuiIcons {

    private GuiIcons() {}

    private static final Map<CurseCategory, Material> CATEGORY_ICONS = new EnumMap<>(CurseCategory.class);
    private static final Map<CurseType, Material> CURSE_ICONS = new EnumMap<>(CurseType.class);
    private static final Map<DeathCondition, Material> DEATH_ICONS = new EnumMap<>(DeathCondition.class);

    static {
        CATEGORY_ICONS.put(CurseCategory.DEPLACEMENT_CAMERA, Material.FEATHER);
        CATEGORY_ICONS.put(CurseCategory.INVENTAIRE_OBJETS, Material.CHEST);
        CATEGORY_ICONS.put(CurseCategory.MINAGE_INTERACTIONS, Material.IRON_PICKAXE);
        CATEGORY_ICONS.put(CurseCategory.ENVIRONNEMENT_ENTITES, Material.ZOMBIE_HEAD);

        // Cat 1 : Déplacements & Caméra
        CURSE_ICONS.put(CurseType.ZOOLANDER, Material.COMPASS);
        CURSE_ICONS.put(CurseType.FAKE_LAG, Material.CLOCK);
        CURSE_ICONS.put(CurseType.COMPULSIVE_SPRINTER, Material.RABBIT_FOOT);
        CURSE_ICONS.put(CurseType.SAINT_VITUS_DANCE, Material.RABBIT_HIDE);
        CURSE_ICONS.put(CurseType.PHANTOM_WEIGHT, Material.PHANTOM_MEMBRANE);
        CURSE_ICONS.put(CurseType.SPIDER_IMPRINT, Material.SPIDER_EYE);

        // Cat 2 : Inventaire & Objets
        CURSE_ICONS.put(CurseType.DIRTY_HANDS_ALLERGY, Material.SPONGE);
        CURSE_ICONS.put(CurseType.FORCED_VEGETARIAN, Material.CARROT);
        CURSE_ICONS.put(CurseType.HOLED_INVENTORY, Material.DROPPER);
        CURSE_ICONS.put(CurseType.IRRESISTIBLE_CRAVING, Material.SUGAR);
        CURSE_ICONS.put(CurseType.FORCED_MINIMALISM, Material.BUNDLE);

        // Cat 3 : Minage & Interactions
        CURSE_ICONS.put(CurseType.ORE_WHEEL, Material.DIAMOND_ORE);
        CURSE_ICONS.put(CurseType.INNER_FURNACE, Material.FURNACE);

        // Cat 4 : Environnement & Entités
        CURSE_ICONS.put(CurseType.CREEPER_SYNDROME, Material.CREEPER_HEAD);
        CURSE_ICONS.put(CurseType.WATER_RAGE, Material.WATER_BUCKET);
        CURSE_ICONS.put(CurseType.VAMPIRE, Material.SOUL_CAMPFIRE);
        CURSE_ICONS.put(CurseType.MONSTER_MAGNET, Material.MAGMA_CREAM);
        CURSE_ICONS.put(CurseType.GLASS_ANKLES, Material.LEATHER_BOOTS);
        CURSE_ICONS.put(CurseType.UNSTABLE_LOTTERY, Material.BREWING_STAND);

        DEATH_ICONS.put(DeathCondition.LAVA, Material.LAVA_BUCKET);
        DEATH_ICONS.put(DeathCondition.FALL_DAMAGE, Material.FEATHER);
        DEATH_ICONS.put(DeathCondition.DROWNING, Material.WATER_BUCKET);
        DEATH_ICONS.put(DeathCondition.ZOMBIE, Material.ZOMBIE_HEAD);
        DEATH_ICONS.put(DeathCondition.CREEPER_EXPLOSION, Material.CREEPER_HEAD);
        DEATH_ICONS.put(DeathCondition.SKELETON_ARROW, Material.ARROW);
        DEATH_ICONS.put(DeathCondition.FIRE, Material.FLINT_AND_STEEL);
        DEATH_ICONS.put(DeathCondition.STARVATION, Material.ROTTEN_FLESH);
        DEATH_ICONS.put(DeathCondition.CACTUS, Material.CACTUS);
        DEATH_ICONS.put(DeathCondition.ANVIL, Material.ANVIL);
        DEATH_ICONS.put(DeathCondition.STALACTITE, Material.POINTED_DRIPSTONE);
        DEATH_ICONS.put(DeathCondition.SUFFOCATION, Material.SAND);
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
