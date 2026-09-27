package fr.siglah.secretCurse.infrastructure.paper.listener.environment;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class PrecariousBalanceBehavior implements CurseBehavior {
    private final UUID targetId;

    public PrecariousBalanceBehavior(JavaPlugin plugin, UUID targetId) {
        this.targetId = targetId;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!player.getUniqueId().equals(targetId)) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            // Multiplie les dégâts par 1.5
            event.setDamage(event.getDamage() * 1.5);

            // Lenteur niveau 3 pendant 4 secondes (80 ticks)
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 80, 2, false, false));
        }
    }
}
