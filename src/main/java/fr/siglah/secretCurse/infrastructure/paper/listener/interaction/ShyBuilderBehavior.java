package fr.siglah.secretCurse.infrastructure.paper.listener.interaction;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class ShyBuilderBehavior implements CurseBehavior {
    private final UUID targetId;

    public ShyBuilderBehavior(JavaPlugin plugin, UUID targetId) {
        this.targetId = targetId;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!player.getUniqueId().equals(targetId)) return;

        // Vérifie les joueurs dans un rayon de 15 blocs
        for (Entity entity : player.getNearbyEntities(15, 15, 15)) {
            if (entity instanceof Player observer) {
                // Si le joueur nous voit directement
                if (observer.hasLineOfSight(player)) {
                    event.setCancelled(true);
                    player.sendActionBar(Component.text("Tu es trop timide pour construire devant " + observer.getName() + " !", NamedTextColor.RED));
                    return;
                }
            }
        }
    }
}
