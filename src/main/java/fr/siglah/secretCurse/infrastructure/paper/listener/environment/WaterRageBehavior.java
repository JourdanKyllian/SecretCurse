package fr.siglah.secretCurse.infrastructure.paper.listener.environment;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

/**
 * Rage Hydrique : être en contact avec l'eau (partiel ou totalement immergé)
 * inflige un empoisonnement en continu.
 * Utilise une boucle active pour ne pas dépendre des mouvements du joueur.
 */
public class WaterRageBehavior implements CurseBehavior {
    private final JavaPlugin plugin;
    private final UUID targetId;
    private BukkitTask task;

    public WaterRageBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
    }

    @Override
    public void onStart(Player player) {
        // Vérification toutes les demi-secondes (10 ticks)
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Player p = Bukkit.getPlayer(targetId);
            if (p == null || !p.isOnline() || p.isDead()) return;

            // isInWater() vérifie de manière très fiable si n'importe quelle
            // partie de la hitbox du joueur (pieds, corps, tête) touche de l'eau
            if (p.isInWater()) {
                // Applique ou rafraîchit le poison pour 3 secondes (60 ticks)
                p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 0));
            }
        }, 10L, 10L);
    }

    @Override
    public void onStop(Player player) {
        if (task != null) task.cancel();

        // Retire le poison restant si on arrête la malédiction alors qu'il est dans l'eau
        if (player != null && player.isOnline()) {
            player.removePotionEffect(PotionEffectType.POISON);
        }
    }
}
