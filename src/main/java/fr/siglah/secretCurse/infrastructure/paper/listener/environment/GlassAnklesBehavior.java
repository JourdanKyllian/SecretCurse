package fr.siglah.secretCurse.infrastructure.paper.listener.environment;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * Chevilles de Verre : toute chute, peu importe la hauteur, tue
 * instantanément. Les messages de mort par chute répétés dans le chat
 * finissent par trahir le joueur tout seuls.
 */
public class GlassAnklesBehavior implements CurseBehavior {
    private final UUID targetId;

    public GlassAnklesBehavior(JavaPlugin plugin, UUID targetId) {
        this.targetId = targetId;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!player.getUniqueId().equals(targetId)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        event.setDamage(1000.0);
    }
}
