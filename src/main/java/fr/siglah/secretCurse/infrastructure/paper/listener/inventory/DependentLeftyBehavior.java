package fr.siglah.secretCurse.infrastructure.paper.listener.inventory;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class DependentLeftyBehavior implements CurseBehavior {
    private final UUID targetId;

    public DependentLeftyBehavior(JavaPlugin plugin, UUID targetId) {
        this.targetId = targetId;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().getUniqueId().equals(targetId)) return;
        if (isTool(event.getPlayer().getInventory().getItemInMainHand().getType())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!player.getUniqueId().equals(targetId)) return;
        if (isTool(player.getInventory().getItemInMainHand().getType())) {
            event.setCancelled(true);
        }
    }

    private boolean isTool(Material mat) {
        String name = mat.name();
        return name.endsWith("_PICKAXE") || name.endsWith("_AXE") ||
                name.endsWith("_SHOVEL") || name.endsWith("_HOE") ||
                name.endsWith("_SWORD") || name.equals("BOW") ||
                name.equals("CROSSBOW") || name.equals("TRIDENT") || name.equals("MACE");
    }
}
