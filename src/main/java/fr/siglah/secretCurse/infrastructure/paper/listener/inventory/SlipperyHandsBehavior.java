package fr.siglah.secretCurse.infrastructure.paper.listener.inventory;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;
import java.util.UUID;

public class SlipperyHandsBehavior implements CurseBehavior {
    private final JavaPlugin plugin;
    private final UUID targetId;
    private final Random random = new Random();
    private BukkitTask task;

    public SlipperyHandsBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
    }

    @Override
    public void onStart(Player player) {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Player p = Bukkit.getPlayer(targetId);
            if (p != null && p.isOnline() && !p.isDead()) {
                if (random.nextInt(100) < 25) { // 25% de chance
                    ItemStack hand = p.getInventory().getItemInMainHand();
                    if (!hand.getType().isAir()) {
                        p.getWorld().dropItemNaturally(p.getLocation(), hand.clone());
                        p.getInventory().setItemInMainHand(null);
                    }
                }
            }
        }, 80L, 80L);
    }

    @Override
    public void onStop(Player player) {
        if (task != null) task.cancel();
    }
}
