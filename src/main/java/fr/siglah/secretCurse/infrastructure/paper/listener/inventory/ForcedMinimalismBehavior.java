package fr.siglah.secretCurse.infrastructure.paper.listener.inventory;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * Minimalisme Forcé : condamne les emplacements 9 à 35 (l'inventaire principal)
 * avec des barrières indéplaçables. Le joueur ne peut utiliser que sa hotbar.
 * Les barrières ne tombent pas à la mort et réapparaissent au respawn.
 */
public class ForcedMinimalismBehavior implements CurseBehavior {
    private final JavaPlugin plugin;
    private final UUID targetId;
    private final NamespacedKey barrierKey;

    public ForcedMinimalismBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
        // Ce tag invisible permet de différencier nos barrières des vrais blocs du jeu
        this.barrierKey = new NamespacedKey(plugin, "secretcurse_locked_slot");
    }

    @Override
    public void onStart(Player player) {
        applyBarriers(player);
    }

    @Override
    public void onStop(Player player) {
        removeBarriers(player);
    }

    private void applyBarriers(Player player) {
        ItemStack barrier = createBarrier();
        for (int i = 9; i <= 35; i++) {
            ItemStack current = player.getInventory().getItem(i);

            // Si le joueur a déjà de vrais objets dans son inventaire au lancement, on les jette au sol
            if (current != null && !current.getType().isAir() && !isBarrier(current)) {
                player.getWorld().dropItemNaturally(player.getLocation(), current);
            }

            player.getInventory().setItem(i, barrier);
        }
    }

    private void removeBarriers(Player player) {
        for (int i = 9; i <= 35; i++) {
            ItemStack current = player.getInventory().getItem(i);
            if (isBarrier(current)) {
                player.getInventory().setItem(i, null);
            }
        }
    }

    private ItemStack createBarrier() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Emplacement Bloqué", NamedTextColor.RED)
                .decoration(TextDecoration.ITALIC, false));
        // On marque l'item avec notre tag secret
        meta.getPersistentDataContainer().set(barrierKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        return item;
    }

    private boolean isBarrier(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return Boolean.TRUE.equals(item.getItemMeta().getPersistentDataContainer().get(barrierKey, PersistentDataType.BOOLEAN));
    }

    // --- SÉCURITÉS POUR EMPÊCHER TOUTE INTERACTION AVEC LES BARRIÈRES ---

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getWhoClicked().getUniqueId().equals(targetId)) return;

        // 1. Clic direct sur la barrière
        if (isBarrier(event.getCurrentItem())) {
            event.setCancelled(true);
            return;
        }

        // 2. Tenir la barrière avec la souris (cursor)
        if (isBarrier(event.getCursor())) {
            event.setCancelled(true);
            return;
        }

        // 3. Tenter d'utiliser les touches 1-9 (Hotbar swap) sur un slot bloqué
        if (event.getClick().name().contains("HOTBAR")) {
            int hotbarButton = event.getHotbarButton();
            if (hotbarButton >= 0) {
                ItemStack hotbarItem = event.getWhoClicked().getInventory().getItem(hotbarButton);
                if (isBarrier(hotbarItem)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!event.getWhoClicked().getUniqueId().equals(targetId)) return;

        // Glisser-déposer de la barrière elle-même
        if (isBarrier(event.getOldCursor()) || isBarrier(event.getCursor())) {
            event.setCancelled(true);
            return;
        }

        // CORRECTION DU BUG : Empêcher de répartir (clic-droit glissé) des objets par-dessus une barrière
        for (int rawSlot : event.getRawSlots()) {
            ItemStack targetItem = event.getView().getItem(rawSlot);
            if (isBarrier(targetItem)) {
                event.setCancelled(true); // Annule le drag entier si la souris passe sur un slot interdit
                return;
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!event.getPlayer().getUniqueId().equals(targetId)) return;

        // Appuyer sur "A" ou "Q" en visant la barrière
        if (isBarrier(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
        }
    }

    // --- GESTION DE LA MORT ET DU RESPAWN ---

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!event.getEntity().getUniqueId().equals(targetId)) return;

        // On filtre la liste des objets qui vont tomber au sol : on efface toutes nos barrières !
        event.getDrops().removeIf(this::isBarrier);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (!event.getPlayer().getUniqueId().equals(targetId)) return;

        // On remet les barrières 1 tick (0.05s) après la réapparition pour éviter
        // que Minecraft ne les écrase avec un inventaire vide
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Player p = Bukkit.getPlayer(targetId);
            if (p != null && p.isOnline()) {
                applyBarriers(p);
            }
        }, 1L);
    }
}
