package fr.siglah.secretCurse.infrastructure.paper.listener.camera;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

/**
 * Poids Fantôme : dès que le joueur est en train de chuter depuis 3 blocs ou
 * plus, une Chute Lente s'applique automatiquement (courte durée, réappliquée
 * en continu tant que la chute se poursuit). Bukkit remet getFallDistance()
 * à 0 dès l'atterrissage, donc l'effet s'arrête naturellement de se réappliquer.
 */
public class PhantomWeightBehavior implements CurseBehavior {
    private static final float FALL_THRESHOLD = 3.0F;

    private final UUID targetId;

    public PhantomWeightBehavior(JavaPlugin plugin, UUID targetId) {
        this.targetId = targetId;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!event.getPlayer().getUniqueId().equals(targetId)) return;

        if (event.getPlayer().getFallDistance() >= FALL_THRESHOLD && !event.getPlayer().isOnGround()) {
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20, 0, true, false));
        }
    }
}
