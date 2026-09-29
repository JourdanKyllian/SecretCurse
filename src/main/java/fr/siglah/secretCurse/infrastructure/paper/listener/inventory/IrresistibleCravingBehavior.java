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
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

/**
 * Envie Irrépressible : le slot 9 de la hotbar contient un "sucre magique"
 * illimité, à consommer toutes les 60 secondes sous peine de nausée
 * croissante. Pour obtenir l'animation "comme si on tirait à l'arc", l'objet
 * est en réalité un Arc reskinné : EntityShootBowEvent#setConsumeArrow(false)
 * évite de consommer la flèche, et on annule le tir pour qu'aucune flèche ne
 * parte réellement — seule l'animation de tension/relâchement est utilisée.
 */
public class IrresistibleCravingBehavior implements CurseBehavior {
    private static final int HOTBAR_SLOT = 8; // Slot 9 en jeu = index 8
    private static final long CHECK_INTERVAL_TICKS = 20L; // 1 seconde
    private static final long WARNING_AFTER_TICKS = 60L * 20L; // 60 secondes

    private final JavaPlugin plugin;
    private final UUID targetId;
    private final NamespacedKey magicSugarKey;
    private BukkitTask task;
    private long ticksSinceLastConsumption = 0L;

    public IrresistibleCravingBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
        this.magicSugarKey = new NamespacedKey(plugin, "secretcurse_magic_sugar");
    }

    @Override
    public void onStart(Player player) {
        player.getInventory().setItem(HOTBAR_SLOT, createMagicSugarBow());
        // Une flèche est nécessaire pour pouvoir "tirer" (sinon le client bloque la tension) ;
        // comme setConsumeArrow(false) la préserve, elle n'est donnée qu'une seule fois.
        player.getInventory().addItem(new ItemStack(Material.ARROW, 1));

        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Player p = Bukkit.getPlayer(targetId);
            if (p == null || !p.isOnline() || p.isDead()) return;

            ticksSinceLastConsumption += CHECK_INTERVAL_TICKS;
            if (ticksSinceLastConsumption >= WARNING_AFTER_TICKS) {
                // Nausée croissante : l'amplificateur augmente avec le temps de retard
                int overdueSeconds = (int) ((ticksSinceLastConsumption - WARNING_AFTER_TICKS) / 20L);
                int amplifier = Math.min(3, overdueSeconds / 15);
                p.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 40, amplifier, true, false));
            }

            // Recloner l'item si jamais il a été jeté/perdu, pour garder la contrainte permanente
            ItemStack current = p.getInventory().getItem(HOTBAR_SLOT);
            if (current == null || !isMagicSugar(current)) {
                p.getInventory().setItem(HOTBAR_SLOT, createMagicSugarBow());
            }
            if (!p.getInventory().contains(Material.ARROW)) {
                p.getInventory().addItem(new ItemStack(Material.ARROW, 1));
            }
        }, CHECK_INTERVAL_TICKS, CHECK_INTERVAL_TICKS);
    }

    @Override
    public void onStop(Player player) {
        if (task != null) task.cancel();

        ItemStack item = player.getInventory().getItem(HOTBAR_SLOT);
        if (item != null && isMagicSugar(item)) {
            player.getInventory().setItem(HOTBAR_SLOT, null);
        }
        player.removePotionEffect(PotionEffectType.NAUSEA);
    }

    @EventHandler
    public void onShootBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!player.getUniqueId().equals(targetId)) return;
        if (event.getBow() == null || !isMagicSugar(event.getBow())) return;

        event.setConsumeArrow(false);
        event.setCancelled(true); // Aucune flèche ne part réellement, ce n'est qu'une "consommation"

        ticksSinceLastConsumption = 0L;
        player.removePotionEffect(PotionEffectType.NAUSEA);
        player.sendActionBar(Component.text("Mmh, sucré...", NamedTextColor.LIGHT_PURPLE));
    }

    private ItemStack createMagicSugarBow() {
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta meta = bow.getItemMeta();
        meta.displayName(Component.text("🍬 Sucre Magique", NamedTextColor.LIGHT_PURPLE)
                .decoration(TextDecoration.ITALIC, false));
        meta.setUnbreakable(true);
        meta.getPersistentDataContainer().set(magicSugarKey, PersistentDataType.BOOLEAN, true);
        bow.setItemMeta(meta);
        return bow;
    }

    private boolean isMagicSugar(ItemStack item) {
        if (item.getType() != Material.BOW || !item.hasItemMeta()) return false;
        Boolean tagged = item.getItemMeta().getPersistentDataContainer().get(magicSugarKey, PersistentDataType.BOOLEAN);
        return Boolean.TRUE.equals(tagged);
    }
}
