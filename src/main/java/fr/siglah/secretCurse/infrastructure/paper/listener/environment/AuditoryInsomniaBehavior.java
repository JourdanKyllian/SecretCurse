package fr.siglah.secretCurse.infrastructure.paper.listener.environment;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class AuditoryInsomniaBehavior implements CurseBehavior {
    private final UUID targetId;

    public AuditoryInsomniaBehavior(JavaPlugin plugin, UUID targetId) {
        this.targetId = targetId;
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        if (!event.getPlayer().getUniqueId().equals(targetId)) return;

        // Cherche une entité de type "Monster" (Zombie, Creeper, etc.) dans un cube de 16x16x16
        boolean monsterNearby = false;
        for (Entity entity : event.getPlayer().getNearbyEntities(16, 16, 16)) {
            if (entity instanceof Monster) {
                monsterNearby = true;
                break;
            }
        }

        if (monsterNearby) {
            event.setCancelled(true);
            event.getPlayer().sendActionBar(Component.text("Les bruits autour t'empêchent de fermer l'œil !", NamedTextColor.RED));
        }
    }
}
