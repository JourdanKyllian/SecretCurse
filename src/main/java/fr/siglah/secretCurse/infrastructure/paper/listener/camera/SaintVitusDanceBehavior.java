package fr.siglah.secretCurse.infrastructure.paper.listener.camera;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.UUID;

/**
 * Danse de Saint-Guy : toutes les 20 à 40 secondes, un saut incontrôlable
 * (même mouvement vertical qu'un saut normal) secoue le joueur.
 */
public class SaintVitusDanceBehavior implements CurseBehavior {
    private static final double JUMP_VELOCITY = 0.42; // Vitesse verticale d'un saut vanilla

    private final JavaPlugin plugin;
    private final UUID targetId;
    private final Random random = new Random();
    private BukkitTask task;

    public SaintVitusDanceBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
    }

    @Override
    public void onStart(Player player) {
        scheduleNextJump();
    }

    @Override
    public void onStop(Player player) {
        if (task != null) task.cancel();
    }

    private void scheduleNextJump() {
        int seconds = 20 + random.nextInt(21); // 20 à 40 inclus
        task = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Player p = Bukkit.getPlayer(targetId);
            if (p != null && p.isOnline() && !p.isDead() && p.isOnGround()) {
                Vector velocity = p.getVelocity();
                p.setVelocity(new Vector(velocity.getX(), JUMP_VELOCITY, velocity.getZ()));
            }
            scheduleNextJump();
        }, seconds * 20L);
    }
}
