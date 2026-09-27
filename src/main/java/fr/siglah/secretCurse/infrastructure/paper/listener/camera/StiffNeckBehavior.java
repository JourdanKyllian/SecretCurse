package fr.siglah.secretCurse.infrastructure.paper.listener.camera;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class StiffNeckBehavior implements CurseBehavior {
    private final UUID targetId;

    public StiffNeckBehavior(JavaPlugin plugin, UUID targetId) {
        this.targetId = targetId;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!event.getPlayer().getUniqueId().equals(targetId)) return;

        // Le pitch représente l'axe vertical de la tête (0 = droit devant)
        if (event.getTo().getPitch() != 0.0F) {
            Location fixedTo = event.getTo().clone();
            fixedTo.setPitch(0.0F); // Bloque la tête à l'horizontale
            event.setTo(fixedTo);
        }
    }
}
