package fr.siglah.secretCurse.infrastructure.paper.listener.camera;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class PacifistJumperBehavior implements CurseBehavior {
    private final UUID targetId;

    public PacifistJumperBehavior(JavaPlugin plugin, UUID targetId) {
        this.targetId = targetId;
    }

    @EventHandler
    public void onJump(PlayerJumpEvent event) {
        if (!event.getPlayer().getUniqueId().equals(targetId)) return;

        String itemInHand = event.getPlayer().getInventory().getItemInMainHand().getType().name();

        // Annule le saut si le joueur tient une épée, hache, arc ou trident
        if (itemInHand.endsWith("_SWORD") || itemInHand.endsWith("_AXE") ||
                itemInHand.equals("BOW") || itemInHand.equals("CROSSBOW") ||
                itemInHand.equals("TRIDENT") || itemInHand.equals("MACE")) {

            event.setCancelled(true);
        }
    }
}
