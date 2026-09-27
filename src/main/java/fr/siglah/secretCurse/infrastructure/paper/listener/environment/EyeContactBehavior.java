package fr.siglah.secretCurse.infrastructure.paper.listener.environment;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Bukkit;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;

import java.util.UUID;

public class EyeContactBehavior implements CurseBehavior {
    private final JavaPlugin plugin;
    private final UUID targetId;
    private BukkitTask task;

    public EyeContactBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
    }

    @Override
    public void onStart(Player player) {
        // Vérifie toutes les demi-secondes où regarde le joueur
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Player p = Bukkit.getPlayer(targetId);
            if (p != null && p.isOnline() && !p.isDead()) {

                // Trace un rayon de 25 blocs depuis les yeux du joueur
                RayTraceResult result = p.getWorld().rayTraceEntities(
                        p.getEyeLocation(),
                        p.getEyeLocation().getDirection(),
                        25.0,
                        entity -> entity instanceof Monster // Filtre uniquement les monstres
                );

                if (result != null && result.getHitEntity() instanceof Monster monster) {
                    monster.setTarget(p); // Enrage le monstre !
                }
            }
        }, 10L, 10L);
    }

    @Override
    public void onStop(Player player) {
        if (task != null) task.cancel();
    }
}
