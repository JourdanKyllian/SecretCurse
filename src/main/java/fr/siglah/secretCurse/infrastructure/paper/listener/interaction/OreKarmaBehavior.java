package fr.siglah.secretCurse.infrastructure.paper.listener.interaction;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.UUID;

public class OreKarmaBehavior implements CurseBehavior {
    private final UUID targetId;
    private final Random random = new Random();

    public OreKarmaBehavior(JavaPlugin plugin, UUID targetId) {
        this.targetId = targetId;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().getUniqueId().equals(targetId)) return;

        String blockName = event.getBlock().getType().name();

        // Si c'est un minerai
        if (blockName.endsWith("_ORE")) {
            // 5% de chance de punition karmique
            if (random.nextInt(100) < 5) {
                Location loc = event.getBlock().getLocation().add(0.5, 0, 0.5); // Centre du bloc

                // Spawn un Silverfish ou un Zombie aléatoirement
                EntityType type = random.nextBoolean() ? EntityType.SILVERFISH : EntityType.ZOMBIE;
                loc.getWorld().spawnEntity(loc, type);
            }
        }
    }
}
