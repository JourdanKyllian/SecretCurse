package fr.siglah.secretCurse.infrastructure.paper.listener.camera;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * Syndrome Zoolander : toute rotation de caméra vers la gauche est annulée
 * et recadrée sur le yaw précédent.
 */
public class ZoolanderBehavior implements CurseBehavior {
    private final UUID targetId;

    public ZoolanderBehavior(JavaPlugin plugin, UUID targetId) {
        this.targetId = targetId;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!event.getPlayer().getUniqueId().equals(targetId)) return;

        float fromYaw = event.getFrom().getYaw();
        float toYaw = event.getTo().getYaw();
        float diff = toYaw - fromYaw;

        while (diff < -180.0F) diff += 360.0F;
        while (diff >= 180.0F) diff -= 360.0F;

        if (diff < -0.5F) { // Tente de tourner à gauche
            Location fixedTo = event.getTo().clone();
            fixedTo.setYaw(fromYaw);
            event.setTo(fixedTo);
        }
    }
}
