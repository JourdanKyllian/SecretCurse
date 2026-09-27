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

        // Cat 1 : Déplacements
        CURSE_ICONS.put(CurseType.ZOOLANDER, Material.COMPASS);
        CURSE_ICONS.put(CurseType.FAKE_LAG, Material.CLOCK);
        CURSE_ICONS.put(CurseType.ANTI_RABBIT, Material.RABBIT_FOOT);
        CURSE_ICONS.put(CurseType.STIFF_NECK, Material.CHAIN);
        CURSE_ICONS.put(CurseType.COWARDLY_FIGHTER, Material.GHAST_TEAR);
        CURSE_ICONS.put(CurseType.PACIFIST_JUMPER, Material.WHITE_WOOL);

        // Cat 2 : Inventaire
        CURSE_ICONS.put(CurseType.DEPENDENT_LEFTY, Material.SHIELD);
        CURSE_ICONS.put(CurseType.HOTBAR_ONLY, Material.BARRIER);
        CURSE_ICONS.put(CurseType.EXCLUSIVITY_CONTRACT, Material.WRITABLE_BOOK);
        CURSE_ICONS.put(CurseType.SLIPPERY_HANDS, Material.SLIME_BALL);
        CURSE_ICONS.put(CurseType.SHATTERING_STACK, Material.GLASS_PANE);
        CURSE_ICONS.put(CurseType.CAPRICIOUS_GOURMET, Material.CAKE);

        // Cat 3 : Minage & Interactions
        CURSE_ICONS.put(CurseType.DIRTY_HANDS, Material.DIRT);
        CURSE_ICONS.put(CurseType.ALTITUDE, Material.SPYGLASS);
        CURSE_ICONS.put(CurseType.SUPERSTITION, Material.BLACK_CAT_SPAWN_EGG);
        CURSE_ICONS.put(CurseType.SHY_BUILDER, Material.ENDER_PEARL);
        CURSE_ICONS.put(CurseType.FEARFUL_CRAFTER, Material.CRAFTING_TABLE);
        CURSE_ICONS.put(CurseType.LOOT_GRAVITY, Material.ANVIL);
        CURSE_ICONS.put(CurseType.ORE_KARMA, Material.DIAMOND_ORE);

        // Cat 4 : Environnement
        CURSE_ICONS.put(CurseType.VEGETARIAN, Material.CARROT);
        CURSE_ICONS.put(CurseType.HYDROPHOBE, Material.WATER_BUCKET);
        CURSE_ICONS.put(CurseType.PRECARIOUS_BALANCE, Material.LEATHER_BOOTS);
        CURSE_ICONS.put(CurseType.EYE_CONTACT, Material.SPIDER_EYE);
        CURSE_ICONS.put(CurseType.VAMPIRE, Material.SOUL_CAMPFIRE);
        CURSE_ICONS.put(CurseType.AUDITORY_INSOMNIA, Material.NOTE_BLOCK);

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
