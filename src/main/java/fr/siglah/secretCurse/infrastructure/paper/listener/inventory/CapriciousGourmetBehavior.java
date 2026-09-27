package fr.siglah.secretCurse.infrastructure.paper.listener.inventory;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class CapriciousGourmetBehavior implements CurseBehavior {
    private final UUID targetId;
    private Material lastFoodEaten = null;

    public CapriciousGourmetBehavior(JavaPlugin plugin, UUID targetId) {
        this.targetId = targetId;
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        if (!event.getPlayer().getUniqueId().equals(targetId)) return;

        Material tryingToEat = event.getItem().getType();

        if (tryingToEat == lastFoodEaten) {
            event.setCancelled(true);
            event.getPlayer().sendActionBar(Component.text("Tu n'as plus envie de manger ça pour le moment !", NamedTextColor.RED));
        } else {
            // Mise à jour de la dernière nourriture ingérée
            lastFoodEaten = tryingToEat;
        }
    }
}
