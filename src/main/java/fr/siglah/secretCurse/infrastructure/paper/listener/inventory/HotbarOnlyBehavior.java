package fr.siglah.secretCurse.infrastructure.paper.listener.inventory;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class HotbarOnlyBehavior implements CurseBehavior {
    private final JavaPlugin plugin;
    private final UUID targetId;
    private BukkitTask task;

    public HotbarOnlyBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
    }

    @Override
    public void onStart(Player player) {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Player p = Bukkit.getPlayer(targetId);
            if (p != null && p.isOnline() && !p.isDead()) {
                // Les slots 9 à 35 correspondent à l'inventaire principal (hors barre d'action)
                for (int i = 9; i <= 35; i++) {
                    ItemStack item = p.getInventory().getItem(i);
                    if (item != null && !item.getType().isAir()) {
                        p.getWorld().dropItemNaturally(p.getLocation(), item.clone());
                        p.getInventory().setItem(i, null);
                    }
                }
            }
        }, 10L, 10L); // Vérifie toutes les demi-secondes
    }

    @Override
    public void onStop(Player player) {
        if (task != null) task.cancel();
    }
}
