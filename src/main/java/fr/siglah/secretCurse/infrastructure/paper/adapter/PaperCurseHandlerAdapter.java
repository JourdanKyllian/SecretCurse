package fr.siglah.secretCurse.infrastructure.paper.adapter;

import fr.siglah.secretCurse.domain.port.CurseHandlerPort;
import fr.siglah.secretCurse.domain.CurseType;
import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;

// Les imports complets pour toutes les catégories (même celles non créées)
import fr.siglah.secretCurse.infrastructure.paper.listener.camera.*;
import fr.siglah.secretCurse.infrastructure.paper.listener.inventory.*;
import fr.siglah.secretCurse.infrastructure.paper.listener.interaction.*;
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
        behaviorRegistry.put(CurseType.ANTI_RABBIT, AntiRabbitBehavior::new);
        behaviorRegistry.put(CurseType.STIFF_NECK, StiffNeckBehavior::new);
        behaviorRegistry.put(CurseType.COWARDLY_FIGHTER, CowardlyFighterBehavior::new);
        behaviorRegistry.put(CurseType.PACIFIST_JUMPER, PacifistJumperBehavior::new);

        // --- Catégorie 2 : Inventaire & Objets ---
        behaviorRegistry.put(CurseType.DEPENDENT_LEFTY, DependentLeftyBehavior::new);
        behaviorRegistry.put(CurseType.HOTBAR_ONLY, HotbarOnlyBehavior::new);
        behaviorRegistry.put(CurseType.EXCLUSIVITY_CONTRACT, ExclusivityContractBehavior::new);
        behaviorRegistry.put(CurseType.SLIPPERY_HANDS, SlipperyHandsBehavior::new);
        behaviorRegistry.put(CurseType.SHATTERING_STACK, ShatteringStackBehavior::new);
        behaviorRegistry.put(CurseType.CAPRICIOUS_GOURMET, CapriciousGourmetBehavior::new);

        // --- Catégorie 3 : Minage & Interactions ---
        behaviorRegistry.put(CurseType.DIRTY_HANDS, DirtyHandsBehavior::new);
        behaviorRegistry.put(CurseType.ALTITUDE, AltitudeBehavior::new);
        behaviorRegistry.put(CurseType.SUPERSTITION, SuperstitionBehavior::new);
        behaviorRegistry.put(CurseType.SHY_BUILDER, ShyBuilderBehavior::new);
        behaviorRegistry.put(CurseType.FEARFUL_CRAFTER, FearfulCrafterBehavior::new);
        behaviorRegistry.put(CurseType.LOOT_GRAVITY, LootGravityBehavior::new);
        behaviorRegistry.put(CurseType.ORE_KARMA, OreKarmaBehavior::new);

        // --- Catégorie 4 : Environnement & Entités ---
        behaviorRegistry.put(CurseType.VEGETARIAN, VegetarianBehavior::new);
        behaviorRegistry.put(CurseType.HYDROPHOBE, HydrophobeBehavior::new);
        behaviorRegistry.put(CurseType.PRECARIOUS_BALANCE, PrecariousBalanceBehavior::new);
        behaviorRegistry.put(CurseType.EYE_CONTACT, EyeContactBehavior::new);
        behaviorRegistry.put(CurseType.VAMPIRE, VampireBehavior::new);
        behaviorRegistry.put(CurseType.AUDITORY_INSOMNIA, AuditoryInsomniaBehavior::new);
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
            plugin.getLogger().info("Malédiction " + type.name() + " ACTIVÉE.");
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
            hider.sendMessage(Component.text("🤫 Défi Secret : " + type.name(), NamedTextColor.RED));
        }
    }

    @Override
    public void announceSuccess(UUID hiderId, CurseType type) {
        Title title = Title.title(
                Component.text("DÉFI TROUVÉ !", NamedTextColor.GREEN),
                Component.text("La malédiction était : " + type.name(), NamedTextColor.YELLOW)
        );
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showTitle(title);
        }
    }

    @Override
    public void announceFailure(UUID hiderId, CurseType type) {
        Title title = Title.title(
                Component.text("ÉCHEC DU CHERCHEUR", NamedTextColor.RED),
                Component.text("Le secret était : " + type.name(), NamedTextColor.GRAY)
        );
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showTitle(title);
        }
    }
}
