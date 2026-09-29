package fr.siglah.secretCurse.infrastructure.paper.listener.inventory;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

/**
 * Allergie aux Mains Sales : tenir en main principale ou secondaire un bloc
 * "sale" (Terre, Terre Grossière, Sable, Sable Rouge, Gravier, Boue, Racines
 * de Mangrove Boueuses) l'éjecte instantanément au sol.
 * <p>
 * Vérifié en boucle courte plutôt que sur un event précis : ça couvre tous les
 * cas (changement de slot actif, drag&drop, swap main/offhand...) sans avoir
 * à écouter une demi-douzaine d'événements différents.
 */
public class DirtyHandsAllergyBehavior implements CurseBehavior {

    private static final Set<Material> DIRTY_BLOCKS = EnumSet.of(
            Material.DIRT, Material.COARSE_DIRT,
            Material.SAND, Material.RED_SAND, Material.GRAVEL,
            Material.MUD, Material.MUDDY_MANGROVE_ROOTS
    );

    private final JavaPlugin plugin;
    private final UUID targetId;
    private BukkitTask task;

    public DirtyHandsAllergyBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
    }

    @Override
    public void onStart(Player player) {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Player p = Bukkit.getPlayer(targetId);
            if (p == null || !p.isOnline() || p.isDead()) return;

            ejectIfDirty(p, p.getInventory().getItemInMainHand(), EquipmentSlot.HAND);
            ejectIfDirty(p, p.getInventory().getItemInOffHand(), EquipmentSlot.OFF_HAND);
        }, 2L, 2L);
    }

    @Override
    public void onStop(Player player) {
        if (task != null) task.cancel();
    }

    private void ejectIfDirty(Player player, ItemStack item, EquipmentSlot slot) {
        if (item == null || !DIRTY_BLOCKS.contains(item.getType())) return;

        player.getWorld().dropItemNaturally(player.getLocation(), item.clone());
        if (slot == EquipmentSlot.HAND) {
            player.getInventory().setItemInMainHand(null);
        } else {
            player.getInventory().setItemInOffHand(null);
        }
    }
}
