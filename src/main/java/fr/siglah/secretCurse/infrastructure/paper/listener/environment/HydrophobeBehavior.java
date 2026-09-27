package fr.siglah.secretCurse.infrastructure.paper.listener.environment;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class HydrophobeBehavior implements CurseBehavior {
    private final JavaPlugin plugin;
    private final UUID targetId;
    private BukkitTask task;

    public HydrophobeBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
    }

    @Override
    public void onStart(Player player) {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Player p = Bukkit.getPlayer(targetId);
            if (p != null && p.isOnline() && !p.isDead()) {
                if (p.isInWater()) {
                    p.damage(2.0); // 1 cœur de dégât par seconde passée dans l'eau
                }
            }
        }, 20L, 20L); // 20 ticks = 1 seconde
    }

    @Override
    public void onStop(Player player) {
        if (task != null) task.cancel();
    }
}
