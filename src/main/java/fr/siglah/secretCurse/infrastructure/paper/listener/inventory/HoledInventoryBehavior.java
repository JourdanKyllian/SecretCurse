package fr.siglah.secretCurse.infrastructure.paper.listener.inventory;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Inventaire Troué : toutes les 30 à 45 secondes, une seule unité d'un objet
 * aléatoire de la hotbar tombe au sol (pas le stack entier), sans son.
 */
public class HoledInventoryBehavior implements CurseBehavior {
    private final JavaPlugin plugin;
    private final UUID targetId;
    private final Random random = new Random();
    private BukkitTask task;

    public HoledInventoryBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
    }

    @Override
    public void onStart(Player player) {
        scheduleNextDrop();
    }

    @Override
    public void onStop(Player player) {
        if (task != null) task.cancel();
    }

    private void scheduleNextDrop() {
        int seconds = 30 + random.nextInt(16); // 30 à 45 inclus
        task = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            dropOneRandomUnit();
            scheduleNextDrop();
        }, seconds * 20L);
    }

    private void dropOneRandomUnit() {
        Player p = Bukkit.getPlayer(targetId);
        if (p == null || !p.isOnline() || p.isDead()) return;

        List<Integer> occupiedSlots = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item != null && !item.getType().isAir()) {
                occupiedSlots.add(i);
            }
        }
        if (occupiedSlots.isEmpty()) return;

        int slot = occupiedSlots.get(random.nextInt(occupiedSlots.size()));
        ItemStack item = p.getInventory().getItem(slot);

        ItemStack singleUnit = item.clone();
        singleUnit.setAmount(1);
        p.getWorld().dropItemNaturally(p.getLocation(), singleUnit);

        if (item.getAmount() <= 1) {
            p.getInventory().setItem(slot, null);
        } else {
            item.setAmount(item.getAmount() - 1);
            p.getInventory().setItem(slot, item); // Réécrit explicitement au cas où getItem() renvoie une copie
        }
    }
}
