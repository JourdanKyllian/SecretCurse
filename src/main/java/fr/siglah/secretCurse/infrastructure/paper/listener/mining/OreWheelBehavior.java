package fr.siglah.secretCurse.infrastructure.paper.listener.mining;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

/**
 * La Roue des Minerais : Charbon → Fer, Fer → Or, Or → Diamant, Diamant →
 * Charbon. Miner un de ces blocs (surface ou deepslate) fait apparaître
 * l'item du minerai suivant dans le cycle, à la place du drop normal.
 */
public class OreWheelBehavior implements CurseBehavior {
    private final UUID targetId;

    // Bloc miné -> item qui apparaît à la place (le "minerai suivant" dans le cycle)
    private static final Map<Material, ItemStack> SUBSTITUTIONS = new EnumMap<>(Material.class);

    static {
        register(Material.COAL_ORE, Material.RAW_IRON);
        register(Material.DEEPSLATE_COAL_ORE, Material.RAW_IRON);

        register(Material.IRON_ORE, Material.RAW_GOLD);
        register(Material.DEEPSLATE_IRON_ORE, Material.RAW_GOLD);

        register(Material.GOLD_ORE, Material.DIAMOND);
        register(Material.DEEPSLATE_GOLD_ORE, Material.DIAMOND);
        register(Material.NETHER_GOLD_ORE, Material.DIAMOND);

        register(Material.DIAMOND_ORE, Material.COAL);
        register(Material.DEEPSLATE_DIAMOND_ORE, Material.COAL);
    }

    private static void register(Material oreBlock, Material substituteItem) {
        SUBSTITUTIONS.put(oreBlock, new ItemStack(substituteItem, 1));
    }

    public OreWheelBehavior(JavaPlugin plugin, UUID targetId) {
        this.targetId = targetId;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().getUniqueId().equals(targetId)) return;

        ItemStack substitute = SUBSTITUTIONS.get(event.getBlock().getType());
        if (substitute == null) return;

        event.setDropItems(false);
        event.getBlock().getWorld().dropItemNaturally(
                event.getBlock().getLocation().add(0.5, 0.5, 0.5),
                substitute.clone()
        );
    }
}
