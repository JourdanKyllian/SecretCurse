package fr.siglah.secretCurse.infrastructure.paper.listener.environment;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

/**
 * Aimant à Monstres : tous les monstres dans un large rayon sont forcés de
 * cibler ce joueur, en écrasant leur cible actuelle (y compris un autre
 * joueur à proximité).
 */
public class MonsterMagnetBehavior implements CurseBehavior {
    private static final double RADIUS = 32.0;

    private final JavaPlugin plugin;
    private final UUID targetId;
    private BukkitTask task;

    public MonsterMagnetBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
    }

    @Override
    public void onStart(Player player) {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Player p = Bukkit.getPlayer(targetId);
            if (p == null || !p.isOnline() || p.isDead()) return;

            for (Entity entity : p.getNearbyEntities(RADIUS, RADIUS, RADIUS)) {
                if (entity instanceof Monster && entity instanceof Mob mob) {
                    LivingEntity currentTarget = mob.getTarget();
                    if (currentTarget == null || !currentTarget.getUniqueId().equals(targetId)) {
                        mob.setTarget(p);
                    }
                }
            }
        }, 20L, 20L);
    }

    @Override
    public void onStop(Player player) {
        if (task != null) task.cancel();
    }
}
