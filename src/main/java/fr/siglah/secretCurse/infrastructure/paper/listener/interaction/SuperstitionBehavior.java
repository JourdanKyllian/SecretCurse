package fr.siglah.secretCurse.infrastructure.paper.listener.interaction;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class SuperstitionBehavior implements CurseBehavior {
    private final UUID targetId;

    public SuperstitionBehavior(JavaPlugin plugin, UUID targetId) {
        this.targetId = targetId;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().getUniqueId().equals(targetId)) return;

        String blockName = event.getBlock().getType().name();
        if (blockName.contains("DIAMOND_ORE") || blockName.contains("EMERALD_ORE") ||
                blockName.contains("GOLD_ORE") || blockName.equals("ANCIENT_DEBRIS")) {

            // Malchance (baisse le loot des tables de butin) et Fatigue pour 15 secondes
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, 300, 1, false, false));
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 300, 0, false, false));
        }
    }
}
