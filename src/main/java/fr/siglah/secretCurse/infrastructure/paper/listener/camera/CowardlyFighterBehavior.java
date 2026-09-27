package fr.siglah.secretCurse.infrastructure.paper.listener.camera;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class CowardlyFighterBehavior implements CurseBehavior {
    private final UUID targetId;

    public CowardlyFighterBehavior(JavaPlugin plugin, UUID targetId) {
        this.targetId = targetId;
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!player.getUniqueId().equals(targetId)) return;

        // Si le joueur tape une entité, il "panique" (Vitesse extrême + Cécité pour 2 secondes)
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 4, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0, false, false));
    }
}
