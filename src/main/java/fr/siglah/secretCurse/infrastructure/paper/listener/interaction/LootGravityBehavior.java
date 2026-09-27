package fr.siglah.secretCurse.infrastructure.paper.listener.interaction;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.UUID;

public class LootGravityBehavior implements CurseBehavior {
    private final UUID targetId;

    public LootGravityBehavior(JavaPlugin plugin, UUID targetId) {
        this.targetId = targetId;
    }

    @EventHandler
    public void onBlockDrop(BlockDropItemEvent event) {
        if (!event.getPlayer().getUniqueId().equals(targetId)) return;

        for (Item item : event.getItems()) {
            // Applique une vélocité lourde vers le bas (au lieu du petit rebond classique de Minecraft)
            item.setVelocity(new Vector(0, -1.0, 0));
        }
    }
}
