package fr.siglah.secretCurse.infrastructure.paper.listener.environment;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class VampireBehavior implements CurseBehavior {
    private final JavaPlugin plugin;
    private final UUID targetId;
    private BukkitTask task;

    public VampireBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
    }

    @Override
    public void onStart(Player player) {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Player p = Bukkit.getPlayer(targetId);
            if (p != null && p.isOnline() && !p.isDead()) {
                long time = p.getWorld().getTime();

                // Le jour dans Minecraft se situe entre 0 et 12300 ticks
                boolean isDaytime = time < 12300 || time > 23850;

                if (isDaytime && !p.getWorld().hasStorm()) {
                    // Vérifie si le bloc au niveau de la tête reçoit la lumière maximale du ciel
                    if (p.getEyeLocation().getBlock().getLightFromSky() == 15) {
                        p.setFireTicks(60); // S'enflamme pour 3 secondes
                    }
                }
            }
        }, 20L, 20L); // Vérification chaque seconde
    }

    @Override
    public void onStop(Player player) {
        if (task != null) task.cancel();
    }
}
