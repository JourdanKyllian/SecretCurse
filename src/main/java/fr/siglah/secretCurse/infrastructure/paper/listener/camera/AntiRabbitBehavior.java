package fr.siglah.secretCurse.infrastructure.paper.listener.camera;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class AntiRabbitBehavior implements CurseBehavior {
    private final UUID targetId;

    public AntiRabbitBehavior(JavaPlugin plugin, UUID targetId) {
        this.targetId = targetId;
    }

    @EventHandler
    public void onJump(PlayerJumpEvent event) {
        if (!event.getPlayer().getUniqueId().equals(targetId)) return;

        // Inflige Lenteur III pendant 2 secondes (40 ticks) après chaque saut
        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 2, false, false));
    }
}
