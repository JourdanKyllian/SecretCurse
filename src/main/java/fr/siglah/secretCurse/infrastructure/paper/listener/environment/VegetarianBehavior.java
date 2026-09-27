package fr.siglah.secretCurse.infrastructure.paper.listener.environment;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

public class VegetarianBehavior implements CurseBehavior {
    private final UUID targetId;

    private static final Set<Material> MEAT_MATERIALS = EnumSet.of(
            Material.PORKCHOP, Material.COOKED_PORKCHOP,
            Material.BEEF, Material.COOKED_BEEF,
            Material.CHICKEN, Material.COOKED_CHICKEN,
            Material.RABBIT, Material.COOKED_RABBIT,
            Material.MUTTON, Material.COOKED_MUTTON,
            Material.ROTTEN_FLESH
    );

    public VegetarianBehavior(JavaPlugin plugin, UUID targetId) {
        this.targetId = targetId;
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        if (!event.getPlayer().getUniqueId().equals(targetId)) return;

        if (MEAT_MATERIALS.contains(event.getItem().getType())) {
            event.setCancelled(true);
            // Empoisonnement sévère (Niveau 2 pendant 5 secondes)
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
        }
    }
}
