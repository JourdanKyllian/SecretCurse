package fr.siglah.secretCurse.infrastructure.paper.listener.interaction;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class AltitudeBehavior implements CurseBehavior {
    private final UUID targetId;

    public AltitudeBehavior(JavaPlugin plugin, UUID targetId) {
        this.targetId = targetId;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().getUniqueId().equals(targetId)) return;

        if (event.getBlock().getY() > 100) {
            // Nausée pendant 5 secondes (100 ticks)
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 100, 0, false, false));
        }
    }
}
