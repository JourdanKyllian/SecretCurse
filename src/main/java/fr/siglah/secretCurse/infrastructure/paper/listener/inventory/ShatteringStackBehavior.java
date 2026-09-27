package fr.siglah.secretCurse.infrastructure.paper.listener.inventory;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class ShatteringStackBehavior implements CurseBehavior {
    private final JavaPlugin plugin;
    private final UUID targetId;
    private BukkitTask task;

    public ShatteringStackBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
    }

    @Override
    public void onStart(Player player) {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Player p = Bukkit.getPlayer(targetId);
            if (p != null && p.isOnline() && !p.isDead()) {
                boolean playedSound = false;

                for (int i = 0; i < p.getInventory().getSize(); i++) {
                    ItemStack item = p.getInventory().getItem(i);
                    if (item != null && item.getAmount() == 64) {
                        item.setAmount(32); // Réduit la pile à 32
                        ItemStack dropped = item.clone();
                        p.getWorld().dropItemNaturally(p.getLocation(), dropped); // Jette la moitié

                        if (!playedSound) {
                            p.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 1.0f);
                            playedSound = true;
                        }
                    }
                }
            }
        }, 40L, 40L); // Toutes les 2 secondes
    }

    @Override
    public void onStop(Player player) {
        if (task != null) task.cancel();
    }
}
