package fr.siglah.secretCurse.infrastructure.paper.listener.interaction;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;
import java.util.UUID;

public class FearfulCrafterBehavior implements CurseBehavior {
    private final JavaPlugin plugin;
    private final UUID targetId;
    private final Random random = new Random();
    private BukkitTask task;

    public FearfulCrafterBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
    }

    @Override
    public void onStart(Player player) {
        // Vérifie toutes les secondes si l'inventaire actif est un établi
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Player p = Bukkit.getPlayer(targetId);
            if (p != null && p.isOnline()) {
                if (p.getOpenInventory().getType() == InventoryType.WORKBENCH) {
                    // 20% de chance de le fermer de peur
                    if (random.nextInt(100) < 20) {
                        p.closeInventory();
                        p.sendMessage("§cUn bruit t'a fait sursauter et tu as lâché l'établi !");
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
