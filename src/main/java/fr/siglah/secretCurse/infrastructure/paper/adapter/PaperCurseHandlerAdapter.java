package fr.siglah.secretCurse.infrastructure.paper.adapter;

import fr.siglah.secretCurse.domain.port.CurseHandlerPort;
import fr.siglah.secretCurse.domain.CurseType;
import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;

import fr.siglah.secretCurse.infrastructure.paper.listener.camera.*;
import fr.siglah.secretCurse.infrastructure.paper.listener.inventory.*;
import fr.siglah.secretCurse.infrastructure.paper.listener.mining.*;
import fr.siglah.secretCurse.infrastructure.paper.listener.environment.*;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

public class PaperCurseHandlerAdapter implements CurseHandlerPort {
    private final JavaPlugin plugin;
    private final Map<CurseType, BiFunction<JavaPlugin, UUID, CurseBehavior>> behaviorRegistry;

    private CurseBehavior activeBehavior = null;
    private UUID currentHiderId = null;

    public PaperCurseHandlerAdapter(JavaPlugin plugin) {
        this.plugin = plugin;
        this.behaviorRegistry = new EnumMap<>(CurseType.class);
        registerBehaviors();
    }

    private void registerBehaviors() {
        // --- Catégorie 1 : Déplacements & Caméra ---
        behaviorRegistry.put(CurseType.ZOOLANDER, ZoolanderBehavior::new);
        behaviorRegistry.put(CurseType.FAKE_LAG, FakeLagBehavior::new);
        behaviorRegistry.put(CurseType.COMPULSIVE_SPRINTER, CompulsiveSprinterBehavior::new);
        behaviorRegistry.put(CurseType.SAINT_VITUS_DANCE, SaintVitusDanceBehavior::new);
        behaviorRegistry.put(CurseType.PHANTOM_WEIGHT, PhantomWeightBehavior::new);
        behaviorRegistry.put(CurseType.SPIDER_IMPRINT, SpiderImprintBehavior::new);

        // --- Catégorie 2 : Inventaire & Objets ---
        behaviorRegistry.put(CurseType.DIRTY_HANDS_ALLERGY, DirtyHandsAllergyBehavior::new);
        behaviorRegistry.put(CurseType.FORCED_VEGETARIAN, ForcedVegetarianBehavior::new);
        behaviorRegistry.put(CurseType.HOLED_INVENTORY, HoledInventoryBehavior::new);
        behaviorRegistry.put(CurseType.IRRESISTIBLE_CRAVING, IrresistibleCravingBehavior::new);
        behaviorRegistry.put(CurseType.FORCED_MINIMALISM, ForcedMinimalismBehavior::new);

        // --- Catégorie 3 : Minage & Interactions ---
        behaviorRegistry.put(CurseType.ORE_WHEEL, OreWheelBehavior::new);
        behaviorRegistry.put(CurseType.INNER_FURNACE, InnerFurnaceBehavior::new);

        // --- Catégorie 4 : Environnement & Entités ---
        behaviorRegistry.put(CurseType.CREEPER_SYNDROME, CreeperSyndromeBehavior::new);
        behaviorRegistry.put(CurseType.WATER_RAGE, WaterRageBehavior::new);
        behaviorRegistry.put(CurseType.VAMPIRE, VampireBehavior::new);
        behaviorRegistry.put(CurseType.MONSTER_MAGNET, MonsterMagnetBehavior::new);
        behaviorRegistry.put(CurseType.GLASS_ANKLES, GlassAnklesBehavior::new);
        behaviorRegistry.put(CurseType.UNSTABLE_LOTTERY, UnstableLotteryBehavior::new);
    }

    @Override
    public void enableCurse(CurseType type, UUID hiderId) {
        disableCurse();
        this.currentHiderId = hiderId;

        BiFunction<JavaPlugin, UUID, CurseBehavior> factory = behaviorRegistry.get(type);
        if (factory != null) {
            this.activeBehavior = factory.apply(plugin, hiderId);
            Player player = Bukkit.getPlayer(hiderId);
            if (player != null) activeBehavior.onStart(player);

            Bukkit.getPluginManager().registerEvents(activeBehavior, plugin);
            plugin.getLogger().info("Malédiction " + type.getDisplayName() + " ACTIVÉE.");
        }
    }

    @Override
    public void disableCurse() {
        if (activeBehavior != null) {
            HandlerList.unregisterAll(activeBehavior);
            if (currentHiderId != null) {
                Player player = Bukkit.getPlayer(currentHiderId);
                if (player != null) activeBehavior.onStop(player);
            }
            activeBehavior = null;
            currentHiderId = null;
            plugin.getLogger().info("Malédiction DÉSACTIVÉE.");
        }
    }

    @Override
    public void sendPrivateBriefing(UUID hiderId, CurseType type) {
        Player hider = Bukkit.getPlayer(hiderId);
        if (hider != null) {
            hider.sendMessage(Component.text("🤫 Défi Secret : " + type.getDisplayName(), NamedTextColor.RED));
            hider.sendMessage(Component.text(type.getDescription(), NamedTextColor.GRAY));
        }
    }

    @Override
    public void announceSuccess(UUID hiderId, CurseType type) {
        Title title = Title.title(
                Component.text("DÉFI TROUVÉ !", NamedTextColor.GREEN),
                Component.text("La malédiction était : " + type.getDisplayName(), NamedTextColor.YELLOW)
        );
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showTitle(title);
        }
    }

    @Override
    public void announceFailure(UUID hiderId, CurseType type) {
        Title title = Title.title(
                Component.text("ÉCHEC DU CHERCHEUR", NamedTextColor.RED),
                Component.text("Le secret était : " + type.getDisplayName(), NamedTextColor.GRAY)
        );
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showTitle(title);
        }
    }
}
