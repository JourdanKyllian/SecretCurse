package fr.siglah.secretCurse.infrastructure.paper.listener.inventory;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class ExclusivityContractBehavior implements CurseBehavior {
    private final JavaPlugin plugin;
    private final UUID targetId;
    private BukkitTask task;

    public ExclusivityContractBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
    }

    @Override
    public void onStart(Player player) {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Player p = Bukkit.getPlayer(targetId);
            if (p != null && p.isOnline() && !p.isDead()) {
                String allowedFamily = null;

                // On scanne l'inventaire entier
                for (int i = 0; i < p.getInventory().getSize(); i++) {
                    ItemStack item = p.getInventory().getItem(i);
                    if (item == null || item.getType().isAir()) continue;

                    String family = getToolFamily(item.getType());
                    if (family != null) {
                        if (allowedFamily == null) {
                            allowedFamily = family; // On enregistre la première famille trouvée
                        } else if (!family.equals(allowedFamily)) {
                            // On rejette les outils d'une autre famille
                            p.getWorld().dropItemNaturally(p.getLocation(), item.clone());
                            p.getInventory().setItem(i, null);
                        }
                    }
                }
            }
        }, 20L, 20L); // Vérifie toutes les secondes
    }

    @Override
    public void onStop(Player player) {
        if (task != null) task.cancel();
    }

    private String getToolFamily(Material mat) {
        String name = mat.name();
        if (name.endsWith("_PICKAXE")) return "PICKAXE";
        if (name.endsWith("_AXE")) return "AXE";
        if (name.endsWith("_SHOVEL")) return "SHOVEL";
        if (name.endsWith("_HOE")) return "HOE";
        if (name.endsWith("_SWORD")) return "SWORD";
        if (name.equals("BOW") || name.equals("CROSSBOW") || name.equals("TRIDENT")) return "RANGED";
        return null;
    }
}
