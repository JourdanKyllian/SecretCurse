package fr.siglah.secretCurse.infrastructure.paper.listener.camera;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.UUID;

public class FakeLagBehavior implements CurseBehavior {
    private final UUID targetId;
    private final Random random = new Random();

    public FakeLagBehavior(JavaPlugin plugin, UUID targetId) {
        this.targetId = targetId;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!event.getPlayer().getUniqueId().equals(targetId)) return;
        if (random.nextInt(100) < 5) {
            event.setCancelled(true);
        }
    }
}
