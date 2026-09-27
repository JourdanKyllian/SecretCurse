package fr.siglah.secretCurse.infrastructure.paper.listener.interaction;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

public class DirtyHandsBehavior implements CurseBehavior {
    private final UUID targetId;
    private static final Set<Material> DIRTY_BLOCKS = EnumSet.of(
            Material.DIRT, Material.COARSE_DIRT, Material.ROOTED_DIRT,
            Material.SAND, Material.RED_SAND, Material.GRAVEL,
            Material.MUD, Material.PODZOL, Material.MYCELIUM, Material.SOUL_SAND
    );

    public DirtyHandsBehavior(JavaPlugin plugin, UUID targetId) {
        this.targetId = targetId;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().getUniqueId().equals(targetId)) return;

        if (DIRTY_BLOCKS.contains(event.getBlock().getType())) {
            // Cécité pendant 3 secondes (60 ticks)
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0, false, false));
        }
    }
}
